package springboot.web.downloader.registory;

import springboot.web.downloader.enums.StatusTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * This class represent registry running now tasks.
 * TaskRegistry based on {@code Map} interface.
 * Registry formed as --> Map<taskId:STRING, taskThread:FUTURE<STRING>>
 */
public final class TaskRegistry {

    private TaskRegistry() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, Future<StatusTask>> registry = new ConcurrentHashMap<>();

    public static Map<String, Future<StatusTask>> getRegistry(){
        return registry;
    }
}
