package com.hp.es.cto.sp.persistence.entity.group;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ORGANIZATION")
public class Organization {
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
	@Column(name = "NAME")
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	/* Data Column */
	@Column(name = "CSA_ORGANIZATION_ID")
	private String csaOrganizationId;
	public void setCsaOrganizationId(String csaOrganizationId) {
		this.csaOrganizationId = csaOrganizationId;
	}
	public String getCsaOrganizationId() {
		return csaOrganizationId;
	}
	
	/* Data Column */
	@Column(name = "LDAP_SERVER")
	private String ldapServer;
	public void setLdapServer(String ldapServer) {
		this.ldapServer = ldapServer;
	}
	public String getLdapServer() {
		return ldapServer;
	}
	
	/* Data Column */
	@Column(name = "PORT")
	private String port;
	public void setPort(String port) {
		this.port = port;
	}
	public String getPort() {
		return port;
	}
	
	/* Data Column */
	@Column(name = "IS_SSL")
	private Boolean isSsl;
	public void setIsSsl(Boolean isSsl) {
		this.isSsl = isSsl;
	}
	public Boolean getIsSsl() {
		return isSsl;
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
	@Column(name = "PASSWORD")
	private String password;
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	/* Data Column */
	@Column(name = "SEARCH_BASE")
	private String searchBase;
	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}
	public String getSearchBase() {
		return searchBase;
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
