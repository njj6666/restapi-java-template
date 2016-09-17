package com.hp.es.cto.sp.persistence.entity.subscription;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="SUBSCRIPTION_SERVER")
public class SubscriptionServer {
	/* Primary Key */
	@Id
	@Column(name = "UUID")
	private String uuid;
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuid() {
		return uuid;
	}
	
	/* Foreign Key */
	@ManyToOne
	@JoinColumn(name = "SUBSCRIPTION_ID")
	private Subscription subscription;
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	public Subscription getSubscription() {
		return subscription;
	}
	
	/* Data Column */
	@Column(name = "SIZE")
	private String size;
	public void setSize(String size) {
		this.size = size;
	}
	public String getSize() {
		return size;
	}
	
	/* Data Column */
	@Column(name = "IP_ADDRESS")
	private String ipAddress;
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	
	/* Data Column */
	@Column(name = "PRIVATE_IP_ADDRESS")
	private String privateIpAddress;
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}
	public String getPrivateIpAddress() {
		return privateIpAddress;
	}
	
	/* Data Column */
	@Column(name = "PUBLIC_IP_ADDRESS")
	private String publicIpAddress;
	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}
	public String getPublicIpAddress() {
		return publicIpAddress;
	}
	
	/* Data Column */
	@Column(name = "DNS")
	private String dns;
	public void setDns(String dns) {
		this.dns = dns;
	}
	public String getDns() {
		return dns;
	}
	
	/* Data Column */
	@Column(name = "SIMPLIFIED_DNS")
	private String simplifiedDns;
	public void setSimplifiedDns(String simplifiedDns) {
		this.simplifiedDns = simplifiedDns;
	}
	public String getSimplifiedDns() {
		return simplifiedDns;
	}
	/* Data Column */
	@Column(name = "OS_TYPE")
	private String osType;
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getOsType() {
		return osType;
	}
	/* Data Column */
	@Column(name = "REGION")
	private String region;
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRegion() {
		return region;
	}
	/* Data Column */
	@Column(name = "ACTIVE_FLAG")
	private String activeFlag;
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	/* Data Column */
	@Column(name = "CSA_PROVIDER_ID")
	private String csaProviderId;
	public void setCsaProviderId(String csaProviderId) {
		this.csaProviderId = csaProviderId;
	}
	public String getCsaProviderId() {
		return csaProviderId;
	}
	
	/* Data Column */
	@Column(name = "IAAS_PROVIDER")
	private String iaasProvider;
	public void setIaasProvider(String iaasProvider) {
		this.iaasProvider = iaasProvider;
	}
	public String getIaasProvider() {
		return iaasProvider;
	}
	
	@OneToMany(targetEntity = SubscriptionServerProduct.class, mappedBy = "server", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<SubscriptionServerProduct> products;

	public List<SubscriptionServerProduct> getProduct() {
		return products;
	}

	public void setProduct(List<SubscriptionServerProduct> products) {
		this.products = products;
	}
}
