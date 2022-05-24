package springboot.web.downloader.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springboot.web.downloader.TestUtils;
import springboot.web.downloader.enums.ErrorStruct;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.registory.TaskRegistry;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestServiceTest {

    private final RestService restService;
    private static String taskId;

    @Autowired
    RestServiceTest(RestService restService) {
        this.restService = restService;
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
        queryWithClientUrlSuccess(this.restService::requireDownload);
    }

    @Test
    @Order(2)
    void getZipSuccess() {
        final var response = this.restService.getZip(taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(28, response.getHeaders().getContentLength() / 100);
    }

    @Test
    void getZipError() {
        notFoundTest(this.restService::getZip);
    }

    @Test
    void requireDownloadError() {
        queryWithClientUrlError(this.restService::requireDownload);
    }

    @Test
    void statusTaskNotFound() {
        notFoundTest(this.restService::statusTask);
    }

    private void notFoundTest(Function<String, ResponseEntity<?>> restMethod){
        final var response = restMethod.apply("XXX-VVV-III");
        ErrorStruct errorStruct = Objects.requireNonNull((ErrorStruct) response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), errorStruct.getErrorCode());
    }

    @Test
    void statusTaskRunning() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(false, null, false);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.RUNNING, response.getBody());
    }

    @Test
    void statusTaskError() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(true, StatusTask.ERROR, false);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.ERROR, response.getBody());
    }

    @Test
    void statusTaskDone() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(true, StatusTask.DONE, false);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.DONE, response.getBody());
    }

    @Test
    void statusTaskUndefined() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(true, StatusTask.DONE, true);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.UNDEFINED, response.getBody());
    }

    private ResponseEntity<?> statusTaskTest(boolean isDone, StatusTask statusTask, boolean isThrowable)
            throws ExecutionException, InterruptedException {
        final String task = this.prepareMockTask(isDone, statusTask, isThrowable);
        return restService.statusTask(task);
    }

    @SuppressWarnings("unchecked")
    private String prepareMockTask(boolean isDone, StatusTask statusTask, boolean isThrowable)
            throws InterruptedException, ExecutionException {
        final String task = UUID.randomUUID().toString();
        Future<StatusTask> mockFuture = Mockito.mock(Future.class);
        Mockito.when(mockFuture.isDone()).thenReturn(isDone);
        if(isThrowable)
            Mockito.when(mockFuture.get()).thenThrow(ExecutionException.class);
        else Mockito.when(mockFuture.get()).thenReturn(statusTask);
        TaskRegistry.registry.put(task, mockFuture);
        return task;
    }

    @Test
    @Order(3)
    void estimateSizeSuccess() throws InterruptedException, ExecutionException {
        queryWithClientUrlSuccess(this.restService::estimateSize);
    }

    @Test
    @Order(4)
    void discoverSizeTestSuccess(){
        var response = this.restService.discoverSize(taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        int size = Integer.parseInt(((String) Objects.requireNonNull(response.getBody())).substring(0,4));
        Assertions.assertEquals(39, size / 100);

    }

    @Test
    void discoverSizeTestFileNotFound() throws ExecutionException, InterruptedException {
        final String task = this.prepareMockTask(true, StatusTask.DONE, false);
        var response = this.restService.discoverSize(task);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("File " + task + " not found", response.getBody());
    }

    @Test
    void estimateSizeError() {
        queryWithClientUrlError(this.restService::estimateSize);
    }

    private void queryWithClientUrlSuccess(Function<String, ResponseEntity<?>> rest)
            throws InterruptedException, ExecutionException {
        String successUrl = "https://locallhost.com/";
        final var response = rest.apply(successUrl);
        taskId = Objects.requireNonNull(response.getBody()).toString();
        final var future = TaskRegistry.registry.get(taskId);
        StatusTask statusTask = future.get();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(UUID.fromString(taskId).getClass(), UUID.class);
        Assertions.assertEquals(StatusTask.DONE, statusTask);
    }

    private void queryWithClientUrlError(Function<String, ResponseEntity<?>> rest) {
        String errorUrl = "https://unreacheble-XXX-url.guru/";
        final var response = rest.apply(errorUrl);
        ErrorStruct errorStruct = Objects.requireNonNull((ErrorStruct) response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorStruct.getErrorCode());
    }

}