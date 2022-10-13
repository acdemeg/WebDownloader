package springboot.web.downloader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class WebDownloaderTests {

	@Autowired
	private final ApplicationContext appContext;

	WebDownloaderTests(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	@Test
	void contextLoads() {
		Assertions.assertNotNull(this.appContext);
	}

}
