package com.hp.es.cto.sp.persistence.dao.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;

/**
 * Database access implementation class for accessing CONTEXT_NODE_PROVIDER_MAPPING table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ContextNodeProviderMappingDaoImpl extends GenericDaoImpl<ContextNodeProviderMapping> implements ContextNodeProviderMappingDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextNodeProviderMapping> findProviderByNode(ContextNode node) {
		return getHibernateTemplate().find("from ContextNodeProviderMapping t where t.node = ?", node);
	}

	@Override
	public void deleteByNodeId(String nodeId) {
		String sql = "delete from CONTEXT_NODE_PROVIDER_MAPPING where CONTEXT_NODE_ID='"+nodeId+"'";
		super.excuteBySql(sql);
	}


}
