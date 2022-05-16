package springboot.web.downloader.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorStruct {
    private Integer errorCode;
    private String errorDescription;
}
