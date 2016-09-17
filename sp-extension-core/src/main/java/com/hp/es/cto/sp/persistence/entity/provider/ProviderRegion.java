package com.hp.es.cto.sp.persistence.entity.provider;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="PROVIDER_REGION",uniqueConstraints = {@UniqueConstraint(columnNames = { "PROVIDER_TYPE_ID", "REGION_NAME" }) })
public class ProviderRegion {
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
	@Column(name = "REGION_NAME")
	private String regionName;
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionName() {
		return regionName;
	}
	
	/* Data Column */
	@Column(name = "REGION_DISPLAY_NAME")
	private String regionDisplayName;
	public void setRegionDisplayName(String regionDisplayName) {
		this.regionDisplayName = regionDisplayName;
	}
	public String getRegionDisplayName() {
		return regionDisplayName;
	}
	
	@OneToMany(mappedBy = "providerRegion", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ProviderRegionAz> providerRegionAz;

	public List<ProviderRegionAz> getProviderRegionAz() {
		return providerRegionAz;
	}

	public void setProviderRegionAz(List<ProviderRegionAz> providerRegionAz) {
		this.providerRegionAz = providerRegionAz;
	}
	
	@OneToMany(mappedBy = "providerRegion", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<OrgProviderRegionMapping> orgProviderRegionMappings;

	public List<OrgProviderRegionMapping> getOrgProviderRegionMapping() {
		return orgProviderRegionMappings;
	}

	public void setOrgProviderRegionMapping(List<OrgProviderRegionMapping> orgProviderRegionMappings) {
		this.orgProviderRegionMappings = orgProviderRegionMappings;
	}
}
