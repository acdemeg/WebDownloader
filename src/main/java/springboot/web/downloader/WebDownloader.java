package springboot.web.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Slf4j
@SpringBootApplication
public class WebDownloader {

	public final static String baseDir = "src/main/resources/sites/";

	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
		var app = SpringApplication.run(WebDownloader.class, args);
//		Wget wget = app.getBean(Wget.class);
//		String dir = UUID.randomUUID().toString();
//		Utils.factory().createDirectory(baseDir + dir);
//		wget.download("https://metanit.com/", baseDir + dir);
//		Zip zip = app.getBean(Zip.class);
//		zip.wrapToZip(dir);
//		System.exit(0);
	}

}
