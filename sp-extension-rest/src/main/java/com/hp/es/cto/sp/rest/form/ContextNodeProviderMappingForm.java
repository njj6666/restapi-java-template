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
@XmlRootElement(name = "context_provider")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextNodeProviderMappingForm {

	private String uuid;
	@XmlElement(name = "context_node_id")
	@JsonProperty(value = "context_node_id")
	private String contextNodeId;
	@XmlElement(name = "csa_provider_id")
	@JsonProperty(value = "csa_provider_id")
	private String csaProviderId;
	@XmlElement(name = "create_date")
	@JsonProperty(value = "create_date")
	private Date createDate;

	public ContextNodeProviderMappingForm() {
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
	
	public String getCsaProviderId() {
		return csaProviderId;
	}

	@FormParam("csa_provider_id")
	public void setCsaProviderId(String csaProviderId) {
		this.csaProviderId = csaProviderId;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create_date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
