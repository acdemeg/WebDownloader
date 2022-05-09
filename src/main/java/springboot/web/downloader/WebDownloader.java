package springboot.web.downloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@SpringBootApplication
public class WebDownloader {
	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
		var app = SpringApplication.run(WebDownloader.class, args);
		Wget wget = app.getBean(Wget.class);
		wget.download("https://metanit.com/");
		Zip zip = app.getBean(Zip.class);
		zip.wrapToZip("metanit.com");
		System.exit(0);
	}

}
