package springboot.web.downloader.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import springboot.web.downloader.utils.Utils;

/**
 * This class provides aspect-methods for handling
 * annotations pointed in Rest controllers
 */
@Component
@Aspect
public class RestAspect {
    /**
     * This aspect performs check availability remote URL
     * which come from client request
     * @param pjp standard parameter
     * @return target return value if check was success
     */
    @Around(" @annotation(springboot.web.downloader.annotations.CheckUriConnection)")
    public Object checkUriConnectionImpl(final ProceedingJoinPoint pjp) throws Throwable {
        var uri = (String) pjp.getArgs()[0];
        var response = Utils.isLiveConnection(uri);
        if(response.getStatusCode().isError())
            return response;
        return pjp.proceed();
    }
}
