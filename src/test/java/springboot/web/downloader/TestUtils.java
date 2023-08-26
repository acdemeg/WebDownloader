package springboot.web.downloader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestUtils {

    public static void prepareTestEnv() throws IOException {
        FileUtils.forceMkdir(new File(WebDownloader.BASE_SITES));
        FileUtils.forceMkdir(new File(WebDownloader.BASE_ARCHIVED));
        FileUtils.forceMkdir(new File(WebDownloader.BASE_SITEMAPS));
    }

    public static void discardTestEnv() throws IOException {
        FileUtils.cleanDirectory(new File(WebDownloader.BASE_SITES));
        FileUtils.cleanDirectory(new File(WebDownloader.BASE_ARCHIVED));
        FileUtils.cleanDirectory(new File(WebDownloader.BASE_SITEMAPS));
    }
}
