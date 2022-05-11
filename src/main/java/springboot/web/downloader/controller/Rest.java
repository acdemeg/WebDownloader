package springboot.web.downloader.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.service.RestService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@RestController
public class Rest {

    private final RestService restService;

    public Rest(RestService restService) {
        this.restService = restService;
    }

    @GetMapping(path = "/zip", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getZip() throws IOException {

        var zip = new File(WebDownloader.baseArchived + "small.zip");
        long length = FileUtils.sizeOf(zip);
        Resource resource = new InputStreamResource(
                new FileInputStream(zip));

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment;filename=site.zip")
                .contentType(MediaType.valueOf("application/zip"))
                .contentLength(length)
                .body(resource);
    }

    @GetMapping(path = "/require", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requireDownload(@RequestParam(value = "uri") final String URI) {
        return this.restService.requireDownload(URI);
    }

    @GetMapping(path = "/estimate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> estimateSize(){
        return ResponseEntity.ok()
                .body(null);
    }

    @GetMapping(path = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> mapSite(){
        return ResponseEntity.ok()
                .body(null);
    }

}
