package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

    @Override
    public String getSiteMap(final String URI) {
        ResponseEntity<String> entity = new RestTemplate().getForEntity(URI, String.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        }
        return ""; // TODO https://www.lostsaloon.com/technology/how-to-create-an-xml-sitemap-using-wget-and-shell-script/
    }

}
