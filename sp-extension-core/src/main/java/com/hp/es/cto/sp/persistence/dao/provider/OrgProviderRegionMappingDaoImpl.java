 package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderRegionMapping;

/**
 * Database access implementation class for accessing ORG_PROVIDER_REGION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class OrgProviderRegionMappingDaoImpl extends GenericDaoImpl<OrgProviderRegionMapping> implements OrgProviderRegionMappingDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OrgProviderRegionMapping> findByOrgId(String orgId) {
		return getHibernateTemplate().find("from OrgProviderRegionMapping t where t.orgId = ?", orgId);
	}

}
