 package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderSizeMapping;

/**
 * Database access implementation class for accessing ORG_PROVIDER_SIZE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class OrgProviderSizeMappingDaoImpl extends GenericDaoImpl<OrgProviderSizeMapping> implements OrgProviderSizeMappingDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OrgProviderSizeMapping> findByOrgId(String orgId) {
		return getHibernateTemplate().find("from OrgProviderSizeMapping t where t.orgId = ?", orgId);
	}

}
