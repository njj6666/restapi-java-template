package com.hp.es.cto.sp.persistence.entity.subscription;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="SUBSCRIPTION")
public class Subscription {
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
	
	/* Data Column */
	@Column(name = "NAME")
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	/* Data Column */
	@Column(name = "STATUS")
	private String status;
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	
	/* Data Column */
	@Column(name = "OWNER_ID")
	private String ownerId;
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	
	/* Data Column */
	@Column(name = "CSA_ORG_ID")
	private String csaOrgId;
	public void setCsaOrgId(String csaOrgId) {
		this.csaOrgId = csaOrgId;
	}
	public String getCsaOrgId() {
		return csaOrgId;
	}
	
	/* Data Column */
	@Column(name = "PROJECT")
	private String project;
	public void setProject(String project) {
		this.project = project;
	}
	public String getProject() {
		return project;
	}
	
	/* Data Column */
	@Column(name = "CONTEXT_NODE_ID")
	private String contextNodeId;
	public void setContextNodeId(String contextNodeId) {
		this.contextNodeId = contextNodeId;
	}
	public String getContextNodeId() {
		return contextNodeId;
	}
	
	/* Data Column */
	@Column(name = "START_TIME")
	private Date startTime;
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	/* Data Column */
	@Column(name = "TERMINATE_TIME")
	private Date terminateTime;
	public void setTerminateTime(Date terminateTime) {
		this.terminateTime = terminateTime;
	}
	public Date getTerminateTime() {
		return terminateTime;
	}
	
	@OneToMany(targetEntity = SubscriptionProperty.class, mappedBy = "subscription", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<SubscriptionProperty> subscriptionProperties;

	public List<SubscriptionProperty> getSubscriptionProperty() {
		return subscriptionProperties;
	}

	public void setSubscriptionProperty(List<SubscriptionProperty> subscriptionProperties) {
		this.subscriptionProperties = subscriptionProperties;
	}

	@OneToMany(targetEntity = SubscriptionServer.class, mappedBy = "subscription", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<SubscriptionServer> subscriptionServers;

	public List<SubscriptionServer> getSubscriptionServer() {
		return subscriptionServers;
	}

	public void setSubscriptionServer(List<SubscriptionServer> subscriptionServers) {
		this.subscriptionServers = subscriptionServers;
	}
	
	@OneToMany(targetEntity = SubscriptionLog.class, mappedBy = "subscription", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<SubscriptionLog> subscriptionLogs;

	public List<SubscriptionLog> getSubscriptionLog() {
		return subscriptionLogs;
	}

	public void setSubscriptionLog(List<SubscriptionLog> subscriptionLogs) {
		this.subscriptionLogs = subscriptionLogs;
	}
	
}
