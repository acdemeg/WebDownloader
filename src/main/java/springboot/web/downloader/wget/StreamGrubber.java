package springboot.web.downloader.wget;

import java.io.*;
import java.util.function.Consumer;

public class StreamGrubber implements Runnable {

    private final InputStream inputStream;
    private final Consumer<String> consumer;

    public StreamGrubber(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
                .forEach(consumer);
    }
}