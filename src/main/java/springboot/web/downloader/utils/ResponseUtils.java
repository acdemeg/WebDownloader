package springboot.web.downloader.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springboot.web.downloader.enums.ErrorStruct;

import java.util.Map;

public class ResponseUtils {

    public static ResponseEntity<?> badRequest(final Exception ex) {
        return new ResponseEntity<>(
                new ErrorStruct(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> notFound(final String errorDescription) {
        return new ResponseEntity<>(
                new ErrorStruct(HttpStatus.NOT_FOUND.value(), errorDescription),
                HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> notAcceptable(final Map<String, ?> validationErrors) {
        return new ResponseEntity<>(validationErrors, HttpStatus.NOT_ACCEPTABLE);
    }

}