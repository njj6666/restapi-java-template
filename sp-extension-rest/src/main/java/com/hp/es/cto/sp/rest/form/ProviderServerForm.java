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
@XmlRootElement(name = "provider-server")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderServerForm {

	private String uuid;
	@XmlElement(name = "csa-provider-id")
	@JsonProperty(value = "csa-provider-id")
	private String csaProviderId;
	/**
	 * the descriptive name for <code>ProviderServer</code> entity
	 */
	@XmlElement(name = "name")
	@JsonProperty(value = "name")
	private String name;
	@XmlElement(name = "size")
	@JsonProperty(value = "size")
	private String size;
	@XmlElement(name = "provider")
	@JsonProperty(value = "provider")
	private String provider;
	@XmlElement(name = "is-allocated")
	@JsonProperty(value = "is-allocated")
	private String isAllocated;
	@XmlElement(name = "os-type")
	@JsonProperty(value = "os-type")
	private String osType;
	@XmlElement(name = "ip-address")
	@JsonProperty(value = "ip-address")
	private String ipAddress;
	@XmlElement(name = "public-ip-address")
	@JsonProperty(value = "public-ip-address")
	private String publicIpAddress;
	@XmlElement(name = "private-ip-address")
	@JsonProperty(value = "private-ip-address")
	private String privateIpAddress;
	@XmlElement(name = "create-date")
	@JsonProperty(value = "create-date")
	private Date createDate;

	public ProviderServerForm() {
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

	public String getSize() {
		return size;
	}
	@FormParam("size")
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	@FormParam("provider")
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the isAllocated
	 */
	public String getIsAllocated() {
		return isAllocated;
	}

	/**
	 * @param isAllocated
	 *            the isAllocated to set
	 */
	@FormParam("is-allocated")
	public void setIsAllocated(String isAllocated) {
		this.isAllocated = isAllocated;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getOsType() {
		return osType;
	}

	@FormParam("os-type")
	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	@FormParam("ip-address")
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPublicIpAddress() {
		return publicIpAddress;
	}

	@FormParam("public-ip-address")
	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	@FormParam("private-ip-address")
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create-date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
