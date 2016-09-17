package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;

/**
 * Database access interface for accessing PROVIDER_KEY table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Dream
 */
public interface ProviderKeyDao extends GenericDao<ProviderKey> {

	/**
	 * Gets a list of ProviderKey by CSA provider id and region.
	 * 
	 * @param providerId
	 *            CSA provider id
	 * @param region
	 *            IaaS provider region name
	 * @return
	 *         a list of ProviderKey
	 */
	List<ProviderKey> findByProviderIdAndRegion(String providerId, String region);

	/**
	 * Gets a list of ProviderKey by CSA provider id.
	 * 
	 * @param providerId
	 *            CSA provider id
	 * @return
	 *         a list of ProviderKey
	 */
	List<ProviderKey> findByProviderId(String providerId);
}
