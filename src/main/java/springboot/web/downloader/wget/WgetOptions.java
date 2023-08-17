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

    private static final String LEVEL = "1";
    private static final String QUOTE_SIZE = "5000m";

    private static final String USER_AGENT = "--user-agent=\"Mozilla/5.0 (X11; Linux x86_64)" +
            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36\"";
    private static final String REFERER = "--referer=\"https://www.google.com/\"";
    private static final String IGNORE_ROBOTS_TXT = "--execute robots=off";
    private static final String NO_CHECK_CERTIFICATE = "--no-check-certificate";
    private static final String PAGE_REQUISITES = "--page-requisites";
    private static final String HTML_EXTENSION = "--html-extension";
    private static final String CONVERT_LINKS = "--convert-links";
    private static final String BACKGROUND_RUN = "--background"; //off
    private static final String LOG_FILE = "--output-file=wget-log";
    private static final String MIRROR = "--recursive --timestamping --level " + LEVEL + " --dont-remove-listing";
    private static final String SPIDER = "--spider --server-response --no-directories";
    private static final String QUOTA = "--quota=" + QUOTE_SIZE;

    private final String wgetDownload = String.format("wget %s %s %s %s %s %s %s %s %s %s ", USER_AGENT, REFERER,
            LOG_FILE, IGNORE_ROBOTS_TXT, NO_CHECK_CERTIFICATE, PAGE_REQUISITES, HTML_EXTENSION, CONVERT_LINKS, MIRROR, QUOTA);
    private final String wgetEstimate = String.format("%s %s ", wgetDownload, SPIDER);

}
