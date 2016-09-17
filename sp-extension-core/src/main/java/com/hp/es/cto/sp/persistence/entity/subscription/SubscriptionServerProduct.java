package com.hp.es.cto.sp.persistence.entity.subscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SUBSCRIPTION_SERVER_PRODUCT")
public class SubscriptionServerProduct {
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
	@JoinColumn(name = "SERVER_ID")
	private SubscriptionServer server;
	public void setSubscriptionServer(SubscriptionServer server) {
		this.server = server;
	}
	public SubscriptionServer getSubscriptionServer() {
		return server;
	}
	
	/* Data Column */
	@Column(name = "PRODCUT_NAME")
	private String productName;
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductName() {
		return productName;
	}
	
	/* Data Column */
	@Column(name = "PRODUCT_VERSION")
	private String productVersion;
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getProductVersion() {
		return productVersion;
	}
}
