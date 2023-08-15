package springboot.web.downloader.enums;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * This class represent node types
 */
@RequiredArgsConstructor
public enum NodeType {
    INPUT("input"),
    OUTPUT("output"),
    DEFAULT("default");

    private final String title;

    public static String getType(Map<String, Set<String>> urlGraph, int level, String url) {
        if (level == 0)
            return INPUT.title;
        return urlGraph.get(url).isEmpty() ? OUTPUT.title : DEFAULT.title;
    }

}
