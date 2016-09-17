package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "provider_region_az")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderRegionAzForm {
	private String uuid;
	@XmlElement(name = "provider_region_id")
	@JsonProperty(value = "provider_region_id")
	private String providerRegionId;
	@XmlElement(name = "az")
	@JsonProperty(value = "az")
	private String az;

	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProviderRegionId() {
		return providerRegionId;
	}

	@FormParam("provider_region_id")
	public void setProviderRegionId(String providerRegionId) {
		this.providerRegionId = providerRegionId;
	}

	public String getAz() {
		return az;
	}

	@FormParam("az")
	public void setAz(String az) {
		this.az = az;
	}
}
