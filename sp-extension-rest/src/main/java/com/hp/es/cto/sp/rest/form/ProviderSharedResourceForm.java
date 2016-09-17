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
@XmlRootElement(name = "provider_shared_resource")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderSharedResourceForm {

	private String uuid;
	@XmlElement(name = "csa_provider_id")
	@JsonProperty(value = "csa_provider_id")
	private String csaProviderId;
	/**
	 * the descriptive name for <code>ProviderServer</code> entity
	 */
	@XmlElement(name = "name")
	@JsonProperty(value = "name")
	private String name;
	@XmlElement(name = "current_connection")
	@JsonProperty(value = "current_connection")
	private Integer currentConnection;
	@XmlElement(name = "max_connection")
	@JsonProperty(value = "max_connection")
	private Integer maxConnection;
	@XmlElement(name = "create_date")
	@JsonProperty(value = "create_date")
	private Date createDate;

	public ProviderSharedResourceForm() {
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
	 * @return the CSA provider id
	 */
	public String getCsaProviderId() {
		return csaProviderId;
	}

	/**
	 * @param providerId
	 *            the the CSA provider id
	 */
	@FormParam("csa_provider_id")
	public void setCsaProviderId(String providerId) {
		this.csaProviderId = providerId;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCurrentConnection() {
		return currentConnection;
	}

	@FormParam("current_connection")
	public void setCurrentConnection(Integer currentConnection) {
		this.currentConnection = currentConnection;
	}

	public Integer getMaxConnection() {
		return maxConnection;
	}

	@FormParam("max_connection")
	public void setMaxConnection(Integer maxConnection) {
		this.maxConnection = maxConnection;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create_date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
