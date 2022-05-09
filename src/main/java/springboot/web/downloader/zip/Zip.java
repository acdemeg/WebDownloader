package springboot.web.downloader.zip;

import java.io.IOException;

/**
 * Here describe methods which take responsible for compressing
 * data downloaded with help wget
 */
public interface Zip {

    void wrapToZip(String siteFolder) throws IOException, InterruptedException;
}
