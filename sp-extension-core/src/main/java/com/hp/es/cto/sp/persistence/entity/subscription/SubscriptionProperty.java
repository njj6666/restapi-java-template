package com.hp.es.cto.sp.persistence.entity.subscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SUBSCRIPTION_PROPERTY")
public class SubscriptionProperty {
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
	@Column(name = "PROPERTY_NAME")
	private String propertyName;
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	
	/* Data Column */
	@Column(name = "PROPERTY_VALUE")
	private String propertyValue;
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
}
