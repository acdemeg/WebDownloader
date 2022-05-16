package springboot.web.downloader.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import springboot.web.downloader.task.WebTask;
import springboot.web.downloader.utils.FunctionTwoArgs;
import springboot.web.downloader.utils.LoggableHttpTraceRepository;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

@Slf4j
@Configuration
public class Config {

    private final Wget wget;
    private final Zip zip;

    @Autowired
    public Config(Wget wget, Zip zip) {
        this.wget = wget;
        this.zip = zip;
    }

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new LoggableHttpTraceRepository();
    }

    @Bean
    public FunctionTwoArgs<String, String, WebTask> webTaskFactory() {
        return this::webTaskWithParam;
    }

    @Bean
    @Scope(value = "prototype")
    public WebTask webTaskWithParam(String taskId, String URI) {
        return new WebTask(wget, zip, taskId, URI);
    }
}
