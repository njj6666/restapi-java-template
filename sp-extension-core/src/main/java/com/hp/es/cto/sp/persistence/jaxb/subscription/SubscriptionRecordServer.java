//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.31 at 01:11:14 PM CST 
//


package com.hp.es.cto.sp.persistence.jaxb.subscription;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subscription_record_server complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subscription_record_server">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="public_ipAddress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="private_ipaddress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="csa_provider_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="iaas_provider" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dns" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="simplified_dns" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="os_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="active_flag" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="products" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subscription_record_server")
public class SubscriptionRecordServer {

    @XmlAttribute
    protected String ipAddress;
    @XmlAttribute(name = "public_ipAddress")
    protected String publicIpAddress;
    @XmlAttribute(name = "private_ipaddress")
    protected String privateIpaddress;
    @XmlAttribute
    protected String size;
    @XmlAttribute(name = "csa_provider")
    protected String csaProvider;
    @XmlAttribute(name = "iaas_provider")
    protected String iaasProvider;
    @XmlAttribute
    protected String dns;
    @XmlAttribute(name = "simplified_dns")
    protected String simplifiedDns;
    @XmlAttribute(name = "os_type")
    protected String osType;
    @XmlAttribute(name = "active_flag")
    protected String activeFlag;
    @XmlAttribute
    protected String products;

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the publicIpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    /**
     * Sets the value of the publicIpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicIpAddress(String value) {
        this.publicIpAddress = value;
    }

    /**
     * Gets the value of the privateIpaddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrivateIpaddress() {
        return privateIpaddress;
    }

    /**
     * Sets the value of the privateIpaddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrivateIpaddress(String value) {
        this.privateIpaddress = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of the csaProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsaProvider() {
        return csaProvider;
    }

    /**
     * Sets the value of the csaProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsaProvider(String value) {
        this.csaProvider = value;
    }

    /**
     * Gets the value of the iaasProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIaasProvider() {
        return iaasProvider;
    }

    /**
     * Sets the value of the iaasProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIaasProvider(String value) {
        this.iaasProvider = value;
    }

    /**
     * Gets the value of the dns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDns() {
        return dns;
    }

    /**
     * Sets the value of the dns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDns(String value) {
        this.dns = value;
    }

    /**
     * Gets the value of the simplifiedDns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimplifiedDns() {
        return simplifiedDns;
    }

    /**
     * Sets the value of the simplifiedDns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimplifiedDns(String value) {
        this.simplifiedDns = value;
    }

    /**
     * Gets the value of the osType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOsType() {
        return osType;
    }

    /**
     * Sets the value of the osType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOsType(String value) {
        this.osType = value;
    }

    /**
     * Gets the value of the activeFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActiveFlag() {
        return activeFlag;
    }

    /**
     * Sets the value of the activeFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActiveFlag(String value) {
        this.activeFlag = value;
    }

    /**
     * Gets the value of the products property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducts() {
        return products;
    }

    /**
     * Sets the value of the products property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducts(String value) {
        this.products = value;
    }

}
