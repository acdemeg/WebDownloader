package springboot.web.downloader.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.annotations.CheckUriConnection;
import springboot.web.downloader.dto.ResponseDto;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.enums.TypeTask;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;
import springboot.web.downloader.utils.FunctionManyArgs;
import springboot.web.downloader.utils.ResponseUtils;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String taskId = UUID.randomUUID().toString();
        return ResponseUtils.ok(taskId);
    }
    
    @Override
    public ResponseEntity<ResponseDto> statusTask(final String taskId, final String lang) {
        try {
            var future = TaskRegistry.registry.get(taskId);
            if(Objects.isNull(future))
                return ResponseUtils.notFound("Task with id: " + taskId + " not found");
            if(!future.isDone())
                return ResponseUtils.ok(StatusTask.RUNNING.getStatus(lang));
            var result = future.get();
            if(result.equals(StatusTask.ERROR))
                return ResponseUtils.internalServerError(StatusTask.ERROR.getStatus(lang));
            return ResponseUtils.ok(StatusTask.DONE.getStatus(lang));
        }
        catch (Exception ex){
            return ResponseUtils.internalServerError(StatusTask.UNDEFINED.getStatus(lang));
        }
    }

    @Override
    public ResponseEntity<?> getZip(final String taskId, final String lang) {
        try {
            ResponseEntity<ResponseDto> res = getStatusTask(taskId, lang);
            if (!res.getStatusCode().is2xxSuccessful())
                return res;

            ResponseEntity<ResponseDto> response = this.statusTask(taskId, lang);
            if(!Objects.equals(Objects.requireNonNull(response.getBody()).getResult(), StatusTask.DONE.getStatus(lang)))
                return response;

            String path = WebDownloader.baseArchived + taskId + ".zip";
            var zip = new File(path);
            Path zipPath = Paths.get(zip.getAbsolutePath());
            Resource resource = new ByteArrayResource(Files.readAllBytes(zipPath));
            long length = FileUtils.sizeOf(zip);
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment;filename=site.zip")
                    .contentType(MediaType.valueOf("application/zip"))
                    .contentLength(length)
                    .body(resource);
        }
        catch (IOException ex){
            return ResponseUtils.notFound("Not found zip-file for taskId: " + taskId);
        }
    }
    
    @Override
    public ResponseEntity<ResponseDto> getSize(final String taskId, final String lang) {
        try {
            ResponseEntity<ResponseDto>  res = getStatusTask(taskId, lang);
            if (!res.getStatusCode().is2xxSuccessful())
                return res;

            String wgetLog = WebDownloader.baseSites + taskId + "/wget-log";
            if(Files.notExists(Path.of(wgetLog)))
                return ResponseUtils.notFound("File " + taskId + " not found");

            Path sh = Paths.get("./src/main/resources/discover-size.sh").toAbsolutePath();
            int exitCode = Utils.runProcess(
                    sh + " " + wgetLog,
                    "DISCOVER_SIZE", WebDownloader.baseSites + taskId);
            if(exitCode != 0)
                return ResponseUtils.internalServerError("Exit code: " + exitCode);

            var list = FileUtils.readLines(new File(wgetLog), StandardCharsets.UTF_8);
            String byteSize = list.get(list.size() - 1);
            return ResponseUtils.ok(byteSize);

        } catch (IOException | InterruptedException ex) {
            return ResponseUtils.internalServerError(ex.getMessage());
        }
    }


    private ResponseEntity<ResponseDto> getStatusTask(final String taskId, final String lang){
        ResponseEntity<ResponseDto> response = this.statusTask(taskId, lang);
        String statusTask =  Objects.requireNonNull(response.getBody()).getResult();
        if(Objects.equals(statusTask, StatusTask.RUNNING.getStatus(lang))
                || Objects.equals(statusTask, StatusTask.UNDEFINED.getStatus(lang))
                || Objects.equals(statusTask, StatusTask.ERROR.getStatus(lang))){
            return ResponseUtils.preconditionFailed("Task with id: " + taskId + " have status " + statusTask);
        }
        return response;
    }

    private ResponseEntity<ResponseDto> runWebTask(final String URI, final TypeTask estimate) {
        String taskId = UUID.randomUUID().toString();
        final var exec = Executors.newSingleThreadExecutor();
        final var future = exec.submit(webTaskFactory.apply(taskId, URI, estimate));
        TaskRegistry.registry.put(taskId, future);
        exec.shutdown();
        return ResponseUtils.ok(taskId);
    }
}
