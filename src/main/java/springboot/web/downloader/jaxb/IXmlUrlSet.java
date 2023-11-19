package springboot.web.downloader.jaxb;

import springboot.web.downloader.jaxb.http.XmlUrlSetHttp;
import springboot.web.downloader.jaxb.https.XmlUrlSetHttps;

import java.util.List;
import java.util.function.Supplier;

public interface IXmlUrlSet {
    List<IXmlUrl> getUrl();

    static Supplier<IXmlUrlSet> getImpl(IXmlUrlSet obj) {
        return (obj instanceof XmlUrlSetHttp) ? XmlUrlSetHttp::new : XmlUrlSetHttps::new;
    }
}
