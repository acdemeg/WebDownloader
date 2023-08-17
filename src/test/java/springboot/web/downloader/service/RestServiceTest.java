package springboot.web.downloader.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import springboot.web.downloader.TestUtils;
import springboot.web.downloader.dto.ResponseDto;
import springboot.web.downloader.dto.SiteMapDto;
import springboot.web.downloader.enums.ErrorMessage;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.utils.FunctionTwoArgs;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import static springboot.web.downloader.WebDownloader.DEFAULT_LANGUAGE;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestServiceTest {

    private final RestService restService;
    private static String taskId;

    @Autowired
    RestServiceTest(final RestService restService) {
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
    @Order(10)
    void requireDownloadSuccess() throws InterruptedException, ExecutionException {
        queryWithClientUrlSuccess(this.restService::requireDownload, "https://locallhost.com/");
    }

    @Test
    @Order(10)
    void requireDownloadError() {
        queryWithClientUrlError(this.restService::requireDownload);
    }

    @Test
    @Order(20)
    void getZipSuccess() throws NoSuchFileException {
        final var res1 = this.restService.find(taskId, DEFAULT_LANGUAGE);
        Assertions.assertEquals(HttpStatus.OK, res1.getStatusCode());
        String fileName = Objects.requireNonNull(res1.getBody()).getResult();
        final var res2 = this.restService.getZip(fileName);
        Assertions.assertEquals(HttpStatus.OK, res2.getStatusCode());
        Assertions.assertEquals(28, res2.getHeaders().getContentLength() / 100);
    }

    @Test
    @Order(20)
    void getZipError() {
        Exception thrown = Assertions.assertThrows(
                NoSuchFileException.class,
                () -> this.restService.getZip("XXX-VVV-III")
        );
        Assertions.assertEquals(thrown.getClass(), NoSuchFileException.class);
    }

    @Test
    @Order(30)
    void buildMapSiteError() {
        queryWithClientUrlError(this.restService::mapSite);
    }

    @Test
    @Order(30)
    void buildMapSiteSuccessOne() throws ExecutionException, InterruptedException {
        queryWithClientUrlSuccess(this.restService::mapSite, "https://java-course.ru/");
    }

    @Test
    @Order(31)
    void getJsonGraphSuccessOne() {
        final var res = this.restService.getJsonGraph(taskId, DEFAULT_LANGUAGE);
        SiteMapDto siteMap = (SiteMapDto) res.getBody();
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(siteMap);
        Assertions.assertEquals(75, siteMap.getNodes().size());
        Assertions.assertEquals(74, siteMap.getEdges().size());
    }

    @Test
    @Order(32)
    void buildMapSiteSuccessTwo() throws ExecutionException, InterruptedException {
        queryWithClientUrlSuccess(this.restService::mapSite, "https://locallhost.com/");
    }

    @Test
    @Order(33)
    void getJsonGraphSuccessTwo() {
        final var res = this.restService.getJsonGraph(taskId, DEFAULT_LANGUAGE);
        SiteMapDto siteMap = (SiteMapDto) res.getBody();
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(siteMap);
        Assertions.assertEquals(1, siteMap.getNodes().size());
        Assertions.assertEquals(0, siteMap.getEdges().size());
    }

    @Test
    @Order(40)
    void getJsonGraphError() {
        notFoundTest(this.restService::getJsonGraph);
    }

    @Test
    @Order(50)
    void estimateSizeSuccess() throws InterruptedException, ExecutionException {
        queryWithClientUrlSuccess(this.restService::estimateSize, "https://locallhost.com/");
    }

    @Test
    @Order(60)
    void discoverSizeTestSuccess(){
        var response = this.restService.getSize(taskId, DEFAULT_LANGUAGE);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        int size = Integer.parseInt(Objects.requireNonNull(
                Objects.requireNonNull(response.getBody()).getResult()).substring(0,4));
        Assertions.assertEquals(39, size / 100);

    }

    @Test
    void statusTaskNotFound() {
        notFoundTest(this.restService::statusTask);
    }

    private void notFoundTest(final FunctionTwoArgs<String, String, ResponseEntity<?>> restMethod){
        final var response = restMethod.apply("XXX-VVV-III", DEFAULT_LANGUAGE);
        ResponseDto responseDto = Objects.requireNonNull((ResponseDto) response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseDto.getStatusCode());
    }

    @Test
    void statusTaskRunning() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(false, null, false);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.RUNNING.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    @Test
    void statusTaskError() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(true, StatusTask.ERROR, false);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.ERROR.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    @Test
    void statusTaskDone() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(true, StatusTask.DONE, false);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.DONE.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    @Test
    void statusTaskUndefined() throws ExecutionException, InterruptedException {
        var response = statusTaskTest(true, StatusTask.DONE, true);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.UNDEFINED.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    private ResponseEntity<ResponseDto> statusTaskTest(final boolean isDone, final StatusTask statusTask, final boolean isThrowable)
            throws ExecutionException, InterruptedException {
        final String task = this.prepareMockTask(isDone, statusTask, isThrowable);
        return restService.statusTask(task, DEFAULT_LANGUAGE);
    }

    @SuppressWarnings("unchecked")
    private String prepareMockTask(final boolean isDone, final StatusTask statusTask, final boolean isThrowable)
            throws InterruptedException, ExecutionException {
        final String task = UUID.randomUUID().toString();
        Future<StatusTask> mockFuture = Mockito.mock(Future.class);
        Mockito.when(mockFuture.isDone()).thenReturn(isDone);
        if(isThrowable)
            Mockito.when(mockFuture.get()).thenThrow(ExecutionException.class);
        else Mockito.when(mockFuture.get()).thenReturn(statusTask);
        TaskRegistry.getRegistry().put(task, mockFuture);
        return task;
    }

    @Test
    void discoverSizeTestFileNotFound() throws ExecutionException, InterruptedException {
        final String task = this.prepareMockTask(true, StatusTask.DONE, false);
        var response = this.restService.getSize(task, DEFAULT_LANGUAGE);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(
                ErrorMessage.FILE_NOT_FOUND.getMessage(DEFAULT_LANGUAGE),
                Objects.requireNonNull(response.getBody()).getResult()
        );
    }

    @Test
    void estimateSizeError() {
        queryWithClientUrlError(this.restService::estimateSize);
    }

    private void queryWithClientUrlSuccess(final Function<String, ResponseEntity<ResponseDto>> rest, String successUrl)
            throws InterruptedException, ExecutionException {
        final var response = rest.apply(successUrl);
        taskId = Objects.requireNonNull(Objects.requireNonNull(response.getBody()).getResult());
        final var future = TaskRegistry.getRegistry().get(taskId);
        StatusTask statusTask = future.get();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(UUID.fromString(taskId).getClass(), UUID.class);
        Assertions.assertEquals(StatusTask.DONE, statusTask);
    }

    private void queryWithClientUrlError(final Function<String, ResponseEntity<?>> rest) {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> rest.apply("https://unreacheble-XXX-url.guru/"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
