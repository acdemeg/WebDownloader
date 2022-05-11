package springboot.web.downloader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.registory.TaskRegistry;
import springboot.web.downloader.task.WebTask;

import java.util.UUID;
import java.util.concurrent.Executors;

@Service
public class RestService {

    public ResponseEntity<?> requireDownload(final String URI) {

        String taskId = UUID.randomUUID().toString();
        var exec = Executors.newSingleThreadExecutor();
        var future = exec.submit(new WebTask(taskId, URI));
        TaskRegistry.registry.put(taskId, future);
        exec.shutdown();
        return ResponseEntity.ok().body("ok");
    }
}
