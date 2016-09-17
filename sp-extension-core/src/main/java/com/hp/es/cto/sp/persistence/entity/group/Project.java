package com.hp.es.cto.sp.persistence.entity.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PROJECT", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"NAME", "CSA_ORGANIZATION_ID" }) })
public class Project {
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
	@Column(name = "NOTIFICATION_LIST")
	private String notificationList;

	public void setNotificationList(String notificationList) {
		this.notificationList = notificationList;
	}

	public String getNotificationList() {
		return notificationList;
	}

	/* Data Column */
	@Column(name = "CSA_ORGANIZATION_ID")
	private String csaOrganizationId;

	public void setCsaOrganizationId(String csaOrganizationId) {
		this.csaOrganizationId = csaOrganizationId;
	}

	public String getCsaOrganizationId() {
		return csaOrganizationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [uuid=" + uuid + ", name=" + name
				+ ", csaOrganizationId=" + csaOrganizationId + "]";
	}

}
