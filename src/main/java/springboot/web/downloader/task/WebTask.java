package springboot.web.downloader.task;

import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.utils.Utils;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import java.util.concurrent.Callable;

public class WebTask implements Callable<String> {

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
    public String call() throws Exception {
        String dir = WebDownloader.baseSites + taskId;
        utils.createDirectory(dir);
        wget.download(URI, dir);
        zip.wrapToZip(taskId);
        return "Done";
    }
}
