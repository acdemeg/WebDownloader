package springboot.web.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springboot.web.downloader.utils.Utils;


@Slf4j
@SpringBootApplication
public class WebDownloader {

    private static final String HOME = System.getProperty("user.home") + "/web-downloader-data/";
    public static final String SCRIPTS = HOME + "scripts/";
    public static final String SITES = HOME + "sites/";
    public static final String ARCHIVED = HOME + "archived/";
    public static final String SITEMAPS = HOME + "sitemaps/";
    public static final String REGISTRY = HOME + "registry/";
    public static final String DEFAULT_LANGUAGE = "Eng";

    static {
        Utils.prepareEnv();
    }

    public static void main(String[] args) {
        SpringApplication.run(WebDownloader.class, args);
    }
}
