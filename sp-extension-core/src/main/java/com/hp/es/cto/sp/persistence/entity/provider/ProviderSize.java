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
@Table(name="PROVIDER_SIZE",uniqueConstraints = {@UniqueConstraint(columnNames = { "PROVIDER_TYPE_ID", "SIZE" }) })
public class ProviderSize {
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
	@Column(name = "DISPLAY_NAME")
	private String displayName;
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	
	/* Data Column */
	@Column(name = "SIZE")
	private String size;
	public void setSize(String size) {
		this.size = size;
	}
	public String getSize() {
		return size;
	}
	
	/* Data Column */
	@Column(name = "FLAVOR")
	private String flavor;
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public String getFlavor() {
		return flavor;
	}
	
	/* Data Column */
	@Column(name = "VCPU")
	private String vcpu;
	public void setVcpu(String vcpu) {
		this.vcpu = vcpu;
	}
	public String getVcpu() {
		return vcpu;
	}
	
	/* Data Column */
	@Column(name = "VMEMORY")
	private String vmemory;
	public void setVmemory(String vmemory) {
		this.vmemory = vmemory;
	}
	public String getVmemory() {
		return vmemory;
	}
	
	@OneToMany(targetEntity = OrgProviderSizeMapping.class, mappedBy = "providerSize", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<OrgProviderSizeMapping> orgProviderSizeMappings;

	public List<OrgProviderSizeMapping> getOrgProviderSizeMapping() {
		return orgProviderSizeMappings;
	}

	public void setOrgProviderSizeMapping(List<OrgProviderSizeMapping> orgProviderSizeMappings) {
		this.orgProviderSizeMappings = orgProviderSizeMappings;
	}
	
	@OneToMany(targetEntity = DesignProviderSizeMapping.class, mappedBy = "providerSize", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<DesignProviderSizeMapping> designProviderSizeMapping;

	public List<DesignProviderSizeMapping> getDesignProviderSizeMapping() {
		return designProviderSizeMapping;
	}

	public void setDesignProviderSizeMapping(List<DesignProviderSizeMapping> designProviderSizeMapping) {
		this.designProviderSizeMapping = designProviderSizeMapping;
	}
}
