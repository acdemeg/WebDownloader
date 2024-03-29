package springboot.web.downloader.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import springboot.web.downloader.dto.ResponseDto;
import springboot.web.downloader.enums.StatusTask;

import java.nio.file.NoSuchFileException;

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
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<ResponseDto> requireDownload(final String URI);

    /**
     * Method performs estimate web-resource size (only
     * static content available for wget processing)
     *
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<ResponseDto> estimateSize(final String URI);

    /**
     * This method pick up all links on site and build map of site
     * whether using {@code sitemap.xml} if exist
     *
     * @param URI is root web-link which came from client
     * @return taskId if URI is available else {@code ErrorStruct}
     */
    ResponseEntity<ResponseDto> mapSite(final String URI);

    /**
     * This method allow to get json graph which represent hierarchical site structure
     * if /map method completed successfully before
     * @param taskId task identifier received from client
     * @return json graph for UI render
     */
    ResponseEntity<ResponseDto> getJsonGraph(final String taskId, final String lang);

    /**
     * This method allow download zip archive as octet-stream
     *
     * @param fileName name of zip-archive file
     * @return zip file as resource
     */
    ResponseEntity<Resource> getZip(final String fileName) throws NoSuchFileException;

    /**
     * This method allow find zip archive file and if success return {@code fileName}
     *
     * @param taskId string which is also .zip-file name
     * @return zip fileName
     */
    ResponseEntity<ResponseDto> find(final String taskId, final String lang);

    /**
     * This method allow monitoring status web-task
     *
     * @param taskId task identifier received from client
     * @return status task {@link StatusTask}
     */
    ResponseEntity<ResponseDto> statusTask(final String taskId, final String lang);

    /**
     * This method allow get approximately size web-resource
     * if before '/estimate' success complete, else if '/estimate'
     * running then return {@code StatusTask}, else if
     * '/estimate' was error return error
     *
     * @param taskId task identifier received from client
     * @return approximately size web-resource
     */
    ResponseEntity<ResponseDto> getSize(final String taskId, final String lang);
}
