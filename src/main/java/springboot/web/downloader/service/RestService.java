package springboot.web.downloader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;

import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * This class provides common rest interface for
 * clients request and is entry point for run
 * all base business functions
 */
@Service
public class RestService {

    /**
     * This method run new thread which processed WGET and ZIP
     * steps and return {@code taskId} for monitoring download status
     * @param URI is root web-link which came from client
     * @return taskId
     */
    public ResponseEntity<?> requireDownload(final String URI) {

        String taskId = UUID.randomUUID().toString();
        var exec = Executors.newSingleThreadExecutor();
        var future = exec.submit(new WebTask(taskId, URI));
        TaskRegistry.registry.put(taskId, future);
        exec.shutdown();
        return ResponseEntity.ok().body(taskId);
    }
}
