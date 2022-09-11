package springboot.web.downloader.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.web.downloader.service.RestService;

@Slf4j
@RestController
public class Rest {

    private final RestService restService;

    public Rest(final RestService restService) {
        this.restService = restService;
    }

    @GetMapping(path = "/zip", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getZip(@RequestParam(value = "taskId") final String taskId){
        return this.restService.getZip(taskId);
    }

    @GetMapping(path = "/size", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> discoverSize(@RequestParam(value = "taskId") final String taskId){
        return this.restService.getSize(taskId);
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> statusTask(@RequestParam(value = "taskId") final String taskId){
        return this.restService.statusTask(taskId);
    }

    @GetMapping(path = "/require", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requireDownload(@RequestParam(value = "uri") final String URI){
        return this.restService.requireDownload(URI);
    }

    @GetMapping(path = "/estimate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> estimateSize(@RequestParam(value = "uri") final String URI){
        return this.restService.estimateSize(URI);
    }

    @GetMapping(path = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> mapSite(@RequestParam(value = "uri") final String URI){
        return this.restService.mapSite(URI);
    }

}
