package springboot.web.downloader.zip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;

@Slf4j
@Service
public class ZipImpl implements Zip {

    private final Utils utils;

    @Autowired
    public ZipImpl(Utils utils) {
        this.utils = utils;
    }

    @Override
    public int wrapToZip(String siteFolder) throws IOException, InterruptedException {
        String siteZip = siteFolder + ".zip";
        String zip = "zip -9 -r " + siteZip + " " + siteFolder;
        String mv = " && mv " + siteZip + " ../archived";
        return utils.runProcess(zip + mv, "ZIP", WebDownloader.baseSites);
    }

}
