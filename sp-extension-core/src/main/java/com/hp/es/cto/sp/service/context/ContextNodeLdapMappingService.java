package com.hp.es.cto.sp.service.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;
import com.hp.es.cto.sp.service.GenericService;

public interface ContextNodeLdapMappingService extends GenericService<ContextNodeLdapMapping> {
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
	 * Finds a list of context nodes by specific ldap group DN.
	 * 
	 * @param nodeId
	 *            context node id
	 * @return
	 *         a list of context nodes mapping
	 */
	List<ContextNodeLdapMapping> findByNode(ContextNode node);
	
	/**
	 * remove a list of ldap mapping by specific context node id.
	 * 
	 * @param nodeId
	 *            context node id
	 * @return
	 *         whether the remove is success
	 */
	void deleteByNodeId(String nodeId);
	
}
