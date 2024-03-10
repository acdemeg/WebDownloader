package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Node implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 2L;
    @SuppressWarnings("unused")
    private static final XYPosition position = new XYPosition();
    @JsonIgnore
    static List<String> colors = Arrays.asList("!bg-yellow-400", "!bg-blue-400", "!bg-green-400", "!bg-pink-400",
            "!bg-sky-400", "!bg-red-400", "!bg-cyan-400", "!bg-orange-400", "!bg-emerald-400", "!bg-rose-400", "!bg-white-400");
    @JsonIgnore
    static Random random = new Random();
    String id;
    String type;
    String className;
    Data data;

    public Node(String id, String type, String className,
                Data data) {
        this.id = id;
        this.type = type;
        this.className = className;
        this.data = data;
    }

    @SuppressWarnings("unused")
    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static class XYPosition {
        int x = 0;
        int y = 0;
    }

    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static final class Data implements Serializable {
        @Serial
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
