package com.hp.es.cto.sp.persistence.entity.provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="PROVIDER_REGION_AZ",uniqueConstraints = {@UniqueConstraint(columnNames = { "REGION_ID", "AZ" }) })
public class ProviderRegionAz {
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
	@JoinColumn(name = "REGION_ID")
	private ProviderRegion providerRegion;
	public void setProviderRegion(ProviderRegion providerRegion) {
		this.providerRegion = providerRegion;
	}
	public ProviderRegion getProviderRegion() {
		return providerRegion;
	}
	
	/* Data Column */
	@Column(name = "AZ")
	private String az;
	public void setAz(String az) {
		this.az = az;
	}
	public String getAz() {
		return az;
	}
}
