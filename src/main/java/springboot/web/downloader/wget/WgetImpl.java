package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.web.downloader.enums.NativeProcessName;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;

@Slf4j
@Service
public class WgetImpl implements Wget {

    private final WgetOptions wgetOptions;

    @Autowired
    public WgetImpl(WgetOptions wgetOptions) {
        this.wgetOptions = wgetOptions;
    }

    @Override
    public int download(final String URI, final String dir) throws InterruptedException, IOException {
        String command = wgetOptions.getWgetDownload() + URI;
        return Utils.runProcess(command, NativeProcessName.WGET_DOWNLOAD.name(), dir);
    }

    @Override
    public int estimate(final String URI, final String dir) throws IOException, InterruptedException {
        String command = wgetOptions.getWgetEstimate() + URI;
        return Utils.runProcess(command, NativeProcessName.WGET_ESTIMATE.name(), dir);
    }

}
