package springboot.web.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;


@Slf4j
@SpringBootApplication
public class WebDownloader {

	public final static String baseSites = System.getProperty("user.home") + "/sites/";
	public final static String baseArchived = System.getProperty("user.home") + "/archived/";

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WebDownloader.class, args);
		Utils.prepareDirectories();
	}

}
