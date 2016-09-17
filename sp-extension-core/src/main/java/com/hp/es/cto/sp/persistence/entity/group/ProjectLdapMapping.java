package com.hp.es.cto.sp.persistence.entity.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECT_LDAP_MAPPING")
public class ProjectLdapMapping {
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
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    /* Data Column */
    @Column(name = "LDAP_DN")
    private String ldapDn;

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    /* Data Column */
    @Column(name = "TYPE")
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ProjectLdapMapping [uuid=" + uuid + ", project=" + project + ", ldapDn=" + ldapDn + ", type=" + type
                + "]";
    }

}
