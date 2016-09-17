package com.hp.es.cto.sp.rest.form;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@XmlRootElement(name = "user-key")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserKeyForm {
	@XmlElement(name = "uuid")
	private String uuid;
	/**
	 * the descriptive name for the user public key
	 */
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "email")
	@JsonProperty(value = "email")
	private String ldapEmail;
	/**
	 * the user public key content
	 */
	@XmlElement(name = "public-key")
	@JsonProperty(value = "public-key")
	private byte[] keyFile;
	@XmlElement(name = "create-date")
	@JsonProperty(value = "create-date")
	private Date createDate;

	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getLdapEmail() {
		return ldapEmail;
	}

	@FormParam("email")
	public void setLdapEmail(String ldapEmail) {
		this.ldapEmail = ldapEmail;
	}

	public byte[] getKeyFile() {
		return keyFile;
	}

	@FormParam("public-key")
	@PartType("application/octet-stream")
	public void setKeyFile(byte[] keyFile) {
		this.keyFile = keyFile;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@FormParam("create-date")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
