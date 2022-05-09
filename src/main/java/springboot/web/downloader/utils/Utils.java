package springboot.web.downloader.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Utils {

    public static Utils factory(){
        return new Utils();
    }

    public void logProcess(Process process, String processName) throws IOException, InterruptedException {
        log.info(processName);
        log.info(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
        int exitCode = process.waitFor();
        log.info("Exit code: " + exitCode);
    }

    public void createDirectory(String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }
}
