package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;

/**
 * Database access implementation class for accessing PROVIDER_KEY table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderKeyDao
 * 
 * @author Dream
 */
public class ProviderKeyDaoImpl extends GenericDaoImpl<ProviderKey> implements ProviderKeyDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderKey> findByProviderIdAndRegion(String providerId, String region) {
		return getHibernateTemplate().find("from ProviderKey t where t.csaProviderId = ? and t.regionName = ?",
				providerId, region);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderKey> findByProviderId(String providerId) {
		return getHibernateTemplate().find("from ProviderKey t where t.csaProviderId = ?", providerId);
	}

}
