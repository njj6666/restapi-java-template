package com.hp.es.cto.sp.persistence.entity.context;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="CONTEXT_NODE_LDAP_MAPPING")
public class ContextNodeLdapMapping {
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
	@JoinColumn(name = "CONTEXT_NODE_ID")
	private ContextNode node;
	public void setNode(ContextNode node) {
		this.node = node;
	}
	public ContextNode getNode() {
		return node;
	}
	/* Data Column */
	@Column(name = "LDAP_DN")
	private String ldapDn;
	public void setLdapDn(String ldapDn) {
		this.ldapDn = ldapDn;
	}
	public String getLdapDn() {
		return ldapDn;
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
