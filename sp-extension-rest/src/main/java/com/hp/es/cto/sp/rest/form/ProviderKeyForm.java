package com.hp.es.cto.sp.rest.form;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * Object for REST request/response
 * 
 * @author tanxu
 * @date Dec 11, 2013
 * @since
 */
@XmlRootElement(name = "provider-key")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderKeyForm {

	private String uuid;
	@XmlElement(name = "csa-provider-id")
	@JsonProperty(value = "csa-provider-id")
	private String csaProviderId;
	@XmlElement(name = "region-name")
	@JsonProperty(value = "region-name")
	private String regionName;
	@XmlElement(name = "region_id")
	@JsonProperty(value = "region_id")
	private String regionId;
	private String authType;
	/**
	 * the user name for SSH login
	 */
	private String username;
	private String password;
	/**
	 * the descriptive name for <code>ProviderKey</code> entity
	 */
	private String name;
	/**
	 * the private key of the provider for SSH login
	 */
	@XmlElement(name = "key-file")
	@JsonProperty(value = "key-file")
	private byte[] keyFile;
	@XmlElement(name = "create-date")
	@JsonProperty(value = "create-date")
	private Date createDate;

	public ProviderKeyForm() {
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
	@FormParam("csa-provider-id")
	public void setCsaProviderId(String providerId) {
		this.csaProviderId = providerId;
	}

	public String getAuthType() {
		return authType;
	}
	@FormParam("auth-type")
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	@FormParam("username")
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	@FormParam("password")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}

	public byte[] getKeyFile() {
		return keyFile;
	}

	@FormParam("key-file")
	@PartType("application/octet-stream")
	public void setKeyFile(byte[] privateKey) {
		this.keyFile = privateKey;
	}

	public String getRegionName() {
		return regionName;
	}

	@FormParam("region-name")
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public String getRegionId() {
		return regionId;
	}

	@FormParam("region_id")
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create-date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
