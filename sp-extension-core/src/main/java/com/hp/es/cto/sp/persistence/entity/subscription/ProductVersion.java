package com.hp.es.cto.sp.persistence.entity.subscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_VERSION")
public class ProductVersion {
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

	/* Data Column */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	/* Data Column */
	@Column(name = "FULL_VERSION")
	private String fullVersion;

	public void setFullVersion(String fullVersion) {
		this.fullVersion = fullVersion;
	}

	public String getFullVersion() {
		return fullVersion;
	}

	/* Data Column */
	@Column(name = "DEFAULT_FLAG")
	private String defaultFlag;

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	/* Data Column */
	@Column(name = "PRIMARY_VERSION")
	private String primaryVersion;

	public String getPrimaryVersion() {
		return primaryVersion;
	}

	public void setPrimaryVersion(String primaryVersion) {
		this.primaryVersion = primaryVersion;
	}

	/* Data Column */
	@Column(name = "URL")
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
