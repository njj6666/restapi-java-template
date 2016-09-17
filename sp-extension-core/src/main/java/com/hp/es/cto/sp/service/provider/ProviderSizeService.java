package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderSize;
import com.hp.es.cto.sp.service.GenericService;

public interface ProviderSizeService extends GenericService<ProviderSize> {
	/**
	 * Finds a list of provider sizes of specific provider type.
	 * 
	 * @param providerTypeId
	 *            specific providerTypeId
	 * @return
	 *         a list of provider sizes
	 */
	List<ProviderSize> findByProviderTypeId(String providerTypeId);
	
	/**
	 * Finds a list of provider sizes of specific provider type.
	 * 
	 * @param providerType
	 *            specific providerType
	 * @param subProviderType
	 *            specific subProviderType
	 * @return
	 *         a list of provider sizes
	 */
	List<ProviderSize> findByProviderType(String providerType, String subProviderType);
	
	/**
	 * Finds a list of provider sizes of specific provider type with organization and service design constraint.
	 * 
	 * @param providerType
	 *            specific providerType
	 * @param subProviderType
	 *            specific subProviderType
	 * @param orgId
	 *            specific organization
	 * @param designId
	 *            specific service design
	 * @return
	 *         a list of provider sizes
	 */
	List<ProviderSize> findByProviderTypeWithConstraint(String providerType, String subType, String orgId, String designId);
	
	
}
