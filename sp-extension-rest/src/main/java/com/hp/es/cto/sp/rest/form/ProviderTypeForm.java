package com.hp.es.cto.sp.rest.form;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "provider_type")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderTypeForm {
	private String uuid;
	@XmlElement(name = "type")
	@JsonProperty(value = "type")
	private String type;
	@XmlElement(name = "sub_types")
	@JsonProperty(value = "sub_types")
	private List<ProviderSubTypeForm> subTypes;

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

	public List<ProviderSubTypeForm> getSubTypes() {
		if(subTypes==null){
			subTypes = new ArrayList<ProviderSubTypeForm>();
		}
		return subTypes;
	}

	@FormParam("sub_types")
	public void setSubTypes(List<ProviderSubTypeForm> subTypes) {
		this.subTypes = subTypes;
	}
}
