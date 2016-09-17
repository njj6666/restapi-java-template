package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Object for REST request/response
 * 
 * @author Robin
 * @since
 */
@XmlRootElement(name = "product_version")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductVersionForm {

	private String uuid;
	@XmlElement(name = "full_version")
	@JsonProperty(value = "full_version")
	private String fullVersion;
	@XmlElement(name = "product_name")
	@JsonProperty(value = "product_name")
	private String productName;
	@XmlElement(name = "default_flag")
	@JsonProperty(value = "default_flag")
	private String defaultFlag;
	@XmlElement(name = "url")
	@JsonProperty(value = "url")
	private String url;
	@XmlElement(name = "primary_version")
	@JsonProperty(value = "primary_version")
	private String primaryVersion;
	
	
	
	public ProductVersionForm() {
	}

	/**
	 * @return the unique id of the record
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the unique id of the record, not required if create new <code>ProviderKey</code>
	 */
	@FormParam("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getProductName() {
		return productName;
	}

	@FormParam("product_name")
	public void setProductName(String productName) {
		this.productName = productName;
	}
	

	public String getFullVersion() {
		return fullVersion;
	}

	@FormParam("full_version")
	public void setFullVersion(String fullVersion) {
		this.fullVersion = fullVersion;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	@FormParam("default_flag")
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getUrl() {
		return url;
	}

	@FormParam("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public String getPrimaryVersion() {
		return primaryVersion;
	}

	@FormParam("primary_version")
	public void setPrimaryVersion(String primaryVersion) {
		this.primaryVersion = primaryVersion;
	}
	
	


}
