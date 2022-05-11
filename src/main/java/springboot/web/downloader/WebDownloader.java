package springboot.web.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;


@Slf4j
@SpringBootApplication
public class WebDownloader {

	public final static String baseSites = "src/main/resources/sites/";
	public final static String baseArchived = "src/main/resources/archived/";
	public static ConfigurableApplicationContext appContext;

	public static void main(String[] args) throws IOException {
		appContext = SpringApplication.run(WebDownloader.class, args);
		Utils.prepareDirectories();
	}

}
