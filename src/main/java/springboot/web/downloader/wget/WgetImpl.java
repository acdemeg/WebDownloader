package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class WgetImpl implements Wget {

    private final WgetOptions wgetOptions;

    public WgetImpl(WgetOptions wgetOptions) {
        this.wgetOptions = wgetOptions;
    }

    @Override
    public void download(String URI, String dir) throws InterruptedException, IOException {
        var process = new ProcessBuilder("sh", "-c", wgetOptions.getWget() + URI)
                .directory(new File(dir)).start();
        var utils = Utils.factory();
        utils.logProcess(process, "WGET_Output");
        utils.wgetLogging(dir);
        log.info("Exit code: " + process.waitFor());
    }

}
