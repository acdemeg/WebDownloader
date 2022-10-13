package springboot.web.downloader.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springboot.web.downloader.dto.ResponseDto;

public class ResponseUtils {

    private ResponseUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<ResponseDto> badRequest(final Exception ex) {
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ResponseDto> notFound(final String description) {
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.NOT_FOUND.value(), description),
                HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ResponseDto> internalServerError(final String description) {
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), description),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ResponseDto> ok(final String description) {
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.OK.value(), description),
                HttpStatus.OK);
    }

    public static ResponseEntity<ResponseDto> preconditionFailed(final String description) {
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.PRECONDITION_FAILED.value(), description),
                HttpStatus.PRECONDITION_FAILED);
    }
}
