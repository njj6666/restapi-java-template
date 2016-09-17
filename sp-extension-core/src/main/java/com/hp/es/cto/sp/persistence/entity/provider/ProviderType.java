package com.hp.es.cto.sp.persistence.entity.provider;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "PROVIDER_TYPE", uniqueConstraints = {@UniqueConstraint(columnNames = { "TYPE", "SUB_TYPE" }) })
public class ProviderType {
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
	@Column(name = "TYPE")
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	/* Data Column */
	@Column(name = "SUB_TYPE")
	private String subType;

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getSubType() {
		return subType;
	}

	@OneToMany(mappedBy = "providerType", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ProviderRegion> providerRegions;

	public List<ProviderRegion> getProviderRegion() {
		return providerRegions;
	}

	public void setProviderRegion(List<ProviderRegion> providerRegions) {
		this.providerRegions = providerRegions;
	}

	@OneToMany(mappedBy = "providerType", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ProviderSize> providerSize;

	public List<ProviderSize> getProviderSize() {
		return providerSize;
	}

	public void setProviderSize(List<ProviderSize> providerSize) {
		this.providerSize = providerSize;
	}
	
	@OneToMany(mappedBy = "providerType", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<OrgProviderTypeMapping> orgProviderTypeMappings;

	public List<OrgProviderTypeMapping> getOrgProviderTypeMapping() {
		return orgProviderTypeMappings;
	}

	public void setOrgProviderTypeMapping(List<OrgProviderTypeMapping> orgProviderTypeMappings) {
		this.orgProviderTypeMappings = orgProviderTypeMappings;
	}
}
