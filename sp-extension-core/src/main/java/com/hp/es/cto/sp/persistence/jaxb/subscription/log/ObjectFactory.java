//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.10 at 12:14:27 PM CST 
//


package com.hp.es.cto.sp.persistence.jaxb.subscription.log;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hp.es.cto.sp.persistence.jaxb.subscription.log package. 
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

    private final static QName _SubscriptionLogs_QNAME = new QName("http://www.hp.com/es/cto/sp/context/jaxb/subscription/log", "subscription_logs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hp.es.cto.sp.persistence.jaxb.subscription.log
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubscriptionLogs }
     * 
     */
    public SubscriptionLogs createSubscriptionLogs() {
        return new SubscriptionLogs();
    }

    /**
     * Create an instance of {@link Slog }
     * 
     */
    public Slog createSlog() {
        return new Slog();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriptionLogs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.hp.com/es/cto/sp/context/jaxb/subscription/log", name = "subscription_logs")
    public JAXBElement<SubscriptionLogs> createSubscriptionLogs(SubscriptionLogs value) {
        return new JAXBElement<SubscriptionLogs>(_SubscriptionLogs_QNAME, SubscriptionLogs.class, null, value);
    }

}