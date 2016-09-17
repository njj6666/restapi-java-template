 package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderTypeMapping;

/**
 * Database access implementation class for accessing ORG_PROVIDER_TYPE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderTypeDao
 * 
 * @author Victor
 */
public class OrgProviderTypeMappingDaoImpl extends GenericDaoImpl<OrgProviderTypeMapping> implements OrgProviderTypeMappingDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OrgProviderTypeMapping> findByOrgId(String orgId) {
		return getHibernateTemplate().find("from OrgProviderTypeMapping t where t.orgId = ?", orgId);
	}

}
