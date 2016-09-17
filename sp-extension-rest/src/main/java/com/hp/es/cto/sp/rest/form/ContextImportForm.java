package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@XmlRootElement(name = "context_import")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextImportForm {
	@XmlElement(name = "csa_org_id")
	private String csaOrgId;
	@XmlElement(name = "context_info")
	@JsonProperty(value = "context_info")
	private byte[] contextInfo;

	public String getCsaOrgId() {
		return csaOrgId;
	}

	@FormParam("csa_org_id")
	public void setCsaOrgId(String csaOrgId) {
		this.csaOrgId = csaOrgId;
	}
	
	public byte[] getContextInfo() {
		return contextInfo;
	}

	@FormParam("context_info")
	@PartType("application/octet-stream")
	public void setContextInfo(byte[] contextInfo) {
		this.contextInfo = contextInfo;
	}
}
