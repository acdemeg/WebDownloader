package springboot.web.downloader.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import springboot.web.downloader.dto.ResponseDto;
import springboot.web.downloader.dto.SiteMapDto;
import springboot.web.downloader.dto.Task;
import springboot.web.downloader.enums.ErrorMessage;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.registry.TaskRegistry;
import springboot.web.downloader.utils.FunctionTwoArgs;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static springboot.web.downloader.WebDownloader.DEFAULT_LANGUAGE;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestServiceTest {

    private final RestService restService;
    private static String taskId;

    static {
        Utils.prepareEnv();
    }

    @Autowired
    RestServiceTest(final RestService restService) {
        this.restService = restService;
    }

    @AfterAll
    static void discardEnvTest() throws IOException {
        Utils.discardEnv();
    }

    @Test
    @Order(10)
    void requireDownloadSuccess() {
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
    void buildMapSiteSuccessOne() {
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
    void buildMapSiteSuccessTwo() {
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
    void estimateSizeSuccess() {
        queryWithClientUrlSuccess(this.restService::estimateSize, "https://locallhost.com/");
    }

    @Test
    @Order(60)
    void discoverSizeTestSuccess(){
        var response = this.restService.getSize(taskId, DEFAULT_LANGUAGE);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("3.92K", Objects.requireNonNull(response.getBody()).getResult());

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
    void statusTaskRunning() {
        var response = statusTaskTest(StatusTask.RUNNING);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.RUNNING.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    @Test
    void statusTaskError() {
        var response = statusTaskTest(StatusTask.ERROR);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.ERROR.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    @Test
    void statusTaskDone() {
        var response = statusTaskTest(StatusTask.DONE);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(StatusTask.DONE.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    @Test
    void statusTaskUndefined() {
        var response = statusTaskTest(StatusTask.UNDEFINED);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals(StatusTask.UNDEFINED.name(), Objects.requireNonNull(response.getBody()).getResult());
    }

    private ResponseEntity<ResponseDto> statusTaskTest(final StatusTask statusTask) {
        final String task = this.prepareMockTask(statusTask);
        return restService.statusTask(task, DEFAULT_LANGUAGE);
    }

    private String prepareMockTask(final StatusTask statusTask) {
        final String taskId = UUID.randomUUID().toString();
        Task mockTask = new Task(taskId, null).setStatusTask(statusTask);
        TaskRegistry.getRegistry().put(taskId, mockTask);
        return taskId;
    }

    @Test
    void discoverSizeTestFileNotFound() {
        final String task = this.prepareMockTask(StatusTask.DONE);
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

    private void queryWithClientUrlSuccess(final Function<String, ResponseEntity<ResponseDto>> rest, String successUrl) {
        var response = rest.apply(successUrl);
        taskId = Objects.requireNonNull(Objects.requireNonNull(response.getBody()).getResult());
        Task task = TaskRegistry.getRegistry().get(taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(UUID.fromString(taskId).getClass(), UUID.class);
        Assertions.assertEquals(StatusTask.DONE, task.getStatusTask());
    }

    private void queryWithClientUrlError(final Function<String, ResponseEntity<?>> rest) {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> rest.apply("https://unreacheble-XXX-url.guru/"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
