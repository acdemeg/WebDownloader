package springboot.web.downloader.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import springboot.web.downloader.WebDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class provided utils methods for logging
 * and prepare environment before application work
 */
@Slf4j
@Service
public class Utils {

    /**
     * Method perform creating folders for 'sites' and 'archived'
     * @throws IOException if occurs error in time creating folders
     *  or files with same name already exist
     */
    public static void prepareDirectories() throws IOException {
        FileUtils.forceMkdir(new File(WebDownloader.baseSites));
        FileUtils.forceMkdir(new File(WebDownloader.baseArchived));
        log.info("Create sites and archived directories if not exist");
    }

    /**
     * Common method which perform logging for standard output shell-command
     * @param process referred to running shell native process
     * @param processName conditional internal process name
     */
    public void logProcess(Process process, String processName) throws IOException {
        log.info(processName);
        log.info(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * Method extract log info from wget-log file and write it in application logs
     * @param dir to point on site location folder
     */
    public void wgetLogging(String dir) throws IOException {
        File file = new File(dir + "/wget-log");
        var lines = FileUtils.readLines(file, "UTF-8");
        log.info(String.join("\n", lines));
    }

    /**
     * Common method for creation new directory
     * @param dir path to new folder
     */
    public void createDirectory(String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }
}
