package springboot.web.downloader.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.web.downloader.utils.LoggableHttpTraceRepository;

@Slf4j
@Configuration
public class Config {
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new LoggableHttpTraceRepository();
    }
}
