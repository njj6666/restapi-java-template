package com.hp.es.cto.sp.persistence.dao.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;

/**
 * Database access implementation class for accessing CONTEXT_NODE_LDAP_MAPPING table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ContextNodeLdapMappingDaoImpl extends GenericDaoImpl<ContextNodeLdapMapping> implements ContextNodeLdapMappingDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextNodeLdapMapping> findByLdapGroupDn(String ldapGroupDn) {
		return getHibernateTemplate().find("from ContextNodeLdapMapping t where t.ldapGroupDn = ?", ldapGroupDn);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextNodeLdapMapping> findByNodeId(String nodeId) {
		return getHibernateTemplate().find("from ContextNodeLdapMapping t where t.contextNodeId = ?", nodeId);
	}

	@Override
	public void deleteByNodeId(String nodeId) {
		String sql = "delete from CONTEXT_NODE_LDAP_MAPPING where CONTEXT_NODE_ID='"+nodeId+"'";
		super.excuteBySql(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContextNodeLdapMapping> findByNode(ContextNode node) {
		return getHibernateTemplate().find("from ContextNodeLdapMapping t where t.node = ?", node);
	}

}
