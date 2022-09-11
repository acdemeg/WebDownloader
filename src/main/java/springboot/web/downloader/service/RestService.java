package springboot.web.downloader.service;

import org.springframework.http.ResponseEntity;
import springboot.web.downloader.dto.ResponseDto;

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
    ResponseEntity<ResponseDto> requireDownload(final String URI);

    /**
     * Method performs estimate web-resource size (only
     * static content available for wget processing)
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<ResponseDto> estimateSize(final String URI);

    /**
     * This method pick up all links on site and build map of site
     * whether using {@code sitemap.xml} if exist
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<ResponseDto> mapSite(final String URI);

    /**
     * This method allow download zip archive as octet-stream
     * @param taskId string which is also .zip-file name
     * @return zip file as resource
     */
    ResponseEntity<?> getZip(final String taskId);

    /**
     * This method allow monitoring status web-task
     * @param taskId task identifier received from client
     * @return status task (DONE, ERROR, RUNNING, UNDEFINED)
     */
    ResponseEntity<ResponseDto> statusTask(final String taskId);

    /**
     * This method allow get approximately size web-resource
     * if before '/estimate' success complete, else if '/estimate'
     * running then return {@code StatusTask}, else if
     * '/estimate' was error return error
     * @param taskId task identifier received from client
     * @return approximately size web-resource
     */
    ResponseEntity<ResponseDto> getSize(final String taskId);
}
