package springboot.web.downloader.zip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.utils.Utils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class ZipImpl implements Zip {

    @Override
    public void wrapToZip(String source) throws IOException, InterruptedException {
        String dest = source + ".zip";
        String zip = "zip -9 -r " + dest + " " + source;
        String mv = " && mv " + dest + " ../archived";
        var process = new ProcessBuilder("sh", "-c", zip + mv)
                .directory(new File(WebDownloader.baseDir)).start();
        Utils.factory().logProcess(process, "ZIP_Output");
    }
}
