package springboot.web.downloader.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import springboot.web.downloader.WebDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class provided utils methods for logging
 * and prepare environment before application work
 */
@Slf4j
public final class Utils {

    /**
     * Method perform creating folders for 'sites' and 'archived'
     * @throws IOException if occurs error in time creating folders
     * or files with same name already exist
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
    public static void logProcess(Process process, String processName) throws IOException {
        log.info(processName);
        log.info(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * Method extract log-info from wget-log file and write it in application logs
     * @param dir to point on site location folder
     */
    public static void wgetLogging(String dir) throws IOException {
        File file = new File(dir + "/wget-log");
        final var lines = FileUtils.readLines(file, "UTF-8");
        log.info(String.join("\n", lines));
    }

    /**
     * Common method for creation new directory
     * @param dir path to new folder
     */
    public static void createDirectory(String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }

    /**
     * Common method for run any native process
     * @param command shell executed command with params
     * @param processName name process which run
     * @param workDir working directory for run process
     * @return exit code shell-utility
     */
    public static int runProcess(String command, String processName, String workDir) throws IOException, InterruptedException {
        final var process = new ProcessBuilder("sh", "-c", command)
                .directory(new File(workDir)).start();
        Utils.logProcess(process, processName + "_Output");
        int exitCode = process.waitFor();
        log.info("Exit code: " + exitCode);
        return exitCode;
    }

    /**
     * This method performs check of availability remote web-resource
     * @param URI remote resource identifier
     * @return test connection info
     */
    public static ResponseEntity<?> isLiveConnection(final String URI){
        try {
            return new RestTemplate().getForEntity(URI, String.class);
        }
        catch (Exception ex){
            return ResponseUtils.badRequest(ex);
        }
    }
}
