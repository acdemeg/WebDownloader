package springboot.web.downloader.registory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * This class represent registry running now tasks.
 * TaskRegistry based on {@code Map} interface.
 * Registry formed as --> Map<taskId:STRING, taskThread:FUTURE<STRING>>
 */
@Service
public final class TaskRegistry {
    public static final Map<String, Future<String>> registry = new HashMap<>();
}
