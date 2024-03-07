
package springboot.web.downloader.jaxb.http;


import javax.annotation.processing.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tChangeFreq.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tChangeFreq"&gt;
 *   &lt;restriction base="{<a href="http://www.w3.org/2001/XMLSchema">...</a>}string"&gt;
 *     &lt;enumeration value="always"/&gt;
 *     &lt;enumeration value="hourly"/&gt;
 *     &lt;enumeration value="daily"/&gt;
 *     &lt;enumeration value="weekly"/&gt;
 *     &lt;enumeration value="monthly"/&gt;
 *     &lt;enumeration value="yearly"/&gt;
 *     &lt;enumeration value="never"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "tChangeFreq", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
@XmlEnum
@Generated({})
public enum XmlChangeFreqHttp {

    @XmlEnumValue("always")
    ALWAYS("always"),
    @XmlEnumValue("hourly")
    HOURLY("hourly"),
    @XmlEnumValue("daily")
    DAILY("daily"),
    @XmlEnumValue("weekly")
    WEEKLY("weekly"),
    @XmlEnumValue("monthly")
    MONTHLY("monthly"),
    @XmlEnumValue("yearly")
    YEARLY("yearly"),
    @XmlEnumValue("never")
    NEVER("never");
    private final String value;

    XmlChangeFreqHttp(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XmlChangeFreqHttp fromValue(String v) {
        for (XmlChangeFreqHttp c: XmlChangeFreqHttp.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}