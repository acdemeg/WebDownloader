package springboot.web.downloader.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.web.downloader.service.RestService;
import springboot.web.downloader.utils.Utils;

import java.io.IOException;

@Slf4j
@RestController
public class Rest {

    private final RestService restService;

    public Rest(RestService restService) {
        this.restService = restService;
    }

    @GetMapping(path = "/zip", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getZip(@RequestParam(value = "taskId") final String taskId) throws IOException {
        return this.restService.getZip(taskId);
    }

    @GetMapping(path = "/require", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requireDownload(@RequestParam(value = "uri") final String URI) {
        var response = Utils.isLiveConnection(URI);
        return (response.getStatusCode().isError()) ? response : this.restService.requireDownload(URI);
    }

    @GetMapping(path = "/estimate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> estimateSize(@RequestParam(value = "uri") final String URI){
        var response = Utils.isLiveConnection(URI);
        return (response.getStatusCode().isError()) ? response : this.restService.estimateSize(URI);
    }

    @GetMapping(path = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> mapSite(@RequestParam(value = "uri") final String URI){
        var response = Utils.isLiveConnection(URI);
        return (response.getStatusCode().isError()) ? response : this.restService.mapSite(URI);
    }

}
