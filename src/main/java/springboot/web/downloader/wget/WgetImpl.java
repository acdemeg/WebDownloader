package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.enums.NativeProcessName;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class WgetImpl implements Wget {

    private final WgetOptions wgetOptions;

    @Autowired
    public WgetImpl(WgetOptions wgetOptions) {
        this.wgetOptions = wgetOptions;
    }

    @Override
    public int download(final String URI, final String dir) throws InterruptedException, IOException {
        String command = wgetOptions.getWgetDownload() + URI;
        return Utils.runProcess(command, NativeProcessName.WGET_DOWNLOAD.name(), dir);
    }

    @Override
    public int estimate(final String URI, final String dir) throws IOException, InterruptedException {
        String command = wgetOptions.getWgetEstimate() + URI;
        return Utils.runProcess(command, NativeProcessName.WGET_ESTIMATE.name(), dir);
    }

    @Override
    public String getSiteMap(final String URI, final String dir) throws IOException, InterruptedException {
        ResponseEntity<String> entity;
        try {
            entity = new RestTemplate().getForEntity(URI + "sitemap.xml", String.class);
            if (entity.getStatusCode().is2xxSuccessful()) {
                return entity.getBody();
            }
        }
        catch (final HttpClientErrorException ex) {
            Path sh = Paths.get(WebDownloader.SITEMAP_GENERATOR_SCRIPT).toAbsolutePath();
            String sitemapDir = WebDownloader.BASE_SITEMAPS + "/" + dir;
            Utils.createDirectory(sitemapDir);
            int exitCode = Utils.runProcess(sh + " " + URI + " " + sitemapDir,
                    NativeProcessName.WGET_GENERATE_SITEMAP.name(), sitemapDir);
            if (exitCode == 0) {
                File file = new File(sitemapDir + "/sitemap.xml");
                return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            }
        }
        return "";
    }
}
