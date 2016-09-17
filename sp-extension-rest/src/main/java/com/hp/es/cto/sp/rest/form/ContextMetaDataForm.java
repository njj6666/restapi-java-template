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
@XmlRootElement(name = "context_metadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextMetaDataForm {

	private String uuid;
	@XmlElement(name = "level")
	@JsonProperty(value = "level")
	private Integer level;
	@XmlElement(name = "name")
	@JsonProperty(value = "name")
	private String name;
	@XmlElement(name = "csa_org_id")
	@JsonProperty(value = "csa_org_id")
	private String csaOrgId;
	@XmlElement(name = "create_date")
	@JsonProperty(value = "create_date")
	private Date createDate;

	public ContextMetaDataForm() {
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
	 * @return the CSA Org id
	 */
	public String getCsaOrgId() {
		return csaOrgId;
	}

	/**
	 * @param orgId
	 *            the the CSA org id
	 */
	@FormParam("csa_org_id")
	public void setCsaOrgId(String orgId) {
		this.csaOrgId = orgId;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getLevel() {
		return level;
	}

	@FormParam("level")
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create_date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
