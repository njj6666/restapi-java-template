package com.hp.es.cto.sp.persistence.entity.keymgmt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER_KEY")
public class UserKey {
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
	@Column(name = "LDAP_EMAIL")
	private String ldapEmail;
	public void setLdapEmail(String ldapEmail) {
		this.ldapEmail = ldapEmail;
	}
	public String getLdapEmail() {
		return ldapEmail;
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
	@Column(name = "KEY_FILE")
	private byte[] keyFile;
	public void setKeyFile(byte[] keyFile) {
		this.keyFile = keyFile;
	}
	public byte[] getKeyFile() {
		return keyFile;
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
