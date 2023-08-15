package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SiteMapDto implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 1L;
    Integer statusCode;
    private List<Node> nodes;
    private List<Edge> edges;
}
