package com.hp.es.cto.sp.persistence.entity.subscription;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SUBSCRIPTION_LOG")
public class SubscriptionLog {
	/* Primary Key */
	@Id
	@Column(name = "UUID")
	private String uuid;
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuid() {
		return uuid;
	}
	
	/* Foreign Key */
	@ManyToOne
	@JoinColumn(name = "SUBSCRIPTION_ID")
	private Subscription subscription;
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	public Subscription getSubscription() {
		return subscription;
	}
	
	/* Data Column */
	@Column(name = "EVENT")
	private String event;
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEvent() {
		return event;
	}
	
	/* Data Column */
	@Column(name = "LOG_LEVEL")
	private String logLevel;
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getLogLevel() {
		return logLevel;
	}
	
	/* Data Column */
	@Column(name = "MESSAGE")
	private String message;
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	
	/* Data Column */
	@Column(name = "COMPONENT")
	private String component;
	public void setComponent(String component) {
		this.component = component;
	}
	public String getComponent() {
		return component;
	}
	
	/* Data Column */
	@Column(name = "LOG_TIME")
	private Date logTime;
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public Date getLogTime() {
		return logTime;
	}
	
	/* Data Column */
	@Column(name = "LOG_DETAIL")
	private String detail;
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getDetail() {
		return detail;
	}
	
	/* Data Column */
	@Column(name = "LOG_PATH")
	private String path;
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	
	/* Data Column */
	@Column(name = "LOG_TAG")
	private String tag;
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTag() {
		return tag;
	}
	
}
