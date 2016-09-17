package com.hp.es.cto.sp.persistence.entity.context;

import java.util.Date;
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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "CONTEXT_NODE", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "NAME", "PARENT_NODE_ID", "CONTEXT_META_DATA_ID" }),
		@UniqueConstraint(columnNames = { "DISPLAY_NAME", "PARENT_NODE_ID", "CONTEXT_META_DATA_ID" }) })
public class ContextNode {
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
	@JoinColumn(name = "CONTEXT_META_DATA_ID")
	private ContextMetaData metaData;

	public void setMetaData(ContextMetaData metaData) {
		this.metaData = metaData;
	}

	public ContextMetaData getMetaData() {
		return metaData;
	}

	@OneToMany(mappedBy = "node", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ContextNodeProviderMapping> nodeProviders;

	public List<ContextNodeProviderMapping> getNodeProviders() {
		return nodeProviders;
	}

	public void setNodeProviders(List<ContextNodeProviderMapping> nodeProviders) {
		this.nodeProviders = nodeProviders;
	}

	@OneToMany(mappedBy = "node", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ContextNodeLdapMapping> nodeLdaps;

	public List<ContextNodeLdapMapping> getNodeLdaps() {
		return nodeLdaps;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_NODE_ID")
	private ContextNode parentNode;

	public ContextNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(ContextNode node) {
		this.parentNode = node;
	}

	@OneToMany(targetEntity = ContextNode.class, cascade = { CascadeType.ALL }, mappedBy = "parentNode", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ContextNode> childNodes;

	public List<ContextNode> getChildNodes() {
		return childNodes;
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

	public void setNodeLdaps(List<ContextNodeLdapMapping> nodeLdaps) {
		this.nodeLdaps = nodeLdaps;
	}

	/* Data Column */
	@Column(name = "DISPLAY_NAME")
	private String displayName;

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
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
}
