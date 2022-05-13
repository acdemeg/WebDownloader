package springboot.web.downloader.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import springboot.web.downloader.TestUtils;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.StatusTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class RestServiceTest {

    private final RestService restService;

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
    void requireDownload() throws InterruptedException, ExecutionException {

        final var response = this.restService.requireDownload("https://metanit.com/");
        String taskId = Objects.requireNonNull(response.getBody()).toString();
        final var future = TaskRegistry.registry.get(taskId);
        StatusTask statusTask = future.get();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(UUID.fromString(taskId).getClass(), UUID.class);
        Assertions.assertEquals(StatusTask.DONE, statusTask);
    }
}