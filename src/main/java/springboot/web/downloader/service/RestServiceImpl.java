package springboot.web.downloader.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.annotations.CheckUriConnection;
import springboot.web.downloader.dto.ResponseDto;
import springboot.web.downloader.dto.SiteMapDto;
import springboot.web.downloader.enums.ErrorMessage;
import springboot.web.downloader.enums.NativeProcessName;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.enums.TypeTask;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;
import springboot.web.downloader.utils.FunctionManyArgs;
import springboot.web.downloader.utils.ResponseUtils;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Service
public class RestServiceImpl implements RestService {

    private final FunctionManyArgs<TypeTask, String, WebTask> webTaskFactory;

    @Autowired
    public RestServiceImpl(FunctionManyArgs<TypeTask, String, WebTask> webTaskFactory) {
        this.webTaskFactory = webTaskFactory;
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<ResponseDto> requireDownload(final String URI) {
        return runWebTask(URI, TypeTask.DOWNLOAD);
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<ResponseDto> estimateSize(final String URI) {
        return runWebTask(URI, TypeTask.ESTIMATE);
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<ResponseDto> mapSite(final String URI) {
        return runWebTask(URI, TypeTask.BUILD_MAP);
    }

    @Override
    public ResponseEntity<ResponseDto> getJsonGraph(final String taskId, final String lang) {
        ResponseEntity<ResponseDto> res = getStatusTask(taskId, lang);
        if (!res.getStatusCode().is2xxSuccessful()
                || !Objects.equals(Objects.requireNonNull(res.getBody()).getResult(), StatusTask.DONE.getStatus(lang)))
            return res;

        Serializable siteMap = TaskRegistry.getResults().get(taskId);
        return ResponseUtils.ok((SiteMapDto) siteMap);
    }

    @Override
    public ResponseEntity<ResponseDto> statusTask(final String taskId, final String lang) {
        try {
            var future = TaskRegistry.getRegistry().get(taskId);
            if (Objects.isNull(future))
                return ResponseUtils.notFound(ErrorMessage.TASK_NOT_FOUND.getMessage(lang));
            if (!future.isDone())
                return ResponseUtils.ok(StatusTask.RUNNING.getStatus(lang));
            var result = future.get();
            if (result.equals(StatusTask.ERROR))
                return ResponseUtils.internalServerError(StatusTask.ERROR.getStatus(lang));
            return ResponseUtils.ok(StatusTask.DONE.getStatus(lang));
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
            return ResponseUtils.internalServerError(StatusTask.UNDEFINED.getStatus(lang));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> find(final String taskId, final String lang) {
        ResponseEntity<ResponseDto> res = getStatusTask(taskId, lang);
        if (!res.getStatusCode().is2xxSuccessful()
                || !Objects.equals(Objects.requireNonNull(res.getBody()).getResult(), StatusTask.DONE.getStatus(lang)))
            return res;
        String path = WebDownloader.ARCHIVED + taskId + ".zip";
        File zip = new File(path);
        if (zip.exists() && zip.isFile() && zip.canRead()) {
            return ResponseUtils.ok(path);
        }
        return ResponseUtils.notFound(ErrorMessage.FILE_NOT_FOUND.getMessage(lang));
    }

    @Override
    public ResponseEntity<Resource> getZip(final String fileName) throws NoSuchFileException {
        File zip = new File(fileName);
        if(!zip.exists()) {
            throw new NoSuchFileException(fileName);
        }
        Path zipPath = Paths.get(zip.getAbsolutePath());
        Resource resource = new FileSystemResource(zipPath);
        long length = FileUtils.sizeOf(zip);
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment;filename=site.zip")
                .contentType(MediaType.valueOf("application/zip"))
                .contentLength(length)
                .body(resource);
    }

    @Override
    public ResponseEntity<ResponseDto> getSize(final String taskId, final String lang) {
        try {
            ResponseEntity<ResponseDto> res = getStatusTask(taskId, lang);
            if (!res.getStatusCode().is2xxSuccessful()
                    || !Objects.equals(Objects.requireNonNull(res.getBody()).getResult(), StatusTask.DONE.getStatus(lang)))
                return res;
            String wgetLog = WebDownloader.SITES + taskId + "/wget-log";
            if (Files.notExists(Path.of(wgetLog)))
                return ResponseUtils.notFound(ErrorMessage.FILE_NOT_FOUND.getMessage(lang));

            int exitCode = Utils.runProcess(Utils.DISCOVER_SIZE_SCRIPT.getAbsolutePath() + " " + wgetLog,
                    NativeProcessName.DISCOVER_SIZE, WebDownloader.SITES + taskId);
            if (exitCode != 0)
                return ResponseUtils.internalServerError(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage(lang));

            var list = FileUtils.readLines(new File(wgetLog), StandardCharsets.UTF_8);
            String byteSize = list.getLast();
            NumberFormat likesShort = NumberFormat.getCompactNumberInstance(
                    Locale.of("en", "US"), NumberFormat.Style.SHORT);
            likesShort.setMaximumFractionDigits(2);
            String size = likesShort.format(Double.valueOf(byteSize.replace(",", ".")));
            return ResponseUtils.ok(size);

        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            return ResponseUtils.internalServerError(ex.getMessage());
        }
    }

    private ResponseEntity<ResponseDto> getStatusTask(final String taskId, final String lang) {
        ResponseEntity<ResponseDto> response = this.statusTask(taskId, lang);
        String statusTask = Objects.requireNonNull(response.getBody()).getResult();
        if (Objects.equals(statusTask, StatusTask.RUNNING.getStatus(lang))
                || Objects.equals(statusTask, StatusTask.UNDEFINED.getStatus(lang))
                || Objects.equals(statusTask, StatusTask.ERROR.getStatus(lang))) {
            return ResponseUtils.preconditionFailed(ErrorMessage.PRECONDITION_FAILED.getMessage(lang));
        }
        return response;
    }

    private ResponseEntity<ResponseDto> runWebTask(final String URI, final TypeTask typeTask) {
        String taskId = UUID.randomUUID().toString();
        try(final var exec = Executors.newVirtualThreadPerTaskExecutor()) {
            final var future = exec.submit(webTaskFactory.apply(taskId, URI, typeTask));
            TaskRegistry.getRegistry().put(taskId, future);
            exec.shutdown();
            return ResponseUtils.ok(taskId);
        }
    }
}
