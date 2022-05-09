package springboot.web.downloader.zip;

import java.io.IOException;

public interface Zip {

    void wrapToZip(String siteFolder) throws IOException, InterruptedException;
}
