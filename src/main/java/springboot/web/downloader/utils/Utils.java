package springboot.web.downloader.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import springboot.web.downloader.wget.WgetOptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This class provided utils methods for logging
 * and prepare environment before application work
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

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
        String output = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        log.info(output);
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
    public static int runProcess(String command, final String processName, final String workDir)
            throws IOException, InterruptedException {
        log.info("Command: " + command);
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
            return getResponse(URI);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /**
     * Helper method for in order to make remote server request
     * @param URI remote resource identifier
     * @return response entity as String
     */
    public static ResponseEntity<String> getResponse(final String URI) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
        headers.add("user-agent", WgetOptions.USER_AGENT);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(URI, HttpMethod.GET, entity, String.class);
    }

    /**
     * This method to define url "level"
     * level value equals count slash(/) symbols in url
     * @param url something url
     * @return level value
     */
    public static int calcUrlLevel(String url) {
        return (int) url.chars().filter(ch -> ch == '/').count();
    }
}
