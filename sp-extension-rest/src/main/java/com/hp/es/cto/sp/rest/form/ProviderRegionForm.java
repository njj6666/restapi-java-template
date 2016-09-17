package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "provider_region")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderRegionForm {
	private String uuid;
	@XmlElement(name = "provider_type_id")
	@JsonProperty(value = "provider_type_id")
	private String providerTypeId;
	@XmlElement(name = "region_name")
	@JsonProperty(value = "region_name")
	private String regionName;

	@XmlElement(name = "region_display_name")
	@JsonProperty(value = "region_display_name")
	private String regionDisplayName;
	
	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProviderTypeId() {
		return providerTypeId;
	}

	@FormParam("provider_type_id")
	public void setProviderTypeId(String providerTypeId) {
		this.providerTypeId = providerTypeId;
	}

	public String getRegionName() {
		return regionName;
	}

	@FormParam("region_name")
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public String getRegionDisplayName() {
		return regionDisplayName;
	}

	@FormParam("region_display_name")
	public void setRegionDisplayName(String regionDisplayName) {
		this.regionDisplayName = regionDisplayName;
	}
}
