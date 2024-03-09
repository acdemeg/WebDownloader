package springboot.web.downloader.registry;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.dto.Task;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

/**
 * This class represent running now tasks registry, also result of its execution.
 * Tasks that were in RUNNING status when the application was terminated will be restarted when the application starts.
 */

@Slf4j
@SuppressWarnings({"unchecked"})
public final class TaskRegistry {

    private final ConcurrentMap<String, Task> registry;

    private final ConcurrentMap<String, Serializable> results;

    private static TaskRegistry taskRegistry;

    @SneakyThrows
    private TaskRegistry() {
        Thread.sleep(1_000); // for volumes sync
        DB db = DBMaker.fileDB(WebDownloader.REGISTRY + "task_registry.db")
                .closeOnJvmShutdown().fileMmapEnable().make();
        registry = (ConcurrentMap<String, Task>) db.hashMap("registry").createOrOpen();
        results = (ConcurrentMap<String, Serializable>) db.hashMap("results").createOrOpen();
    }

    public static synchronized void create() {
        if (taskRegistry == null) {
            taskRegistry = new TaskRegistry();
        }
    }

    public static ConcurrentMap<String, Task> getRegistry() {
        return taskRegistry.registry;
    }

    public static ConcurrentMap<String, Serializable> getResults() {
        return taskRegistry.results;
    }
}
