package com.hp.es.cto.sp.persistence.entity.provider;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PROVIDER_SERVER")
public class ProviderServer {
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
	@Column(name = "CSA_PROVIDER_ID")
	private String csaProviderId;
	public void setCsaProviderId(String csaProviderId) {
		this.csaProviderId = csaProviderId;
	}
	public String getCsaProviderId() {
		return csaProviderId;
	}
	
	/* Data Column */
	@Column(name = "NAME")
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	/* Data Column */
	@Column(name = "IP_ADDRESS", unique=true)
	private String ipAddress;
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	
	/* Data Column */
	@Column(name = "PRIVATE_IP_ADDRESS",unique=true)
	private String privateIpAddress;
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}
	public String getPrivateIpAddress() {
		return privateIpAddress;
	}
	
	/* Data Column */
	@Column(name = "PUBLIC_IP_ADDRESS", unique=true)
	private String publicIpAddress;
	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}
	public String getPublicIpAddress() {
		return publicIpAddress;
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
	@Column(name = "OS_TYPE")
	private String osType;
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getOsType() {
		return osType;
	}
	
	/* Data Column */
	@Column(name = "PROVIDER")
	private String provider;
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProvider() {
		return provider;
	}
	
	/* Data Column */
	@Column(name = "ISALLOCATED")
	private String isAllocated;
	public void setIsAllocated(String isAllocated) {
		this.isAllocated = isAllocated;
	}
	public String getIsAllocated() {
		return isAllocated;
	}
	
	/* Data Column */
	@Column(name = "CREATE_DATE")
	private Date createDate;
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
}
