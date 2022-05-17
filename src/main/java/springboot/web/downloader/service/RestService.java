package springboot.web.downloader.service;

import org.springframework.http.ResponseEntity;

/**
 * This class provides common rest interface for
 * clients request and is entry point for run
 * all base business functions
 */
public interface RestService {
    /**
     * This method run new thread which processed WGET and ZIP
     * steps and return {@code taskId} for monitoring download status
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<?> requireDownload(final String URI);

    /**
     * Method performs estimate web-resource size (only
     * static content available for wget processing)
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<?> estimateSize(final String URI);

    /**
     * This method pick up all links on site and build map of site
     * whether using {@code sitemap.xml} if exist
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<?> mapSite(final String URI);

    /**
     * This method allow download zip archive as octet-stream
     *
     * @param taskId string which is also .zip-file name
     * @return zip file as resource
     */
    ResponseEntity<?> getZip(final String taskId);
}
