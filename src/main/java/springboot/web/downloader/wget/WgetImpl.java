package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.web.downloader.utils.Utils;

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
    public int download(String URI, String dir) throws InterruptedException, IOException {
        String command = wgetOptions.getWget() + URI;
        return utils.runProcess(command, "WGET", dir);
    }

}
