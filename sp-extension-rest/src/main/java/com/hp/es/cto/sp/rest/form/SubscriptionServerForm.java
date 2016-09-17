package com.hp.es.cto.sp.rest.form;

import java.util.List;

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
@XmlRootElement(name = "subscription_server")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionServerForm {

	private String uuid;
	
	@XmlElement(name = "subscription_id")
	@JsonProperty(value = "subscription_id")
	private String subscriptionId;
	
	@XmlElement(name = "size")
	@JsonProperty(value = "size")
	private String size;
	
	@XmlElement(name = "ip_address")
	@JsonProperty(value = "ip_address")
	private String ipAddress;
	
	@XmlElement(name = "public_ip_address")
	@JsonProperty(value = "public_ip_address")
	private String publicIpAddress;
	
	@XmlElement(name = "private_ip_address")
	@JsonProperty(value = "private_ip_address")
	private String privateIpAddress;
	
	@XmlElement(name = "csa_provider_id")
	@JsonProperty(value = "csa_provider_id")
	private String csaProviderId;
	
	@XmlElement(name = "iaas_provider")
	@JsonProperty(value = "iaas_provider")
	private String iaasProvider;
	
	@XmlElement(name = "dns")
	@JsonProperty(value = "dns")
	private String dns;
	
	@JsonProperty(value = "simplified_dns")
	private String simplifiedDns;
	
	@XmlElement(name = "os_type")
	@JsonProperty(value = "os_type")
	private String osType;
	
	@XmlElement(name = "region")
	@JsonProperty(value = "region")
	private String region;
	
	@XmlElement(name = "active_flag")
	@JsonProperty(value = "active_flag")
	private String activeFlag;
	
	@XmlElement(name = "product_name")
	@JsonProperty(value = "product_name")
	private List<String> productNames;
	

	public SubscriptionServerForm() {
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

	public String getOsType() {
		return osType;
	}
	@FormParam("os_type")
	public void setOsType(String osType) {
		this.osType = osType;
	}
	
	public String getRegion() {
		return region;
	}
	@FormParam("region")
	public void setRegion(String region) {
		this.region = region;
	}

	public String getActiveFlag() {
		return activeFlag;
	}
	@FormParam("active_flag")
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}


	/**
	 * @return the subscription id
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}

	/**
	 * @param subscription
	 *            subscription id
	 */
	@FormParam("subscription_id")
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	@FormParam("ip_address")
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPublicIpAddress() {
		return publicIpAddress;
	}

	@FormParam("public_ip_address")
	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	@FormParam("private_ip_address")
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}
	
	public String getSize() {
		return size;
	}
	@FormParam("size")
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getCsaProviderId() {
		return csaProviderId;
	}

	@FormParam("csa_provider_id")
	public void setCsaProviderId(String csaProviderId) {
		this.csaProviderId = csaProviderId;
	}
	
	public String getIaasProvider() {
		return iaasProvider;
	}

	@FormParam("iaas_provider")
	public void setIaasProvider(String iaasProvider) {
		this.iaasProvider = iaasProvider;
	}
	
	public String getDns() {
		return dns;
	}

	@FormParam("dns")
	public void setDns(String dns) {
		this.dns = dns;
	}
	

	public String getSimplifiedDns() {
		return simplifiedDns;
	}

	@FormParam("simplified_dns")
	public void setSimplifiedDns(String simplifiedDns) {
		this.simplifiedDns = simplifiedDns;
	}
	
	public List<String> getProductNames() {
		return productNames;
	}

	@FormParam("product_name")
	public void setProductNames(List<String> productNames) {
		this.productNames = productNames;
	}

}
