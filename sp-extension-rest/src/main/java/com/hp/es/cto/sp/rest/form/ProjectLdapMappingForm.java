package com.hp.es.cto.sp.rest.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "project-ldap")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectLdapMappingForm {

    private String uuid;
    @XmlElement(name = "type")
    @JsonProperty(value = "type")
    private String type;
    @XmlElement(name = "ldap_dn")
    @JsonProperty(value = "ldap_dn")
    private String ldapDn;
    @XmlElement(name = "project_id")
    @JsonProperty(value = "project_id")
    private String projectId;
    @XmlElement(name = "project_name")
    @JsonProperty(value = "project_name")
    private String projectName;

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    @FormParam("project_name")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    @FormParam("project_id")
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    @FormParam("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the ldapDn
     */
    public String getLdapDn() {
        return ldapDn;
    }

    /**
     * @param ldapDn the ldapDn to set
     */
    @FormParam("ldap_dn")
    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

}
