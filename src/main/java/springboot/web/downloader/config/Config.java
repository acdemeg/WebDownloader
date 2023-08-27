package springboot.web.downloader.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.web.downloader.enums.TypeTask;
import springboot.web.downloader.task.WebTask;
import springboot.web.downloader.utils.FunctionManyArgs;
import springboot.web.downloader.wget.Wget;
import springboot.web.downloader.zip.Zip;

/**
 * This class contain beans definitions
 * which using in application
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class Config {

    private final Wget wget;
    private final Zip zip;

    @Autowired
    public Config(Wget wget, Zip zip) {
        this.wget = wget;
        this.zip = zip;
    }

    /**
     * Two next beans represents prototype-bean fabric
     * with many arguments bean constructor
     *
     * @return function interface for many arguments
     */
    @Bean
    public FunctionManyArgs<TypeTask, String, WebTask> webTaskFactory() {
        return this::webTaskWithParam;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    @Scope(value = "prototype")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WebTask webTaskWithParam(String taskId, String uri, TypeTask typeTask) {
        return new WebTask(wget, zip, taskId, uri, typeTask);
    }
}
