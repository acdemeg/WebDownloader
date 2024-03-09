package springboot.web.downloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * Represent execution BUILD_MAP task result
 * Serializing using mapdb, not work with records !!!
 */
@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(makeFinal = true)
public class SiteMapDto extends ResponseDto implements Serializable {
    @Serial
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
