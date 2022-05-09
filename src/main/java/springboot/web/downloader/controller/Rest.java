package springboot.web.downloader.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
public class Rest {

    @GetMapping(path = "/upload", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getDocumentStatuses() {

        byte[] bytes = "test data-".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(bytes);
        Resource resource = new InputStreamResource(is);
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment;filename=export.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .body(resource);
    }
}
