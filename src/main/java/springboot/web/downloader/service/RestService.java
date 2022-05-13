package springboot.web.downloader.service;

import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;

/**
 * This class provides common rest interface for
 * clients request and is entry point for run
 * all base business functions
 */
public interface RestService {
    /**
     * This method run new thread which processed WGET and ZIP
     * steps and return {@code taskId} for monitoring download status
     *
     * @param URI is root web-link which came from client
     * @return taskId
     */
    ResponseEntity<?> requireDownload(String URI);

    /**
     * This method allow download zip archive as octet-stream
     *
     * @param taskId string which is also .zip-file name
     * @return zip file as resource
     * @throws FileNotFoundException if zip file not found
     */
    ResponseEntity<?> getZip(String taskId) throws FileNotFoundException;
}
