package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class WgetImpl implements Wget {

    private final WgetOptions wgetOptions;
    private final Utils utils;

    @Autowired
    public WgetImpl(WgetOptions wgetOptions, Utils utils) {
        this.wgetOptions = wgetOptions;
        this.utils = utils;
    }

    @Override
    public void download(String URI, String dir) throws InterruptedException, IOException {
        var process = new ProcessBuilder("sh", "-c", wgetOptions.getWget() + URI)
                .directory(new File(dir)).start();
        utils.logProcess(process, "WGET_Output");
        log.info("Exit code: " + process.waitFor());
        utils.wgetLogging(dir);
    }

}
