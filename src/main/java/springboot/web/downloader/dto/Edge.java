package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serial;
import java.io.Serializable;


public record Edge(String id, String source, String target) implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 4L;
    @SuppressWarnings("unused")
    private static final String type = "smoothstep";
}
