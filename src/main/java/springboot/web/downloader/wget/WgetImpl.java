package springboot.web.downloader.wget;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class WgetImpl implements Wget {

    private final WgetOptions wgetOptions;

    public WgetImpl(WgetOptions wgetOptions) {
        this.wgetOptions = wgetOptions;
    }

    @Override
    public void download(String URI) throws InterruptedException, IOException {
        var process = new ProcessBuilder("sh", "-c", wgetOptions.getWget() + URI)
                .directory(new File("src/main/resources/sites")).start();

        String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(output);

        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
    }

}
