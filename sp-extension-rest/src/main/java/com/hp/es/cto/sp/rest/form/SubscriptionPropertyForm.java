package com.hp.es.cto.sp.rest.form;

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
@XmlRootElement(name = "subscription_property")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionPropertyForm {

	private String uuid;
	@XmlElement(name = "subscription_id")
	@JsonProperty(value = "subscription_id")
	private String subscriptionId;
	@XmlElement(name = "property_name")
	@JsonProperty(value = "property_name")
	private String propertyName;
	@XmlElement(name = "property_value")
	@JsonProperty(value = "property_value")
	private String propertyValue;

	public SubscriptionPropertyForm() {
	}

	/**
	 * @return the unique id of the record
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the unique id of the record, not required if create new <code>ProviderKey</code>
	 */
	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the subscription id
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}

	/**
	 * @param subscription
	 *            subscription id
	 */
	@FormParam("subscription_id")
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	@FormParam("property_name")
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getPropertyValue() {
		return propertyValue;
	}

	@FormParam("property_value")
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	

}
