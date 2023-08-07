package springboot.web.downloader.registory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.web.downloader.enums.StatusTask;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * This class represent registry running now tasks, also result of it
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskRegistry {
    @Getter
    private static final Map<String, Future<StatusTask>> registry = new ConcurrentHashMap<>();
    @Getter
    private static final Map<String, Serializable> results = new ConcurrentHashMap<>();
}
