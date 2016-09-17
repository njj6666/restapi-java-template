package com.hp.es.cto.sp.persistence.entity.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="GROUP_MAPPING")
public class GroupMapping {
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
	@JoinColumn(name = "GROUP_ID")
	private String groupId;
	public void setLogicalGroup(String groupId) {
		this.groupId = groupId;
	}
	public String getLogicalGroup() {
		return groupId;
	}
	
	/* Data Column */
	@Column(name = "LDAP_GROUP")
	private String ldapGroup;
	public void setLdapGroup(String ldapGroup) {
		this.ldapGroup = ldapGroup;
	}
	public String getLdapGroup() {
		return ldapGroup;
	}
}
