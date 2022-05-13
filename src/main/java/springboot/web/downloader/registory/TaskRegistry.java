package springboot.web.downloader.registory;

import springboot.web.downloader.task.StatusTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * This class represent registry running now tasks.
 * TaskRegistry based on {@code Map} interface.
 * Registry formed as --> Map<taskId:STRING, taskThread:FUTURE<STRING>>
 */
public final class TaskRegistry {
    public static final Map<String, Future<StatusTask>> registry = new HashMap<>();
}
