package springboot.web.downloader.wget;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface Wget {
    void download(String URI) throws ExecutionException, InterruptedException, IOException;
}
