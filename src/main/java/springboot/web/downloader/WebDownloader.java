package springboot.web.downloader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;


@Slf4j
@SpringBootApplication
public class WebDownloader {

    private static final String USER_HOME = System.getProperty("user.home");
    public static final String BASE_SITES = USER_HOME + "/sites/";
    public static final String BASE_ARCHIVED = USER_HOME + "/archived/";
    public static final String BASE_SITEMAPS = USER_HOME + "/sitemaps";
    public static final String DEFAULT_LANGUAGE = "Eng";
    public static final String DISCOVER_SIZE_SCRIPT = "./src/main/resources/discover-size.sh";
    public static final String SITEMAP_GENERATOR_SCRIPT = "./src/main/resources/sitemap-generator.sh";

    public static void main(String[] args) throws IOException {
        FileUtils.forceMkdir(new File(BASE_SITES));
        FileUtils.forceMkdir(new File(BASE_ARCHIVED));
        FileUtils.forceMkdir(new File(BASE_SITEMAPS));
        log.info("Create directories if not exist");
        SpringApplication.run(WebDownloader.class, args);
    }

}
