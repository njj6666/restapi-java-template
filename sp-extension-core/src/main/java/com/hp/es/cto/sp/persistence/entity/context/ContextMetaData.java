package com.hp.es.cto.sp.persistence.entity.context;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "CONTEXT_META_DATA", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "LEVEL", "CSA_ORG_ID" }),
		@UniqueConstraint(columnNames = { "NAME", "CSA_ORG_ID" })})
public class ContextMetaData {
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
	@Column(name = "LEVEL")
	private Integer level;

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return level;
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
	@Column(name = "CSA_ORG_ID")
	private String csaOrgId;

	public void setCsaOrgId(String csaOrgId) {
		this.csaOrgId = csaOrgId;
	}

	public String getCsaOrgId() {
		return csaOrgId;
	}

	/* Data Column */
	@Column(name = "CREATE_DATE")
	private Date createDate;

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@OneToMany(mappedBy = "metaData", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<ContextNode> nodes;

	public List<ContextNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<ContextNode> nodes) {
		this.nodes = nodes;
	}
}
