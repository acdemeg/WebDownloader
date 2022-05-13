package springboot.web.downloader.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import springboot.web.downloader.TestUtils;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.StatusTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestServiceTest {

    private final RestService restService;
    private static String taskId;

    @Autowired
    RestServiceTest(ConfigurableApplicationContext applicationContext, RestService restService) {
        this.restService = restService;
        WebDownloader.appContext = applicationContext;
    }

    @BeforeAll
    static void prepareEnvTest() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestUtils.prepareTestEnv();
    }

    @AfterAll
    static void discardEnvTest() throws IOException {
        TestUtils.discardTestEnv();
    }

    @Test
    @Order(1)
    void requireDownload() throws InterruptedException, ExecutionException {
        final var response = this.restService.requireDownload("https://locallhost.com/");
        taskId = Objects.requireNonNull(response.getBody()).toString();
        final var future = TaskRegistry.registry.get(taskId);
        StatusTask statusTask = future.get();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(UUID.fromString(taskId).getClass(), UUID.class);
        Assertions.assertEquals(StatusTask.DONE, statusTask);
    }

    @Test
    @Order(2)
    void getZip() throws FileNotFoundException {
        final var response = this.restService.getZip(taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(28, response.getHeaders().getContentLength() / 100);
    }

}