package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Node implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 2L;
    String id;
    String type;
    String className;
    Data data;
    static XYPosition position = new XYPosition();
    @JsonIgnore
    static List<String> colors = Arrays.asList("!bg-yellow-400", "!bg-blue-400", "!bg-green-400", "!bg-pink-400",
            "!bg-sky-400", "!bg-red-400", "!bg-cyan-400", "!bg-orange-400", "!bg-emerald-400", "!bg-rose-400", "!bg-white-400");
    @JsonIgnore
    static Random random = new Random();
    @SuppressWarnings("unused")
    @lombok.Data
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    private static class XYPosition {
        int x = 0;
        int y = 0;
    }
    @lombok.Data
    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Data implements Serializable {
        @JsonIgnore
        private static final long serialVersionUID = 3L;
        String link;
        String label;
    }

    public static String getColorByLevel(int level) {
        if (colors.size() >= level) {
            return colors.get(level);
        }
        return colors.get(random.nextInt(11));
    }

    public static String getLabelByLevel(int level, String link) {
        String[] arr = link.split("/"); // example ['https:', '', 'java-course.ru', 'sitemap.xml']
        return String.join("/", Arrays.copyOfRange(arr, level + 2, arr.length));
    }
}
