package com.hp.es.cto.sp.rest.form;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Object for REST request/response
 * 
 * @author Victor
 * @since
 */
@XmlRootElement(name = "subscription_collection")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionCollectionForm {

	@XmlElement(name = "size")
	@JsonProperty(value = "size")
	private Integer size;
	@XmlElement(name = "subscription")
	@JsonProperty(value = "subscription")
	private List<SubscriptionForm> subscription;

	public SubscriptionCollectionForm() {
	}

	/**
	 * @return the size of the records
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size of the records
	 */
	@FormParam("uuid")
	public void setSize(Integer size) {
		this.size = size;
	}

	
	public List<SubscriptionForm> getSubscriptions() {
		return subscription;
	}

	@FormParam("subscription")
	public void setSubscriptions(List<SubscriptionForm> subscription) {
		this.subscription = subscription;
	}
	
}
