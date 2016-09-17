package com.hp.es.cto.sp.rest.form;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Object for REST request/response
 * 
 * @author Victor
 * @since
 */
@XmlRootElement(name = "context_ldap")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextNodeLdapMappingForm {

	private String uuid;
	@XmlElement(name = "context_node_id")
	@JsonProperty(value = "context_node_id")
	private String contextNodeId;
	@XmlElement(name = "ldap_dn")
	@JsonProperty(value = "ldap_dn")
	private String ldapDn;
	@XmlElement(name = "create_date")
	@JsonProperty(value = "create_date")
	private Date createDate;

	public ContextNodeLdapMappingForm() {
	}

	/**
	 * @return the unique id of the record
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the unique id of the record, not required if create new <code>ProviderKey</code>
	 */
	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return contextNodeId
	 */
	public String getContextNodeId() {
		return contextNodeId;
	}

	/**
	 * @param contextNodeId
	 *            the contextNodeId
	 */
	@FormParam("context_node_id")
	public void setContextNodeId(String contextNodeId) {
		this.contextNodeId = contextNodeId;
	}
	
	public String getLdapDn() {
		return ldapDn;
	}

	@FormParam("ldap_dn")
	public void setLdapDn(String ldapDn) {
		this.ldapDn = ldapDn;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create_date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
