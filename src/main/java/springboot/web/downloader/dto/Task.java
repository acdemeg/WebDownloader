package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import springboot.web.downloader.enums.StatusTask;
import springboot.web.downloader.enums.TypeTask;

import java.io.Serial;
import java.io.Serializable;

/**
 * User task with query parameters for mapdb serialization
 * mapdb not work with records !!!
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class Task implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 6L;
    final String taskId;
    final Params params;
    StatusTask statusTask;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Params implements Serializable {
        @Serial
        @JsonIgnore
        private static final long serialVersionUID = 7L;
        String uri;
        TypeTask typeTask;
    }
}

