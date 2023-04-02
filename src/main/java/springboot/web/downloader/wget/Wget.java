package springboot.web.downloader.wget;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Here describe methods which take response for download
 * and approximately size estimate of
 * web-resources with help wget shell-utility
 */
public interface Wget {

    /**
     * This method perform run shell-utility 'wget'
     * with need number of parameters in order to
     * create local copy web-resource
     *
     * @param URI root link web-resource
     * @param dir dest folder for download
     * @return status code shell-command
     */
    int download(final String URI, final String dir) throws ExecutionException, InterruptedException, IOException;

    /**
     * This method perform run shell-utility 'wget'
     * in spider mode for approximately estimate size web-resource
     * Method not create none local files and directories except wget-log
     *
     * @param URI root link web-resource
     * @return status code shell-command
     */
    int estimate(final String URI, final String dir) throws IOException, InterruptedException;

}
