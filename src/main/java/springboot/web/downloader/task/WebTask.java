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

    private final Wget wget = WebDownloader.appContext.getBean(Wget.class);
    private final Utils utils = WebDownloader.appContext.getBean(Utils.class);
    private final Zip zip = WebDownloader.appContext.getBean(Zip.class);
    private final String taskId;
    private final String URI;

    public WebTask(String taskId, String uri) {
        this.taskId = taskId;
        URI = uri;
    }

    @Override
    public StatusTask call() throws Exception {
        int exitCode = 1;
        String dir = WebDownloader.baseSites + taskId;
        utils.createDirectory(dir);
        if(wget.download(URI, dir) == 0){
            exitCode = zip.wrapToZip(taskId);
        }
        utils.wgetLogging(dir);
        return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
    }
}
