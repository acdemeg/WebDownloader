package springboot.web.downloader.wget;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Here describe methods which take response for download
 * web-resources with help wget shell-utility
 */
public interface Wget {

    /**
     * This method perform run shell-utility 'wget'
     * with need number of parameters in order to
     * create local copy web-resource
     * @param URI root link web-resource
     * @param dir dest folder for download
     */
    void download(String URI, String dir) throws ExecutionException, InterruptedException, IOException;
}
