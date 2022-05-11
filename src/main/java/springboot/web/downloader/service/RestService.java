package springboot.web.downloader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.utils.Utils;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class RestService {

    private final Wget wget;
    private final Utils utils;
    private final Zip zip;

    @Autowired
    public RestService(Wget wget, Utils utils, Zip zip) {
        this.wget = wget;
        this.utils = utils;
        this.zip = zip;
    }

    public ResponseEntity<?> requireDownload(final String URI, String destFolder)
            throws IOException, ExecutionException, InterruptedException {
        String dir = WebDownloader.baseSites + destFolder;
        utils.createDirectory(dir);
        wget.download(URI, dir);
        zip.wrapToZip(destFolder);

        return ResponseEntity.ok()
                .body("");
    }
}
