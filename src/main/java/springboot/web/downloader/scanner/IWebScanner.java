package springboot.web.downloader.scanner;

import java.util.TreeMap;

/**
 * Here described methods need for built site map
 * and validate client links
 */
public interface IWebScanner {

    TreeMap<String, Object> toBuildSiteTreeMap(final String root);

    boolean validateRootLink(final String root);
}
