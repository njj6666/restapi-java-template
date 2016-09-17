package com.hp.es.cto.sp.persistence.entity.provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ORG_PROVIDER_TYPE")
public class OrgProviderTypeMapping {
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
	@JoinColumn(name = "PROVIDER_TYPE_ID")
	private ProviderType providerType;
	public void setProviderType(ProviderType providerType) {
		this.providerType = providerType;
	}
	public ProviderType getProviderType() {
		return providerType;
	}
	
	/* Data Column */
	@Column(name = "ORG_ID")
	private String orgId;
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgId() {
		return orgId;
	}
}
