package springboot.web.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;


@Slf4j
@SpringBootApplication
public class WebDownloader {

    private static final String USER_HOME = System.getProperty("user.home");
    public static final String SCRIPTS = USER_HOME + "/scripts/";
    public static final String SITES = USER_HOME + "/sites/";
    public static final String ARCHIVED = USER_HOME + "/archived/";
    public static final String SITEMAPS = USER_HOME + "/sitemaps/";
    public static final String DEFAULT_LANGUAGE = "Eng";

    public static void main(String[] args) throws IOException {
        Utils.prepareEnv();
        SpringApplication.run(WebDownloader.class, args);
    }
}
