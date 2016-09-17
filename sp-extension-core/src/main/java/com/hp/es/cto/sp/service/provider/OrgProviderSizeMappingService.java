package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderSizeMapping;
import com.hp.es.cto.sp.service.GenericService;

public interface OrgProviderSizeMappingService extends GenericService<OrgProviderSizeMapping> {
	/**
	 * Finds a list of provider sizes mapping of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return a list of provider sizes
	 */
	List<OrgProviderSizeMapping> findByOrgId(String orgId);

	/**
	 * delete a list of provider sizes mapping of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 * 
	 */
	void deleteByOrgId(String orgId);

	/**
	 * create a list of provider sizes mapping of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 * 
	 */
	void createByOrgId(String orgId, List<String> sizes);

	/**
	 * Finds a list of provider sizes mapping of specific Organization and provider Type.
	 * 
	 * @param orgId
	 *            specific orgId
	 * 
	 * @param typeId
	 *            specific provider type Id
	 * 
	 * @return
	 * 
	 */
	List<OrgProviderSizeMapping> findByOrgIdAndTypeId(String orgId, String typeId);

	/**
	 * Delete a list of provider sizes mapping of specific Organization and provider Type.
	 * 
	 * @param orgId
	 *            specific orgId
	 *            
	 * @param providerTypeId
	 *            specific provider type Id
	 */
	void deleteByOrgIdAndProviderId(String orgId, String providerTypeId);
}
