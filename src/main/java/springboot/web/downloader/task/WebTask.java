package springboot.web.downloader.task;

import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.dto.Edge;
import springboot.web.downloader.dto.Node;
import springboot.web.downloader.dto.SiteMapDto;
import springboot.web.downloader.enums.NodeType;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.enums.TypeTask;
import springboot.web.downloader.jaxb.XmlUrl;
import springboot.web.downloader.jaxb.XmlUrlSet;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.utils.Utils;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * This class is abstract task created after client request
 * which must be processed in separate thread.
 * WebTask have {@code taskId} identifier thought it
 * client able to monitoring WebTask status.
 * WebTask is Prototype bean and creation new way in {@code Config.class}
 * {<code>
 * public FunctionTwoArgs<String, String, WebTask> webTaskFactory()
 * </code>}
 */
public class WebTask implements Callable<StatusTask> {

    private final Wget wget;
    private final Zip zip;
    private final String taskId;
    private final String uri;
    private final TypeTask typeTask;

    public WebTask(Wget wget, Zip zip, String taskId, String uri, TypeTask typeTask) {
        this.wget = wget;
        this.zip = zip;
        this.taskId = taskId;
        this.uri = uri;
        this.typeTask = typeTask;
    }

    @Override
    public StatusTask call() throws Exception {
        switch (typeTask) {
            case DOWNLOAD: return requireDownload();
            case ESTIMATE: return estimateSize();
            case BUILD_MAP: return buildMap();
            default: return StatusTask.UNDEFINED;
        }
    }

    private StatusTask requireDownload() throws IOException, InterruptedException {
        int exitCode = 1;
        String dir = WebDownloader.BASE_SITES + taskId;
        Utils.createDirectory(dir);
        if (wget.download(uri, dir) == 0) {
            exitCode = zip.wrapToZip(taskId);
        }
        Utils.wgetLogging(dir);
        return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
    }

    private StatusTask estimateSize() throws IOException, InterruptedException {
        String dir = WebDownloader.BASE_SITES + taskId;
        Utils.createDirectory(dir);
        int exitCode = wget.estimate(uri, dir);
        return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
    }

    private StatusTask buildMap() throws JAXBException {
        // 1 - create or get sitemap.xml
        String xml = wget.getSiteMap(uri + "/sitemap.xml");
        if (xml.isEmpty())
            return StatusTask.ERROR;
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlUrlSet.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StringReader xmlReader = new StringReader(xml);
        XmlUrlSet xmlUrlSet = (XmlUrlSet) jaxbUnmarshaller.unmarshal(xmlReader);
        xmlReader.close();
        // 2 - transform to SiteMapDto.class
        SiteMapDto siteMap = buildTree(xmlUrlSet);
        TaskRegistry.getResults().put(taskId, siteMap);
        return StatusTask.DONE;
    }

    private SiteMapDto buildTree(XmlUrlSet xmlUrlSet) {
        List<String> urls = xmlUrlSet.getUrl().stream().map(XmlUrl::getLoc).collect(Collectors.toList());
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Map<String, Set<String>> tree = new HashMap<>();
        // build tree
        Map<Integer, List<String>> levelUrlMap = urls.stream().collect(Collectors.groupingBy(Utils::calcUrlLevel));
        urls.forEach(root -> {
            int urlLevel = Utils.calcUrlLevel(root);
            List<Integer> levels = levelUrlMap.keySet().stream().filter(l -> l > urlLevel).sorted().collect(Collectors.toList());
            Set<String> urlLinks = new HashSet<>();
            levels.forEach(level -> urlLinks.addAll(levelUrlMap.get(level).stream().filter(
                    url -> url.contains(root) && urlLinks.stream().noneMatch(url::contains)).collect(Collectors.toList())));
            tree.put(root, urlLinks);
        });
        // fill nodes and edges
        tree.forEach((node, children) ->  {
            int level = calculateNodeLevel(tree, node, 0);
            nodes.add(new Node(node, NodeType.getType(tree, level, node), Node.getColorByLevel(level), new Node.Data(node)));
            children.forEach(childNode -> edges.add(new Edge(node + childNode, node, childNode)));
        });

        return new SiteMapDto(0, nodes, edges);
    }

    private int calculateNodeLevel(Map<String, Set<String>> urlGraph, String url, int level) {
        // search potential parent
        Optional<String> parent = urlGraph.keySet().stream()
                .filter(url::contains).filter(p -> urlGraph.get(p).contains(url)).findFirst();
        return parent.map(s -> calculateNodeLevel(urlGraph, s, level + 1)).orElse(level);
    }
}
