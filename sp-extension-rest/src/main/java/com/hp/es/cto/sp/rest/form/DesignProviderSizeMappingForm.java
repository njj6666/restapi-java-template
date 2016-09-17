package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "design_provider_size")
@XmlAccessorType(XmlAccessType.FIELD)
public class DesignProviderSizeMappingForm {
	private String uuid;
	@XmlElement(name = "design_id")
	@JsonProperty(value = "design_id")
	private String designId;
	@XmlElement(name = "provider_size_id")
	@JsonProperty(value = "provider_size_id")
	private String providerSizeId;

	public String getUuid() {
		return uuid;
	}

	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDesignId() {
		return designId;
	}

	@FormParam("design_id")
	public void setDesignId(String designId) {
		this.designId = designId;
	}

	public String getProviderSizeId() {
		return providerSizeId;
	}

	@FormParam("provider_size_id")
	public void setProviderSizeId(String providerSizeId) {
		this.providerSizeId = providerSizeId;
	}
}
