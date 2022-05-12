package springboot.web.downloader.zip;

import java.io.IOException;

/**
 * Here describe methods which take responsible
 * for compressing downloaded data
 */
public interface Zip {

    /**
     * This method perform run shell-utility 'zip' which to aim
     * creating .zip file and move .zip file in archive folder
     * @param siteFolder name site location folder
     */
    void wrapToZip(String siteFolder) throws IOException, InterruptedException;
}
