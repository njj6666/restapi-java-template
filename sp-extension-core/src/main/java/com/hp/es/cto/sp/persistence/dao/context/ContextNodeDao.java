package com.hp.es.cto.sp.persistence.dao.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;

/**
 * Database access interface for accessing CONTEXT_NODE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface ContextNodeDao extends GenericDao<ContextNode> {
	/**
	 * Finds a list of Context Nodes by Parent Node .
	 * 
	 * @param parentNode
	 *            specific parent node  
	 * @return
	 *         a list of Context Nodes
	 */
	List<ContextNode> findByParent(ContextNode parentNode);

	
	/**
	 * Finds a list of Context Nodes by Meta Data .
	 * 
	 * @param metaData
	 *            specific metaData  
	 * @return
	 *         a list of Context Nodes
	 */
	List<ContextNode> findByMetaData(ContextMetaData metaData);

	/**
	 * Finds a list of Context Nodes by Parent Node ID and ladp group dns.
	 * 
	 * @param parentNodeId
	 *            specific parent node Id 
	 * @param ldapDnList
	 *            list of GroupDN
	 * @return
	 *         a list of Context Nodes
	 */
	List<ContextNode> findAllByParentIdAndLdapDns(String parentNodeId, List<String> ldapDnList);
	
	/**
	 * Finds a list of Providers by context Node ID and ladp group dns.
	 * 
	 * @param nodeId
	 *            specific context node Id 
	 * @param ldapDnList
	 *            list of GroupDN
	 * @return
	 *         a list of Context Nodes
	 */
	List<String> findProvidersByNodeIdAndLdapDns(String nodeId, List<String> ldapDnList);
	
	/**
	 * Finds a list of Providers by context Node ID and ladp group dns.
	 * 
	 * @param nodeId
	 *            specific context node Id 
	 * @return
	 *         a list of Context Nodes
	 */
	List<String> findParentProvidersByNodeId(String nodeId);
	
	/**
	 * Finds a full context path for the specified node
	 * 
	 * @param nodeId
	 *            specific context node Id 
	 * @return
	 *         a full context path
	 */
	List<String> findContextPathByNodeId(String nodeId);
}
