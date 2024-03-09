package springboot.web.downloader.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.dto.Edge;
import springboot.web.downloader.dto.Node;
import springboot.web.downloader.dto.SiteMapDto;
import springboot.web.downloader.dto.Task;
import springboot.web.downloader.enums.NodeType;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.jaxb.IXmlUrlSet;
import springboot.web.downloader.jaxb.http.XmlUrl;
import springboot.web.downloader.registry.TaskRegistry;
import springboot.web.downloader.utils.Utils;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is abstract task created after client request
 * which must be processed in separate thread.
 * WebTask have {@code taskId} identifier thought it
 * client able to monitoring WebTask status.
 * WebTask is Prototype bean and creation new way in {@code Config.class}
 */
@Slf4j
public class WebTask implements Runnable {

    private final Wget wget;
    private final Zip zip;
    private final Task task;

    public WebTask(Wget wget, Zip zip, Task task) {
        this.wget = wget;
        this.zip = zip;
        this.task = task;
    }

    @Override
    @SneakyThrows
    public void run() {
        TaskRegistry.getRegistry().put(task.getTaskId(), task.setStatusTask(StatusTask.RUNNING));
        StatusTask statusTask = switch (task.getParams().getTypeTask()) {
            case DOWNLOAD -> requireDownload();
            case ESTIMATE -> estimateSize();
            case BUILD_MAP -> buildMap();
        };
        TaskRegistry.getRegistry().put(task.getTaskId(), task.setStatusTask(statusTask));
    }

    private StatusTask requireDownload() throws InterruptedException {
        try {
            int exitCode = 1;
            String dir = WebDownloader.SITES + task.getTaskId();
            Utils.createDirectory(dir);
            if (wget.download(task.getParams().getUri(), dir) == 0) {
                exitCode = zip.wrapToZip(task.getTaskId());
            }
            Utils.wgetLogging(dir);
            return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return StatusTask.ERROR;
        }
    }

    private StatusTask estimateSize() throws InterruptedException {
        try {
            String dir = WebDownloader.SITES + task.getTaskId();
            Utils.createDirectory(dir);
            int exitCode = wget.estimate(task.getParams().getUri(), dir);
            return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return StatusTask.ERROR;
        }
    }

    private StatusTask buildMap() throws InterruptedException {
        try {
            // 1 - create or generate sitemap.xml
            IXmlUrlSet xmlUrlSet = wget.getSiteMap(task.getParams().getUri(), task.getTaskId());
            // 2 - transform to SiteMapDto.class
            SiteMapDto siteMap = buildTree(xmlUrlSet);
            TaskRegistry.getResults().put(task.getTaskId(), siteMap);
            return StatusTask.DONE;
        } catch (IOException | JAXBException | SAXException ex) {
            log.error(ex.getMessage());
            return StatusTask.ERROR;
        }
    }

    private SiteMapDto buildTree(IXmlUrlSet xmlUrlSet) {
        List<String> urls = xmlUrlSet.getUrl().stream().map(XmlUrl::getLoc).toList();
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Map<String, Set<String>> tree = new HashMap<>();
        // build tree
        Map<Integer, List<String>> levelUrlMap = urls.stream().collect(Collectors.groupingBy(Utils::calcUrlLevel));
        urls.forEach(root -> {
            int urlLevel = Utils.calcUrlLevel(root);
            List<Integer> levels = levelUrlMap.keySet().stream().filter(l -> l > urlLevel).sorted().toList();
            Set<String> urlLinks = new HashSet<>();
            levels.forEach(level -> urlLinks.addAll(levelUrlMap.get(level).stream().filter(
                    url -> url.contains(root) && urlLinks.stream().noneMatch(url::contains)).toList()));
            tree.put(root, urlLinks);
        });
        // fill nodes and edges
        tree.forEach((node, children) -> {
            int level = calculateNodeLevel(tree, node, 0);
            nodes.add(
                    new Node(node, NodeType.getType(tree, level, node), Node.getColorByLevel(level),
                    new Node.Data(node, Node.getLabelByLevel(level, node))));
            children.forEach(childNode -> edges.add(
                    new Edge(String.valueOf((node + childNode).hashCode()), node, childNode)));
        });

        return new SiteMapDto(nodes, edges);
    }

    private int calculateNodeLevel(Map<String, Set<String>> urlGraph, String url, int level) {
        // search potential parent
        Optional<String> parent = urlGraph.keySet().stream()
                .filter(url::contains).filter(p -> urlGraph.get(p).contains(url)).findFirst();
        return parent.map(s -> calculateNodeLevel(urlGraph, s, level + 1)).orElse(level);
    }
}
