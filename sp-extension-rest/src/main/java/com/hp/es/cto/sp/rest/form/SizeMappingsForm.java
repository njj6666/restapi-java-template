package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "org_provider_size")
@XmlAccessorType(XmlAccessType.FIELD)
public class SizeMappingsForm {
	private String uuid;
	@XmlElement(name = "org_id")
	@JsonProperty(value = "org_id")
	private String orgId;
	@XmlElement(name = "provider_size")
	@JsonProperty(value = "provider_size")
	private String providerSize;

	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOrgId() {
		return orgId;
	}

	@FormParam("org_id")
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getProviderSize() {
		return providerSize;
	}

	@FormParam("provider_size")
	public void setProviderSize(String providerSize) {
		this.providerSize = providerSize;
	}
}
