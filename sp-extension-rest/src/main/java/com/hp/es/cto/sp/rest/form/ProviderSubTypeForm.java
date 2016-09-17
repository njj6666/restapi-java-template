package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "provider_sub_type")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderSubTypeForm {
	private String uuid;
	@XmlElement(name = "type")
	@JsonProperty(value = "type")
	private String type;
	@XmlElement(name = "sub_type")
	@JsonProperty(value = "sub_type")
	private String subType;

	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	@FormParam("type")
	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	@FormParam("sub_type")
	public void setSubType(String subType) {
		this.subType = subType;
	}
}
