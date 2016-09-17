package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegionAz;
import com.hp.es.cto.sp.service.GenericService;

public interface ProviderRegionAzService extends GenericService<ProviderRegionAz> {
	/**
	 * Finds a list of provider regions AZ of specific region.
	 * 
	 * @param regionId
	 *            specific region
	 * @return
	 *         a list of provider region AZs
	 */
	List<ProviderRegionAz> findByRegionId(String regionId);
}
