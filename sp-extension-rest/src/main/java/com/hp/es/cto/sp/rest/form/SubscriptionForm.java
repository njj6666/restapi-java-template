package com.hp.es.cto.sp.rest.form;

import java.util.Date;
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
@XmlRootElement(name = "subscription")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionForm {

	private String uuid;
	@XmlElement(name = "name")
	@JsonProperty(value = "name")
	private String name;
	@XmlElement(name = "status")
	@JsonProperty(value = "status")
	private String status;
	@XmlElement(name = "owner_id")
	@JsonProperty(value = "owner_id")
	private String ownerId;
	@XmlElement(name = "csa_org_id")
	@JsonProperty(value = "csa_org_id")
	private String csaOrgId;
	@XmlElement(name = "context_node_id")
	@JsonProperty(value = "context_node_id")
	private String contextNodeId;
	@XmlElement(name = "project")
	@JsonProperty(value = "project")
	private String project;
	@XmlElement(name = "start_time")
	@JsonProperty(value = "start_time")
	private Date startTime;
	@XmlElement(name = "terminate_time")
	@JsonProperty(value = "terminate_time")
	private Date terminateTime;
	@XmlElement(name = "property")
	@JsonProperty(value = "property")
	private List<SubscriptionPropertyForm> propertyPairs;
	@XmlElement(name = "server")
	@JsonProperty(value = "server")
	private List<SubscriptionServerForm> serverIds;

	public SubscriptionForm() {
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
	 * @return the CSA Org id
	 */
	public String getCsaOrgId() {
		return csaOrgId;
	}

	/**
	 * @param orgId
	 *            the the CSA org id
	 */
	@FormParam("csa_org_id")
	public void setCsaOrgId(String orgId) {
		this.csaOrgId = orgId;
	}
	
	/**
	 * @return the Owner id
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            the Owner id
	 */
	@FormParam("owner_id")
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	@FormParam("name")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}

	@FormParam("status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getProject() {
		return project;
	}

	@FormParam("project")
	public void setProject(String project) {
		this.project = project;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	@FormParam("start_time")
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getTerminateTime() {
		return terminateTime;
	}

	@FormParam("terminate_time")
	public void setTerminateTime(Date terminateTime) {
		this.terminateTime = terminateTime;
	}
	
	/**
	 * @return contextNodeId
	 */
	public String getContextNodeId() {
		return contextNodeId;
	}

	/**
	 * @param contextNodeId
	 *            the contextNodeId
	 */
	@FormParam("context_node_id")
	public void setContextNodeId(String contextNodeId) {
		this.contextNodeId = contextNodeId;
	}
	
	public List<SubscriptionServerForm> getServerIds() {
		return serverIds;
	}

	@FormParam("server")
	public void setServerIds(List<SubscriptionServerForm> serverIds) {
		this.serverIds = serverIds;
	}
	
	public List<SubscriptionPropertyForm> getPropertyPairs() {
		return 	propertyPairs;
	}

	@FormParam("property")
	public void setProperties(List<SubscriptionPropertyForm> propertyPairs) {
		this.propertyPairs = propertyPairs;
	}
}
