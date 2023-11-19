
package springboot.web.downloader.jaxb.https;

import javax.annotation.processing.Generated;
import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 *
 *         Container for the data needed to describe a document to crawl.
 *
 *
 * <p>Java class for tUrl complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="tUrl"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{<a href="https://www.w3.org/2001/XMLSchema">...</a>}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="loc" type="{https://www.sitemaps.org/schemas/sitemap/0.9}tLoc"/&gt;
 *         &lt;element name="lastmod" type="{<a href="https://www.sitemaps.org/schemas/sitemap/0.9">...</a>}tLastmod" minOccurs="0"/&gt;
 *         &lt;element name="changefreq" type="{https://www.sitemaps.org/schemas/sitemap/0.9}tChangeFreq" minOccurs="0"/&gt;
 *         &lt;element name="priority" type="{https://www.sitemaps.org/schemas/sitemap/0.9}tPriority" minOccurs="0"/&gt;
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tUrl", namespace = "https://www.sitemaps.org/schemas/sitemap/0.9", propOrder = {
    "loc",
    "lastmod",
    "changefreq",
    "priority",
    "any"
})
@Generated({})
public class XmlUrlHttps implements springboot.web.downloader.jaxb.IXmlUrl {

    @XmlElement(namespace = "https://www.sitemaps.org/schemas/sitemap/0.9", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String loc;
    @XmlElement(namespace = "https://www.sitemaps.org/schemas/sitemap/0.9")
    protected String lastmod;
    @XmlElement(namespace = "https://www.sitemaps.org/schemas/sitemap/0.9")
    @XmlSchemaType(name = "string")
    protected XmlChangeFreqHttps changefreq;
    @XmlElement(namespace = "https://www.sitemaps.org/schemas/sitemap/0.9")
    protected BigDecimal priority;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the loc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
    public String getLoc() {
        return loc;
    }

    /**
     * Sets the value of the loc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoc(String value) {
        this.loc = value;
    }

    /**
     * Gets the value of the lastmod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastmod() {
        return lastmod;
    }

    /**
     * Sets the value of the lastmod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastmod(String value) {
        this.lastmod = value;
    }

    /**
     * Gets the value of the changefreq property.
     * 
     * @return
     *     possible object is
     *     {@link XmlChangeFreqHttps }
     *     
     */
    public XmlChangeFreqHttps getChangefreq() {
        return changefreq;
    }

    /**
     * Sets the value of the changefreq property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlChangeFreqHttps }
     *     
     */
    public void setChangefreq(XmlChangeFreqHttps value) {
        this.changefreq = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPriority(BigDecimal value) {
        this.priority = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<>();
        }
        return this.any;
    }

}
