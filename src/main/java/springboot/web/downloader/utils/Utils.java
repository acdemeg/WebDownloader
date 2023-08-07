package springboot.web.downloader.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import springboot.web.downloader.WebDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class provided utils methods for logging
 * and prepare environment before application work
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    /**
     * Method perform creating folders for 'sites' and 'archived'
     *
     * @throws IOException if occurs error in time creating folders
     *                     or files with same name already exist
     */
    public static void prepareDirectories() throws IOException {
        FileUtils.forceMkdir(new File(WebDownloader.BASE_SITES));
        FileUtils.forceMkdir(new File(WebDownloader.BASE_ARCHIVED));
        log.info("Create sites and archived directories if not exist");
    }

    /**
     * Common method which perform logging for standard output shell-command
     *
     * @param process     referred to running shell native process
     * @param processName conditional internal process name
     */
    public static void logProcess(final Process process, final String processName) throws IOException {
        log.info(processName);
        log.info(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * Method extract log-info from wget-log file and write it in application logs
     *
     * @param dir to point on site location folder
     */
    public static void wgetLogging(final String dir) throws IOException {
        File file = new File(dir + "/wget-log");
        final var lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        log.info(String.join("\n", lines));
    }

    /**
     * Common method for creation new directory
     *
     * @param dir path to new folder
     */
    public static void createDirectory(final String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }

    /**
     * Common method for run any native process
     *
     * @param command     shell executed command with params
     * @param processName name process which run
     * @param workDir     working directory for run process
     * @return exit code shell-utility
     */
    public static int runProcess(final String command, final String processName, final String workDir)
            throws IOException, InterruptedException {
        final var process = new ProcessBuilder("sh", "-c", command)
                .directory(new File(workDir)).start();
        Utils.logProcess(process, processName + "_Output");
        int exitCode = process.waitFor();
        log.info("Exit code: " + exitCode);
        return exitCode;
    }

    /**
     * This method performs check of availability remote web-resource
     *
     * @param URI remote resource identifier
     * @return test connection info
     */
    public static ResponseEntity<String> isLiveConnection(final String URI) {
        try {
            return new RestTemplate().getForEntity(URI, String.class);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
