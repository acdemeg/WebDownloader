package springboot.web.downloader.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;

@Slf4j
public class LoggableHttpTraceRepository extends InMemoryHttpTraceRepository {
    @Override
    public void add(HttpTrace trace) {
        super.add(trace);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            log.info(objectMapper.writeValueAsString(trace));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}

