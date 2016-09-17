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
 * @author Victor
 * @since
 */
@XmlRootElement(name = "subscription_server_product")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionServerProductForm {

	private String uuid;
	@XmlElement(name = "server_id")
	@JsonProperty(value = "server_id")
	private String serverId;
	@XmlElement(name = "product_name")
	@JsonProperty(value = "product_name")
	private String productName;
	@XmlElement(name = "product_version")
	@JsonProperty(value = "product_version")
	private String productVersion;

	public SubscriptionServerProductForm() {
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

	/**
	 * @return the Server id
	 */
	public String getServerId() {
		return serverId;
	}

	/**
	 * @param orgId
	 *            the Server id
	 */
	@FormParam("server_id")
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getProductName() {
		return productName;
	}

	@FormParam("product_name")
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductVersion() {
		return productVersion;
	}

	@FormParam("product_version")
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}


}
