package springboot.web.downloader.wget;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Here describe methods which take response for download
 * web-resources with help wget
 */
public interface Wget {

    void download(String URI, String dir) throws ExecutionException, InterruptedException, IOException;
}
