package springboot.web.downloader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteMapDto {
    private Integer statusCode;
    private List<Object> nodes;
    private List<Object> edges;
}
