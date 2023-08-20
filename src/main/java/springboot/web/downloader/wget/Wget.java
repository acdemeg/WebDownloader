package springboot.web.downloader.wget;

import org.xml.sax.SAXException;
import springboot.web.downloader.jaxb.IXmlUrlSet;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Here describe methods which take response for download
 * and approximately size estimate of
 * web-resources with help wget shell-utility
 */
public interface Wget {

    /**
     * This method perform run shell-utility 'wget'
     * with need number of parameters in order to
     * create local copy web-resource
     *
     * @param URI root link web-resource
     * @param dir dest folder for download
     * @return status code shell-command
     */
    int download(final String URI, final String dir) throws InterruptedException, IOException;

    /**
     * This method perform run shell-utility 'wget'
     * in spider mode for approximately estimate size web-resource
     * Method not create none local files and directories except wget-log
     *
     * @param URI root link web-resource
     * @param dir workdir for process
     * @return status code shell-command
     */
    int estimate(final String URI, final String dir) throws IOException, InterruptedException;

    /**
     * This method check if exist sitemap for current site and return it in case success
     * otherwise create sitemap using wget utility
     * @param URI sitemap uri
     * @param dir workdir for process
     * @return sitemap object
     */
    IXmlUrlSet getSiteMap(final String URI, final String dir) throws IOException, InterruptedException, JAXBException, SAXException;

}
