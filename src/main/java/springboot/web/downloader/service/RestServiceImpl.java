package springboot.web.downloader.service;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Service
public class RestServiceImpl implements RestService {

    @Override
    public ResponseEntity<?> requireDownload(final String URI) {
        String taskId = UUID.randomUUID().toString();
        final var exec = Executors.newSingleThreadExecutor();
        final var future = exec.submit(new WebTask(taskId, URI));
        TaskRegistry.registry.put(taskId, future);
        exec.shutdown();
        return ResponseEntity.ok().body(taskId);
    }

    @Override
    public ResponseEntity<?> getZip(String taskId) throws FileNotFoundException {
        var zip = new File(WebDownloader.baseArchived + taskId + ".zip");
        long length = FileUtils.sizeOf(zip);
        Resource resource = new InputStreamResource(new FileInputStream(zip));
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment;filename=site.zip")
                .contentType(MediaType.valueOf("application/zip"))
                .contentLength(length)
                .body(resource);
    }
}
