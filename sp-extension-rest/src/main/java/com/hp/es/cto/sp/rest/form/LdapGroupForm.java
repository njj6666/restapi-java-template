package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "ldap-group")
@XmlAccessorType(XmlAccessType.FIELD)
public class LdapGroupForm {
	@XmlElement(name = "dn")
	@JsonProperty(value = "dn")
	@FormParam("dn")
	private String dn;
	
	@XmlElement(name = "cn")
	@JsonProperty(value = "cn")
	@FormParam("cn")
	private String cn;

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}
}
