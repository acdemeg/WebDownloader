package springboot.web.downloader.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class UtilsTest {

    private final Utils utils;

    @Autowired
    UtilsTest(Utils utils) {
        this.utils = utils;
    }

    @Test
    void runProcess() {
    }

    @Test
    void isLiveConnection() {
        var success = utils.isLiveConnection("https://locallhost.com/");
        Assertions.assertEquals(HttpStatus.OK, success.getStatusCode());

        var error = utils.isLiveConnection("https://refactoring.guru/");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }
}