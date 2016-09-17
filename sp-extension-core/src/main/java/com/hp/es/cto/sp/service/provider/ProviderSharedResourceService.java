package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderSharedResource;
import com.hp.es.cto.sp.service.GenericService;

public interface ProviderSharedResourceService extends GenericService<ProviderSharedResource> {
	/**
	 * Finds a list of provider resources of specific iaas provider.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @return
	 *         a list of server resource records
	 */
	List<ProviderSharedResource> findByProviderId(String iaasProviderId);
	
	/**
	 * remove a list of provider resources by specific iaas provider.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @return
	 *         whether the remove is success
	 */
	void deleteByProviderId(String iaasProviderId);
	
	/**
	 * Finds a list of provider resources of specific iaas provider with allocated status.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @param isAllocated
	 *            isAllocated status
	 * @return
	 *         a list of server resource records
	 */
	List<ProviderSharedResource> findByProviderIdAndAvailable(String iaasProviderId);

	/**
	 * Finds a list of provider resources of specific iaas provider not available status.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @param isAllocated
	 *            isAllocated status
	 * @return
	 *         a list of server resource records
	 */
	List<ProviderSharedResource> findByProviderIdAndUnvailable(String providerId);
}
