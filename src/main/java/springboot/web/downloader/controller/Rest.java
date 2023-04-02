package springboot.web.downloader.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.web.downloader.dto.ResponseDto;
import springboot.web.downloader.service.RestService;

import java.nio.file.NoSuchFileException;

@Slf4j
@RestController
public class Rest {

    private final RestService restService;

    public Rest(final RestService restService) {
        this.restService = restService;
    }

    @GetMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> find(
            @RequestParam(value = "taskId") final String taskId,
            @RequestParam(value = "lang", defaultValue = "Eng", required = false) final String lang) {
        return this.restService.find(taskId, lang);
    }

    @GetMapping(path = "/zip", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getZip(@RequestParam(value = "fileName") final String fileName) throws NoSuchFileException {
        return this.restService.getZip(fileName);
    }

    @GetMapping(path = "/size", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> discoverSize(
            @RequestParam(value = "taskId") final String taskId,
            @RequestParam(value = "lang", defaultValue = "Eng", required = false) final String lang) {
        return this.restService.getSize(taskId, lang);
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> statusTask(
            @RequestParam(value = "taskId") final String taskId,
            @RequestParam(value = "lang", defaultValue = "Eng", required = false) final String lang) {
        return this.restService.statusTask(taskId, lang);
    }

    @GetMapping(path = "/require", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> requireDownload(@RequestParam(value = "uri") final String URI) {
        return this.restService.requireDownload(URI);
    }

    @GetMapping(path = "/estimate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> estimateSize(@RequestParam(value = "uri") final String URI) {
        return this.restService.estimateSize(URI);
    }

    @GetMapping(path = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> mapSite(@RequestParam(value = "uri") final String URI) {
        return this.restService.mapSite(URI);
    }

}
