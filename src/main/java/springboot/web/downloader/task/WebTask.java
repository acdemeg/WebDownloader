package springboot.web.downloader.task;

import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.utils.Utils;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import java.util.concurrent.Callable;

/**
 * This class is abstract task created after client request
 * which must be processed in separate thread.
 * WebTask have {@code taskId} identifier thought it
 * client able to monitoring WebTask status
 */
public class WebTask implements Callable<StatusTask> {

    private final Wget wget;
    private final Zip zip;
    private final String taskId;
    private final String URI;

    public WebTask(Wget wget, Zip zip, String taskId, String URI) {
        this.wget = wget;
        this.zip = zip;
        this.taskId = taskId;
        this.URI = URI;
    }

    @Override
    public StatusTask call() throws Exception {
        int exitCode = 1;
        String dir = WebDownloader.baseSites + taskId;
        Utils.createDirectory(dir);
        if(wget.download(URI, dir) == 0){
            exitCode = zip.wrapToZip(taskId);
        }
        Utils.wgetLogging(dir);
        return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
    }
}
