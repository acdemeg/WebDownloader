package springboot.web.downloader.wget;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represent set of params which can be
 * used for specify 'wget' shell-utility
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WgetOptions {

    public static final String LEVEL = "1";
    public static final String QUOTE_SIZE = "5000m";
    public static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36";
    public static final String USER_AGENT_PROP = "--user-agent=\"" + USER_AGENT + "\"";
    public static final String REFERER = "--referer=\"https://www.google.com/\"";
    public static final String IGNORE_ROBOTS_TXT = "--execute robots=off";
    public static final String NO_CHECK_CERTIFICATE = "--no-check-certificate";
    public static final String PAGE_REQUISITES = "--page-requisites";
    public static final String HTML_EXTENSION = "--html-extension";
    public static final String CONVERT_LINKS = "--convert-links";
    public static final String BACKGROUND_RUN = "--background"; //off
    public static final String LOG_FILE = "--output-file=wget-log";
    public static final String MIRROR = "--recursive --timestamping --level " + LEVEL + " --dont-remove-listing";
    public static final String SPIDER = "--spider --server-response --no-directories";
    public static final String QUOTA = "--quota=" + QUOTE_SIZE;

    public static final String WGET_DOWNLOAD = String.format("wget %s %s %s %s %s %s %s %s %s %s ", USER_AGENT_PROP, REFERER,
            LOG_FILE, IGNORE_ROBOTS_TXT, NO_CHECK_CERTIFICATE, PAGE_REQUISITES, HTML_EXTENSION, CONVERT_LINKS, MIRROR, QUOTA);
    public static final String WGET_ESTIMATE = String.format("%s %s ", WGET_DOWNLOAD, SPIDER);

}
