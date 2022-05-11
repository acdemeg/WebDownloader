package springboot.web.downloader.registory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class TaskRegistry {

    public static final Map<String, Future<String>> registry = new HashMap<>();
}
