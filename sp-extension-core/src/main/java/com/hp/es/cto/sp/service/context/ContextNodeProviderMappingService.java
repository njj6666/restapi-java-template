package com.hp.es.cto.sp.service.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;
import com.hp.es.cto.sp.service.GenericService;

public interface ContextNodeProviderMappingService extends GenericService<ContextNodeProviderMapping> {
	/**
	 * Finds a list of provider list by specific context node .
	 * 
	 * @param node
	 *            specific context node 
	 * @return
	 *         a list of provider id mapping
	 */
	List<ContextNodeProviderMapping> findProviderByNode(ContextNode node);
	
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
