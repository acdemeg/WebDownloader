package springboot.web.downloader.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation mark of rest methods which
 * have {@code URI} param and point to annotation handler
 * would perform check connection with remote resource by {@code URI}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUriConnection {
}
