package springboot.web.downloader.task;

import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.enums.TypeTask;
import springboot.web.downloader.utils.Utils;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * This class is abstract task created after client request
 * which must be processed in separate thread.
 * WebTask have {@code taskId} identifier thought it
 * client able to monitoring WebTask status.
 * WebTask is Prototype bean and creation new way in {@code Config.class}
 * {<code>
 * public FunctionTwoArgs<String, String, WebTask> webTaskFactory()
 * </code>}
 */
public class WebTask implements Callable<StatusTask> {

    private final Wget wget;
    private final Zip zip;
    private final String taskId;
    private final String uri;
    private final TypeTask typeTask;

    public WebTask(Wget wget, Zip zip, String taskId, String uri, TypeTask typeTask) {
        this.wget = wget;
        this.zip = zip;
        this.taskId = taskId;
        this.uri = uri;
        this.typeTask = typeTask;
    }

    @Override
    public StatusTask call() throws Exception {
        if (typeTask.equals(TypeTask.ESTIMATE)) {
            return estimateSize();
        } else return requireDownload();
    }

    private StatusTask requireDownload() throws IOException, ExecutionException, InterruptedException {
        int exitCode = 1;
        String dir = WebDownloader.BASE_SITES + taskId;
        Utils.createDirectory(dir);
        if (wget.download(uri, dir) == 0) {
            exitCode = zip.wrapToZip(taskId);
        }
        Utils.wgetLogging(dir);
        return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
    }

    private StatusTask estimateSize() throws IOException, InterruptedException {
        String dir = WebDownloader.BASE_SITES + taskId;
        Utils.createDirectory(dir);
        int exitCode = wget.estimate(uri, dir);
        return (exitCode == 0) ? StatusTask.DONE : StatusTask.ERROR;
    }
}
