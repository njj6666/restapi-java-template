package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectForm {

    private String uuid;
    @XmlElement(name = "name")
    @JsonProperty(value = "name")
    private String name;
    @XmlElement(name = "csa_org_id")
    @JsonProperty(value = "csa_org_id")
    private String csaOrganizationId;
    @XmlElement(name = "notification_list")
    @JsonProperty(value = "notification_list")
    private String notificationList;

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    @FormParam("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @FormParam("name")
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * @return the notificationList
     */
    public String getNotificationList() {
        return notificationList;
    }

    /**
     * @param name the notificationList to set
     */
    @FormParam("notification_list")
    public void setNotificationList(String notificationList) {
        this.notificationList = notificationList;
    }

    @FormParam("csa_org_id")
    public void setCsaOrganizationId(String csaOrganizationId) {
        this.csaOrganizationId = csaOrganizationId;
    }

    public String getCsaOrganizationId() {
        return csaOrganizationId;
    }

}
