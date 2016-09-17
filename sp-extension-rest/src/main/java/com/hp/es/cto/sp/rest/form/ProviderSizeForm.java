package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "provider_size")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderSizeForm {
	private String uuid;
	@XmlElement(name = "provider_type_id")
	@JsonProperty(value = "provider_type_id")
	private String providerTypeId;
	@XmlElement(name = "display_name")
	@JsonProperty(value = "display_name")
	private String displayName;
	@XmlElement(name = "size")
	@JsonProperty(value = "size")
	private String size;
	@XmlElement(name = "flavor")
	@JsonProperty(value = "flavor")
	private String flavor;
	@XmlElement(name = "vcpu")
	@JsonProperty(value = "vcpu")
	private String vcpu;
	@XmlElement(name = "vmemory")
	@JsonProperty(value = "vmemory")
	private String vmemory;

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

	public String getDisplayName() {
		return displayName;
	}

	@FormParam("display_name")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getSize() {
		return size;
	}

	@FormParam("size")
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getFlavor() {
		return flavor;
	}

	@FormParam("flavor")
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	
	public String getVcpu() {
		return vcpu;
	}

	@FormParam("vcpu")
	public void setVcpu(String vcpu) {
		this.vcpu = vcpu;
	}
	
	public String getVmemory() {
		return vmemory;
	}

	@FormParam("vmemory")
	public void setVmemory(String vmemory) {
		this.vmemory = vmemory;
	}
}
