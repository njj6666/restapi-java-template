package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderRegionMapping;
import com.hp.es.cto.sp.service.GenericService;

public interface OrgProviderRegionMappingService extends GenericService<OrgProviderRegionMapping> {
	/**
	 * Finds a list of provider Regions mapping of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return a list of provider Regions
	 */
	List<OrgProviderRegionMapping> findByOrgId(String orgId);

	/**
	 * delete a list of provider Regions mapping of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 * 
	 */
	void deleteByOrgId(String orgId);

	/**
	 * create a list of provider Regions mapping of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 * 
	 */
	void createByOrgId(String orgId, List<String> Regions);

	/**
	 * Finds a list of provider Regions mapping of specific Organization and provider Type.
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
	List<OrgProviderRegionMapping> findByOrgIdAndTypeId(String orgId, String typeId);

	/**
	 * Delete a list of provider Regions mapping of specific Organization and provider Type.
	 * 
	 * @param orgId
	 *            specific orgId
	 *            
	 * @param providerTypeId
	 *            specific provider type Id
	 */
	void deleteByOrgIdAndProviderId(String orgId, String providerTypeId);
}
