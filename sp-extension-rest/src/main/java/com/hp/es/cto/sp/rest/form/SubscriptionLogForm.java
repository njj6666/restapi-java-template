package com.hp.es.cto.sp.rest.form;

import java.util.Date;

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
@XmlRootElement(name = "subscription_log")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionLogForm {

	private String uuid;
	@XmlElement(name = "subscription_id")
	@JsonProperty(value = "subscription_id")
	private String subscriptionId;
	@XmlElement(name = "log_level")
	@JsonProperty(value = "log_level")
	private String logLevel;
	@XmlElement(name = "message")
	@JsonProperty(value = "message")
	private String message;
	@XmlElement(name = "event")
	@JsonProperty(value = "event")
	private String event;
	@XmlElement(name = "component")
	@JsonProperty(value = "component")
	private String component;
	@XmlElement(name = "log_time")
	@JsonProperty(value = "log_time")
	private Date logTime;
	@XmlElement(name = "detail")
	@JsonProperty(value = "detail")
	private String detail;
	@XmlElement(name = "path")
	@JsonProperty(value = "path")
	private String path;
	@XmlElement(name = "tag")
	@JsonProperty(value = "tag")
	private String tag;

	public SubscriptionLogForm() {
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
	
	public String getLogLevel() {
		return logLevel;
	}

	@FormParam("log_level")
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getMessage() {
		return message;
	}

	@FormParam("message")
	public void setMessage(String message) {
		this.message = message;
	}

	public String getEvent() {
		return event;
	}

	@FormParam("event")
	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getComponent() {
		return component;
	}
	@FormParam("component")
	public void setComponent(String component) {
		this.component = component;
	}
	
	public Date getLogTime() {
		return logTime;
	}

	@FormParam("log_time")
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	
	public String getDetail() {
		return detail;
	}

	@FormParam("detail")
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getPath() {
		return path;
	}

	@FormParam("path")
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getTag() {
		return tag;
	}

	@FormParam("tag")
	public void setTag(String tag) {
		this.tag = tag;
	}
}
