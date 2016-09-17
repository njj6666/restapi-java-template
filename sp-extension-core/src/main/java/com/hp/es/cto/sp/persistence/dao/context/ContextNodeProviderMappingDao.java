package com.hp.es.cto.sp.persistence.dao.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;

/**
 * Database access interface for accessing CONTEXT_NODE_PROVIDER_MAPPING table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface ContextNodeProviderMappingDao extends GenericDao<ContextNodeProviderMapping> {
	
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
	 * Finds a list of provider list by specific context node .
	 * 
	 * @param node
	 *            specific context node 
	 * @return
	 *         a list of context node mapping
	 */
	List<ContextNodeProviderMapping> findProviderByNode(ContextNode node);
}
