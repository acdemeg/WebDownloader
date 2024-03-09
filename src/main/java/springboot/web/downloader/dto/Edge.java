package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Edge implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 4L;
    @SuppressWarnings({"unused", "java:S115"})
    private static final String type = "smoothstep";
    String id;
    String source;
    String target;
}
