package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(makeFinal = true)
public class SiteMapDto extends ResponseDto implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 1L;
    private List<Node> nodes;
    private List<Edge> edges;

    public SiteMapDto(List<Node> nodes, List<Edge> edges) {
        super(200, "Success");
        this.nodes = nodes;
        this.edges = edges;
    }
}
