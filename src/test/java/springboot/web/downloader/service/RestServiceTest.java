package springboot.web.downloader.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import springboot.web.downloader.TestUtils;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.enums.ErrorStruct;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.registory.TaskRegistry;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    void requireDownloadSuccess() throws InterruptedException, ExecutionException {
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
    void getZipSuccess() {
        final var response = this.restService.getZip(taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(28, response.getHeaders().getContentLength() / 100);
    }

    @Test
    void requireDownloadError() {
        final var response = this.restService.requireDownload("https://unreacheble-XXX-url.guru/");
        ErrorStruct errorStruct = Objects.requireNonNull((ErrorStruct) response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorStruct.getErrorCode());
    }

    @Test
    void getZipError() {
        final var response = this.restService.getZip("XXX-VVV-III");
        ErrorStruct errorStruct = Objects.requireNonNull((ErrorStruct) response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), errorStruct.getErrorCode());
    }

    @Test
    void statusTaskNotFound() {
        final var response = this.restService.statusTask("XXX-VVV-III");
        ErrorStruct errorStruct = Objects.requireNonNull((ErrorStruct) response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), errorStruct.getErrorCode());
    }

    @Test
    @SuppressWarnings("unchecked")
    void statusTaskRunning() {
        final String task = UUID.randomUUID().toString();
        Future<StatusTask> mockFuture = Mockito.mock(Future.class);
        Mockito.when(mockFuture.isDone()).thenReturn(false);
        TaskRegistry.registry.put(task, mockFuture);

        var response = restService.statusTask(task);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.RUNNING, response.getBody());
    }

    @Test
    @SuppressWarnings("unchecked")
    void statusTaskError() throws ExecutionException, InterruptedException {
        final String task = UUID.randomUUID().toString();
        Future<StatusTask> mockFuture = Mockito.mock(Future.class);
        Mockito.when(mockFuture.isDone()).thenReturn(true);
        Mockito.when(mockFuture.get()).thenReturn(StatusTask.ERROR);
        TaskRegistry.registry.put(task, mockFuture);

        var response = restService.statusTask(task);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.ERROR, response.getBody());
    }

    @Test
    @SuppressWarnings("unchecked")
    void statusTaskDone() throws ExecutionException, InterruptedException {
        final String task = UUID.randomUUID().toString();
        Future<StatusTask> mockFuture = Mockito.mock(Future.class);
        Mockito.when(mockFuture.isDone()).thenReturn(true);
        Mockito.when(mockFuture.get()).thenReturn(StatusTask.DONE);
        TaskRegistry.registry.put(task, mockFuture);

        var response = restService.statusTask(task);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.DONE, response.getBody());
    }

    @Test
    @SuppressWarnings("unchecked")
    void statusTaskUndefined() throws ExecutionException, InterruptedException {
        final String task = UUID.randomUUID().toString();
        Future<StatusTask> mockFuture = Mockito.mock(Future.class);
        Mockito.when(mockFuture.isDone()).thenReturn(true);
        Mockito.when(mockFuture.get()).thenThrow(ExecutionException.class);
        TaskRegistry.registry.put(task, mockFuture);

        var response = restService.statusTask(task);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.UNDEFINED, response.getBody());
    }

}