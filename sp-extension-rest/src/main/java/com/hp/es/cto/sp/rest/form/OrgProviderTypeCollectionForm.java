package com.hp.es.cto.sp.rest.form;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "org_provider_type_col")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgProviderTypeCollectionForm {
	private String uuid;
	@XmlElement(name = "org_id")
	@JsonProperty(value = "org_id")
	private String orgId;
	@XmlElement(name = "provider_type")
	@JsonProperty(value = "provider_type")
	private String providerType;
	@XmlElement(name = "subTypes")
	@JsonProperty(value = "subTypes")
	private List<String> subTypes;

	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProviderType() {
		return providerType;
	}

	@FormParam("provider_type")
	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}
	
	public String getOrgId() {
		return orgId;
	}

	@FormParam("org_id")
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<String> getSubTypes() {
		return subTypes;
	}

	@FormParam("subTypes")
	public void setSubTypes(List<String> subTypes) {
		this.subTypes = subTypes;
	}
}
