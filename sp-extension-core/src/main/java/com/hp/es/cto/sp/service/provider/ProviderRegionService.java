package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegion;
import com.hp.es.cto.sp.service.GenericService;

public interface ProviderRegionService extends GenericService<ProviderRegion> {
	List<ProviderRegion> findByProviderTypeId(String iaasProvider);
	
	/**
	 * Finds a list of provider regions of specific provider type.
	 * 
	 * @param providerType
	 *            specific providerType
	 * @param subProviderType
	 *            specific subProviderType
	 * @return
	 *         a list of provider regions
	 */
	List<ProviderRegion> findByProviderType(String providerType, String subProviderType);
	
	/**
	 * Finds a list of provider regions of specific provider type.
	 * 
	 * @param providerType
	 *            specific providerType
	 * @return
	 *         a list of provider regions
	 */
	List<ProviderRegion> findByProviderType(String providerType);
	
	/**
	 * Finds a list of provider regions of specific provider type with organization constraint.
	 * 
	 * @param providerType
	 *            specific providerType
	 * @param subProviderType
	 *            specific subProviderType
	 * @param orgId
	 *            specific organization
	 * @return
	 *         a list of provider regions
	 */
	List<ProviderRegion> findByProviderTypeWithConstraint(String providerType, String subType, String orgId);
	
}
