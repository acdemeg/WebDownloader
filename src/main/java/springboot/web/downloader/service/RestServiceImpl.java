package springboot.web.downloader.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.annotations.CheckUriConnection;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.enums.TypeTask;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;
import springboot.web.downloader.utils.FunctionManyArgs;
import springboot.web.downloader.utils.ResponseUtils;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    public ResponseEntity<?> requireDownload(final String URI) {
        return runWebTask(URI, TypeTask.DOWNLOAD);
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<?> estimateSize(final String URI) {
        return runWebTask(URI, TypeTask.ESTIMATE);
    }

    private ResponseEntity<String> runWebTask(String URI, TypeTask estimate) {
        String taskId = UUID.randomUUID().toString();
        final var exec = Executors.newSingleThreadExecutor();
        final var future = exec.submit(webTaskFactory.apply(taskId, URI, estimate));
        TaskRegistry.registry.put(taskId, future);
        exec.shutdown();
        return ResponseEntity.ok().body(taskId);
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<?> mapSite(final String URI) {
        String taskId = UUID.randomUUID().toString();
        return ResponseEntity.ok().body(taskId);
    }

    @Override
    public ResponseEntity<?> getZip(final String taskId) {
        try {
            String path = WebDownloader.baseArchived + taskId + ".zip";
            var zip = new File(path);
            Resource resource = new InputStreamResource(new FileInputStream(zip));
            long length = FileUtils.sizeOf(zip);
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment;filename=site.zip")
                    .contentType(MediaType.valueOf("application/zip"))
                    .contentLength(length)
                    .body(resource);
        }
        catch (FileNotFoundException ex){
            return ResponseUtils.notFound("Not found zip-file for taskId: " + taskId);
        }
    }

    @Override
    public ResponseEntity<?> statusTask(final String taskId) {
        try {
            var future = TaskRegistry.registry.get(taskId);
            if(Objects.isNull(future))
                return ResponseUtils.notFound("Task with id: " + taskId + " not found");
            if(!future.isDone())
                return ResponseEntity.ok().body(StatusTask.RUNNING);
            var result = future.get();
            if(result.equals(StatusTask.ERROR))
                return ResponseEntity.internalServerError().body(StatusTask.ERROR);
            return ResponseEntity.ok().body(StatusTask.DONE);
        }
        catch (Exception ex){
            return ResponseEntity.internalServerError().body(StatusTask.UNDEFINED);
        }
    }

    @Override
    public ResponseEntity<?> discoverSize(String taskId) {
        try {
            ResponseEntity<?> response = this.statusTask(taskId);
            if(!Objects.equals(response.getBody(), StatusTask.DONE))
                return response;

            String wgetLog = WebDownloader.baseSites + taskId + "/wget-log";
            if(Files.notExists(Path.of(wgetLog)))
                return ResponseEntity.internalServerError().body("File " + taskId + " not found");

            Path sh = Paths.get("./src/main/resources/discover-size.sh").toAbsolutePath();
            int exitCode = Utils.runProcess(
                    sh + " " + wgetLog,
                    "DISCOVER_SIZE", WebDownloader.baseSites + taskId);
            if(exitCode != 0)
                return ResponseEntity.internalServerError().body("Exit code: " + exitCode);

            var list = FileUtils.readLines(new File(wgetLog), StandardCharsets.UTF_8);
            String byteSize = list.get(list.size() - 1);
            return ResponseEntity.ok().body(byteSize);

        } catch (IOException | InterruptedException ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
