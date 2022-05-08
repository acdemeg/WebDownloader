package springboot.web.downloader.wget;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Service
public class WgetImpl implements Wget {

    private final String userAgent = "--user-agent=\"Mozilla/5.0 (X11; Linux x86_64)" +
            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36\"";
    private final String referer = "--referer=\"https://www.google.com/\"";
    private final String ignoreRobotsTxt = "--execute robots=off";
    private final String noCheckCertificate = "--no-check-certificate";
    private final String pageRequisites = "--page-requisites";
    private final String htmlExtension = "--html-extension";
    private final String convertLinks = "--convert-links";
    private final String level = "1";
    private final String backgroundRun = "--background";
    private final String mirror = "--recursive --timestamping --level " + level + " --dont-remove-listing";
    private final String wget = String.format("wget %s %s %s %s %s %s %s %s %s ", userAgent, referer,
            ignoreRobotsTxt, noCheckCertificate, pageRequisites, htmlExtension, convertLinks, backgroundRun, mirror);

    @Override
    public void download(String URI) throws ExecutionException, InterruptedException, IOException {
        var process = new ProcessBuilder("sh", "-c", wget + URI /*"wget -b http://java-course.ru/"*/)
                .directory(new File(System.getProperty("user.home"))).start();
        var grubber = new StreamGrubber(process.getInputStream(), System.out::println);
        var exec = Executors.newSingleThreadExecutor();
        exec.submit(grubber).get();
        exec.shutdown();
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}
