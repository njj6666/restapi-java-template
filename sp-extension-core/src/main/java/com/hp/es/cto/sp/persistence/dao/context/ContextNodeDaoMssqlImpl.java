package com.hp.es.cto.sp.persistence.dao.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;

/**
 * Database access implementation class for accessing CONTEXT_NODE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ContextNodeDaoMssqlImpl extends GenericDaoImpl<ContextNode> implements ContextNodeDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextNode> findByParent(ContextNode parentNode) {
		return getHibernateTemplate().find("from ContextNode t where t.parentNode = ?", parentNode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContextNode> findByMetaData(ContextMetaData metaData) {
		return getHibernateTemplate().find("from ContextNode t where t.metaData = ?", metaData);
	}
	@Override
	public List<ContextNode> findAllByParentIdAndLdapDns(String parentNodeId, List<String> ldapDnList) {
		String sql = "With ACCESSNODE " + "as (select distinct UUID from CONTEXT_NODE  " + "WHERE UUID not in ( select distinct EG.CONTEXT_NODE_ID  from CONTEXT_NODE_LDAP_MAPPING EG) "
				+ "or UUID in (select distinct EG.CONTEXT_NODE_ID from CONTEXT_NODE_LDAP_MAPPING EG " + "WHERE EG.LDAP_DN in (:ldapdns)) ),"
				+ "CONTEXTNODE as ( select UUID,NAME,PARENT_NODE_ID from CONTEXT_NODE " + "where UUID IN (SELECT DISTINCT CONTEXT_NODE_ID FROM CONTEXT_NODE_PROVIDER_MAPPING) "
				+ "and UUID IN (select UUID from ACCESSNODE) union all  " + "select A.UUID,A.NAME,A.PARENT_NODE_ID from CONTEXT_NODE A, CONTEXTNODE B  "
				+ "where A.UUID=B.PARENT_NODE_ID and A.UUID IN (select UUID from ACCESSNODE)) " + "select * from CONTEXT_NODE C where C.PARENT_NODE_ID = :parentNode "
				+ "and C.UUID in ( select DISTINCT UUID from CONTEXTNODE) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ldapdns", ldapDnList);
		params.put("parentNode", parentNodeId);
		List<ContextNode> contextNodes = queryEntityBySql(sql, params, ContextNode.class);
		return contextNodes;
	}

	@Override
	public List<String> findProvidersByNodeIdAndLdapDns(String nodeId, List<String> ldapDnList) {
		String sql = "With ACCESSNODE " + "as (select distinct UUID from CONTEXT_NODE " + "WHERE UUID not in (select distinct EG.CONTEXT_NODE_ID  from CONTEXT_NODE_LDAP_MAPPING EG) "
				+ "or UUID in (select distinct EG.CONTEXT_NODE_ID from CONTEXT_NODE_LDAP_MAPPING EG " + "WHERE EG.LDAP_DN in (:ldapdns)) ),"
				+ "CONTEXTNODE as ( select UUID,NAME,PARENT_NODE_ID from CONTEXT_NODE where UUID = :node and UUID IN (select UUID from ACCESSNODE) "
				+ "union all select A.UUID,A.NAME,A.PARENT_NODE_ID from CONTEXT_NODE A, CONTEXTNODE B " + "where A.PARENT_NODE_ID=B.UUID and  A.UUID IN (select UUID from ACCESSNODE) ) "
				+ "select DISTINCT CSA_PROVIDER_ID from CONTEXT_NODE_PROVIDER_MAPPING C " + "where C.CONTEXT_NODE_ID IN  ( select DISTINCT UUID from CONTEXTNODE) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ldapdns", ldapDnList);
		params.put("node", nodeId);
		List<String> resourceProviderIds = queryStringPropertyBySql(sql, params, "CSA_PROVIDER_ID");
		return resourceProviderIds;
	}

	@Override
	public List<String> findParentProvidersByNodeId(String nodeId) {
		String sql = "With CONTEXTNODE as ( " + "select UUID,NAME,PARENT_NODE_ID " + "from CONTEXT_NODE where UUID=:node  " + "union all select A.UUID,A.NAME,A.PARENT_NODE_ID "
				+ "from CONTEXT_NODE A, CONTEXTNODE B  where A.UUID=B.PARENT_NODE_ID ) " + "select distinct cnpm.CSA_PROVIDER_ID from CONTEXT_NODE_PROVIDER_MAPPING cnpm "
				+ "where cnpm.CONTEXT_NODE_ID in (select UUID from  CONTEXTNODE C)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("node", nodeId);
		List<String> resourceProviderIds = queryStringPropertyBySql(sql, params, "CSA_PROVIDER_ID");
		return resourceProviderIds;
	}

	@Override
	public List<String> findContextPathByNodeId(String nodeId) {
		String sqlStr = "With CONTEXTNODE as (select UUID,NAME,PARENT_NODE_ID,CONTEXT_META_DATA_ID from CONTEXT_NODE where UUID=:node "
				+ " union all select A.UUID,A.NAME,A.PARENT_NODE_ID,A.CONTEXT_META_DATA_ID " + " from CONTEXT_NODE A, CONTEXTNODE B where A.UUID=B.PARENT_NODE_ID ) ,"
				+ " Nodepath as (select node.NAME,meta.LEVEL,meta.CSA_ORG_ID from CONTEXTNODE node, CONTEXT_META_DATA meta" + " where node.CONTEXT_META_DATA_ID=meta.UUID) "
				+ " SELECT name_path=STUFF((SELECT '---'+[NAME] FROM Nodepath t " + "WHERE csa_org_id=t1.csa_org_id order by LEVEL FOR XML PATH('')), 1, 3, '') "
				+ "FROM Nodepath t1 GROUP BY csa_org_id";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("node", nodeId);
		List<String> paths = queryStringPropertyBySql(sqlStr, params, "name_path");
		return paths;
	}
}
