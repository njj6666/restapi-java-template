//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.31 at 01:11:14 PM CST 
//


package com.hp.es.cto.sp.persistence.jaxb.subscription;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hp.es.cto.sp.persistence.jaxb.subscription package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SubscriptionReport_QNAME = new QName("http://www.hp.com/es/cto/sp/context/jaxb/subscription", "subscription_report");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hp.es.cto.sp.persistence.jaxb.subscription
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubscriptionRecord }
     * 
     */
    public SubscriptionRecord createSubscriptionRecord() {
        return new SubscriptionRecord();
    }

    /**
     * Create an instance of {@link SubscriptionReport }
     * 
     */
    public SubscriptionReport createSubscriptionReport() {
        return new SubscriptionReport();
    }

    /**
     * Create an instance of {@link SubscriptionRecordServer }
     * 
     */
    public SubscriptionRecordServer createSubscriptionRecordServer() {
        return new SubscriptionRecordServer();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriptionReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.hp.com/es/cto/sp/context/jaxb/subscription", name = "subscription_report")
    public JAXBElement<SubscriptionReport> createSubscriptionReport(SubscriptionReport value) {
        return new JAXBElement<SubscriptionReport>(_SubscriptionReport_QNAME, SubscriptionReport.class, null, value);
    }

}
