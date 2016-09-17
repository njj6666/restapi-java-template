package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.DesignProviderSizeMapping;
import com.hp.es.cto.sp.service.GenericService;

public interface DesignProviderSizeMappingService extends GenericService<DesignProviderSizeMapping> {
	/**
	 * Finds a list of provider sizes  mapping of specific Service Design.
	 * 
	 * @param designId
	 *            specific service design Id
	 * @return
	 *         a list of provider sizes
	 */
	List<DesignProviderSizeMapping> findByDesignId(String designId);
	
	/**
	 * delete a list of provider sizes  mapping of specific Service Design.
	 * 
	 * @param designId
	 *            specific service design Id
	 * @return
	 *         
	 */
	void deleteByDesignId(String designId);
	
	/**
	 * create a list of provider sizes  mapping of specific Service Design.
	 * 
	 * @param designId
	 *            specific service design Id
	 * @return
	 *         
	 */
	void createByDesignId(String designId, List<String> sizes);

	/**
	 * find a list of provider sizes  mapping of specific Service Design and provider Type.
	 * @param designId
	 * @param typeId
	 * @return
	 */
	List<DesignProviderSizeMapping> findByDesignIdAndTypeId(String designId, String typeId);

	/**
	 * delete a list of provider sizes  mapping of specific Service Design and provider Type.
	 * @param designId
	 * 
	 * @param providerTypeId
	 */
	void deleteByDesignIdAndProviderId(String designId, String providerTypeId);
	
}
