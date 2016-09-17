package com.hp.es.cto.sp.persistence.dao.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;

/**
 * Database access interface for accessing CONTEXT_NODE_LDAP_MAPPING table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface ContextNodeLdapMappingDao extends GenericDao<ContextNodeLdapMapping> {
	/**
	 * Finds a list of context nodes by specific ldap group DN.
	 * 
	 * @param ldapGroupDn
	 *            specific ldap group DN
	 * @return
	 *         a list of context nodes mapping
	 */
	List<ContextNodeLdapMapping> findByLdapGroupDn(String ldapGroupDn);
	
	
	/**
	 * Finds a list of context nodes by specific node
	 * 
	 * @param nodeId
	 *            context node id
	 * @return
	 *         a list of context nodes mapping
	 */
	List<ContextNodeLdapMapping> findByNodeId(String nodeId);
	
	/**
	 * remove a list of ldap mapping by specific context node id.
	 * 
	 * @param nodeId
	 *            context node id
	 * @return
	 *         whether the remove is success
	 */
	void deleteByNodeId(String nodeId);
	
	/**
	 * Finds a list of context nodes by specific node.
	 * 
	 * @param node
	 *            context node 
	 * @return
	 *         a list of context nodes mapping
	 */
	List<ContextNodeLdapMapping> findByNode(ContextNode node);
}
