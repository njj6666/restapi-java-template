package com.hp.es.cto.sp.persistence.entity.provider;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PROVIDER_SHARED_RESOURCE")
public class ProviderSharedResource {
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
	@Column(name = "CURRENT_CONNECTION")
	private Integer currentConnection;
	public void setCurrentConnection(Integer currentConnection) {
		this.currentConnection = currentConnection;
	}
	public Integer getCurrentConnection() {
		return currentConnection;
	}
	
	/* Data Column */
	@Column(name = "MAX_CONNECTION")
	private Integer maxConnection;
	public void setMaxConnection(Integer maxConnection) {
		this.maxConnection = maxConnection;
	}
	public Integer getMaxConnection() {
		return maxConnection;
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
