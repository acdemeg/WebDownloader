package springboot.web.downloader.wget;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Service
public class WgetImpl implements Wget {

    @Override
    public void download(String URI) throws ExecutionException, InterruptedException, IOException {
        var process = new ProcessBuilder("sh", "-c", "ls")
                .directory(new File(System.getProperty("user.home"))).start();
        var grubber = new StreamGrubber(process.getInputStream(), System.out::println);
        var exec = Executors.newSingleThreadExecutor();
        exec.submit(grubber).get();
        exec.shutdown();
    }
}
