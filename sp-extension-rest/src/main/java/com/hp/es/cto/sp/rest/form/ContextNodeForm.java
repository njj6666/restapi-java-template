package com.hp.es.cto.sp.rest.form;

import java.util.Date;
import java.util.List;

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
@XmlRootElement(name = "context_node")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextNodeForm {

	private String uuid;
	@XmlElement(name = "name")
	@JsonProperty(value = "name")
	private String name;
	@XmlElement(name = "display_name")
	@JsonProperty(value = "display_name")
	private String displayName;
	@XmlElement(name = "context_metadata_id")
	@JsonProperty(value = "context_metadata_id")
	private String contextMetaDataId;
	@XmlElement(name = "parent_node_id")
	@JsonProperty(value = "parent_node_id")
	private String parentNodeId;
	@XmlElement(name = "child_node_ids")
	@JsonProperty(value = "child_node_ids")
	private List<String> childNodeIds;
	@XmlElement(name = "ldap_mapping")
	@JsonProperty(value = "ldap_mapping")
	private List<ContextNodeLdapMappingForm> ldapMappings;
	@XmlElement(name = "provider_mapping")
	@JsonProperty(value = "provider_mapping")
	private List<ContextNodeProviderMappingForm> providerMappings;
	@XmlElement(name = "create_date")
	@JsonProperty(value = "create_date")
	private Date createDate;

	public ContextNodeForm() {
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
	 * @return contextMetaDataId
	 */
	public String getContextMetaDataId() {
		return contextMetaDataId;
	}

	/**
	 * @param contextMetaDataId
	 *            the contextMetaDataId
	 */
	@FormParam("context_metadata_id")
	public void setContextMetaDataId(String contextMetaDataId) {
		this.contextMetaDataId = contextMetaDataId;
	}
	
	public String getParentNodeId() {
		return parentNodeId;
	}

	@FormParam("parent_node_id")
	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	@FormParam("display_name")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public List<String> getChildNodeIds() {
		return childNodeIds;
	}

	@FormParam("child_node_ids")
	public void setChildNodeIds(List<String> childNodeIds) {
		this.childNodeIds = childNodeIds;
	}
	
	public List<ContextNodeLdapMappingForm> getLdapMappings() {
		return ldapMappings;
	}

	@FormParam("ldap_mapping")
	public void setLdapMappings(List<ContextNodeLdapMappingForm> ldapMappings) {
		this.ldapMappings = ldapMappings;
	}
	
	public List<ContextNodeProviderMappingForm> getProviderMappings() {
		return providerMappings;
	}

	@FormParam("provider_mapping")
	public void setProviderMappings(List<ContextNodeProviderMappingForm> providerMappings) {
		this.providerMappings = providerMappings;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create_date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
