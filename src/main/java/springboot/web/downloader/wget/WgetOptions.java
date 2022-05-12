package springboot.web.downloader.wget;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * This class represent set of params which can be
 * used for specify 'wget' shell-utility
 */
@Data
@Component
public final class WgetOptions {

    private final String userAgent = "--user-agent=\"Mozilla/5.0 (X11; Linux x86_64)" +
            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36\"";
    private final String referer = "--referer=\"https://www.google.com/\"";
    private final String ignoreRobotsTxt = "--execute robots=off";
    private final String noCheckCertificate = "--no-check-certificate";
    private final String pageRequisites = "--page-requisites";
    private final String htmlExtension = "--html-extension";
    private final String convertLinks = "--convert-links";
    private final String level = "1";
    private final String backgroundRun = "--background"; //off
    private final String logFile = "--output-file=wget-log";
    private final String mirror = "--recursive --timestamping --level " + level + " --dont-remove-listing";
    private final String wget = String.format("wget %s %s %s %s %s %s %s %s %s ", userAgent, referer, logFile,
            ignoreRobotsTxt, noCheckCertificate, pageRequisites, htmlExtension, convertLinks, mirror);
}
