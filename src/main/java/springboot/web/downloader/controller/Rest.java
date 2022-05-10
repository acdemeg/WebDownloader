package springboot.web.downloader.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.web.downloader.WebDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
public class Rest {

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
    public ResponseEntity<?> requireDownload(){
        return ResponseEntity.ok()
                .body(null);
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
