package com.hp.es.cto.sp.rest.form;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "org_provider_region_col")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgProviderRegionCollectionForm {
	private String uuid;
	@XmlElement(name = "org_id")
	@JsonProperty(value = "org_id")
	private String orgId;
	@XmlElement(name = "provider_type_id")
	@JsonProperty(value = "provider_type_id")
	private String providerTypeId;
	@XmlElement(name = "regions")
	@JsonProperty(value = "regions")
	private List<String> regions;

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
	
	public String getOrgId() {
		return orgId;
	}

	@FormParam("org_id")
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<String> getRegions() {
		return regions;
	}

	@FormParam("regions")
	public void setRegions(List<String> regions) {
		this.regions = regions;
	}
}
