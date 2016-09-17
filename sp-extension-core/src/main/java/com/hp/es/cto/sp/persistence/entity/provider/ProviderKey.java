package com.hp.es.cto.sp.persistence.entity.provider;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PROVIDER_KEY")
public class ProviderKey {
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
	@Column(name = "CREATE_DATE")
	private Date createDate;
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
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
	@Column(name = "KEY_FILE")
	private byte[] keyFile;
	public void setKeyFile(byte[] keyFile) {
		this.keyFile = keyFile;
	}
	public byte[] getKeyFile() {
		return keyFile;
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
	@Column(name = "PASSWORD")
	private String password;
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	/* Data Column */
	@Column(name = "USERNAME")
	private String username;
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
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
	@Column(name = "REGION_ID")
	private String regionId;
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionId() {
		return regionId;
	}
}
