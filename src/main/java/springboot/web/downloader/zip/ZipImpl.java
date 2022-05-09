package springboot.web.downloader.zip;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ZipImpl implements Zip {

    @Override
    public void wrapToZip(String source) throws IOException, InterruptedException {
        String dest = source + ".zip";
        String zip = "zip -r " + dest + " " + source;
        //todo 'archived' and 'sites' must be created
        String mv = " && mv " + dest + " archived";
        var process = new ProcessBuilder("sh", "-c", zip + mv)
                .directory(new File("src/main/resources/sites")).start();

        String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(output);

        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
    }
}
