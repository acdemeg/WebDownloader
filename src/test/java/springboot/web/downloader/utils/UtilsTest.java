package springboot.web.downloader.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@SpringBootTest
class UtilsTest {

    @Test
    void runProcess() throws IOException, InterruptedException {
        int exitSuccess = Utils.runProcess("ls", "ls", System.getProperty("user.home"));
        Assertions.assertEquals(0, exitSuccess);
        int exitError = Utils.runProcess("ls -something", "ls", System.getProperty("user.home"));
        Assertions.assertNotEquals(0, exitError);
    }

    @Test
    void isLiveConnection() {
        var success = Utils.isLiveConnection("https://locallhost.com/");
        Assertions.assertEquals(HttpStatus.OK, success.getStatusCode());
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> Utils.isLiveConnection("https://unreacheble-XXX-url.guru/"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}