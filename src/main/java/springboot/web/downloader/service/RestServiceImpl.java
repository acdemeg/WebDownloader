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
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;
import springboot.web.downloader.utils.FunctionTwoArgs;
import springboot.web.downloader.utils.ResponseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Service
public class RestServiceImpl implements RestService {

    private final FunctionTwoArgs<String, String, WebTask> webTaskFactory;

    @Autowired
    public RestServiceImpl(FunctionTwoArgs<String, String, WebTask> webTaskFactory) {
        this.webTaskFactory = webTaskFactory;
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<?> requireDownload(final String URI) {
        String taskId = UUID.randomUUID().toString();
        final var exec = Executors.newSingleThreadExecutor();
        final var future = exec.submit(webTaskFactory.apply(taskId, URI));
        TaskRegistry.registry.put(taskId, future);
        exec.shutdown();
        return ResponseEntity.ok().body(taskId);
    }

    @Override
    @CheckUriConnection
    public ResponseEntity<?> estimateSize(final String URI) {
        String taskId = UUID.randomUUID().toString();
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
}
