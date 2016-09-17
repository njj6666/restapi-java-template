package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "org_provider_region")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgProviderRegionMappingForm {
	private String uuid;
	@XmlElement(name = "org_id")
	@JsonProperty(value = "org_id")
	private String orgId;
	@XmlElement(name = "provider_region_id")
	@JsonProperty(value = "provider_region_id")
	private String providerRegionId;

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

	public String getProviderRegionId() {
		return providerRegionId;
	}

	@FormParam("provider_region_id")
	public void setProviderRegionId(String providerRegionId) {
		this.providerRegionId = providerRegionId;
	}
}
