package com.hp.es.cto.sp.rest.form;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "design_provider_size_col")
@XmlAccessorType(XmlAccessType.FIELD)
public class DesignProviderSizeCollectionForm {
	private String uuid;
	@XmlElement(name = "design_id")
	@JsonProperty(value = "design_id")
	private String designId;
	@XmlElement(name = "provider_type_id")
	@JsonProperty(value = "provider_type_id")
	private String providerTypeId;
	@XmlElement(name = "sizes")
	@JsonProperty(value = "sizes")
	private List<String> sizes;

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

	public List<String> getSizes() {
		return sizes;
	}

	@FormParam("sizes")
	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}
	
	public String getProviderTypeId() {
		return providerTypeId;
	}

	@FormParam("provider_type_id")
	public void setProviderTypeId(String providerTypeId) {
		this.providerTypeId = providerTypeId;
	}
}
