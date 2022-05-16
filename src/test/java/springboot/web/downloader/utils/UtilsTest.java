package springboot.web.downloader.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class UtilsTest {

    @Test
    void runProcess() {
    }

    @Test
    void isLiveConnection() {
        var success = Utils.isLiveConnection("https://locallhost.com/");
        Assertions.assertEquals(HttpStatus.OK, success.getStatusCode());

        var error = Utils.isLiveConnection("https://unreacheble-XXX-url.guru/");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }
}