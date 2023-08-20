package springboot.web.downloader.wget;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springboot.web.downloader.WebDownloader;
import springboot.web.downloader.enums.NativeProcessName;
import springboot.web.downloader.jaxb.IXmlUrlSet;
import springboot.web.downloader.jaxb.http.XmlUrlSetHttp;
import springboot.web.downloader.jaxb.https.XmlUrlSetHttps;
import springboot.web.downloader.utils.Utils;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
@Service
public class WgetImpl implements Wget {

    @Override
    public int download(final String URI, final String dir) throws InterruptedException, IOException {
        String command = WgetOptions.WGET_DOWNLOAD + URI;
        return Utils.runProcess(command, NativeProcessName.WGET_DOWNLOAD.name(), dir);
    }

    @Override
    public int estimate(final String URI, final String dir) throws IOException, InterruptedException {
        String command = WgetOptions.WGET_ESTIMATE + URI;
        return Utils.runProcess(command, NativeProcessName.WGET_ESTIMATE.name(), dir);
    }

    @Override
    public IXmlUrlSet getSiteMap(final String URI, final String dir) throws IOException, InterruptedException, JAXBException {
        try {
            // priority 1: /root-domain/sitemap.xml
            ResponseEntity<String> entity = Utils.getResponse(URI + "sitemap.xml");
            return unmarshallingSitemapXml(entity.getBody());
        } catch (final Exception ex) {
            try {
                // priority 2: /root-domain/(url from robots.txt)
                String sitemap = getSiteMapFromRobotsTxt(URI);
                return unmarshallingSitemapXml(sitemap);
            } catch (Exception ex2) {
                // priority 3: generating sitemap.xml using WGET
                String generatedSitemap = generateSitemapWithWget(URI, dir);
                return unmarshallingSitemapXml(generatedSitemap);
            }
        }
    }

    private String generateSitemapWithWget(final String URI, final String dir) throws IOException, InterruptedException {
        Path sh = Paths.get(WebDownloader.SITEMAP_GENERATOR_SCRIPT).toAbsolutePath();
        String sitemapDir = WebDownloader.BASE_SITEMAPS + dir;
        Utils.createDirectory(sitemapDir);
        int exitCode = Utils.runProcess(sh + " " + URI + " " + sitemapDir,
                NativeProcessName.WGET_GENERATE_SITEMAP.name(), sitemapDir);
        if (exitCode == 0) {
            File file = new File(sitemapDir + "/sitemap.xml");
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        }
        return "";
    }

    private String getSiteMapFromRobotsTxt(final String URI) {
        String robotsBody = Utils.getResponse(URI + "robots.txt").getBody();
        if (robotsBody == null) {
            return "";
        }
        String sitemapLine = Arrays.stream(robotsBody.split("\n")).filter(line ->
                line.toLowerCase().startsWith("sitemap")).findFirst().orElse("");
        int start = sitemapLine.toLowerCase().indexOf("http");
        String sitemapAddress = start > -1 ? sitemapLine.substring(start) : "";
        if (sitemapAddress.isEmpty()) {
            return "";
        }
        String sitemap = Utils.getResponse(sitemapAddress).getBody();
        return sitemap == null ? "" : sitemap;
    }

    private IXmlUrlSet unmarshallingSitemapXml(String body) throws JAXBException {
        if (body == null || body.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Xml body must be not null and not empty");
        }
        boolean isHttp = validateXml(body, WebDownloader.SITEMAP_XSD_HTTP);
        boolean isHttps = false;
        if(!isHttp) {
            isHttps = validateXml(body, WebDownloader.SITEMAP_XSD_HTTPS);
        }
        if (!(isHttp || isHttps)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Xml validation error");
        }
        Class<?> clazz = isHttp ? XmlUrlSetHttp.class : XmlUrlSetHttps.class;

        return unmarshallingXml(body, clazz);
    }

    private IXmlUrlSet unmarshallingXml(String body, Class<?> clazz) throws JAXBException {
        try (StringReader xmlReader = new StringReader(body)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (IXmlUrlSet) jaxbUnmarshaller.unmarshal(xmlReader);
        }
    }

    @SuppressWarnings("java:S2755")
    private boolean validateXml(String body, String schemaPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaFile = new StreamSource(FileUtils.getFile(schemaPath));
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(body))); // Else StreamClosed Exception!
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
