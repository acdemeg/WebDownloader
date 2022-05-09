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

    public void logProcess(Process process, String processName) throws IOException {
        log.info(processName);
        log.info(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
    }

    public void wgetLogging(String dir) throws IOException {
        File file = new File(dir + "/wget-log");
        var lines = FileUtils.readLines(file, "UTF-8");
        log.info(String.join("\n", lines));
    }

    public void createDirectory(String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }
}
