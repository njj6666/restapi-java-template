package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.provider.DesignProviderSizeMapping;

/**
 * Database access interface for accessing DESIGN_PROVIDER_SIZE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface DesignProviderSizeMappingDao extends GenericDao<DesignProviderSizeMapping> {
	/**
	 * Finds a list of provider sizes of specific Service Design.
	 * 
	 * @param designId
	 *            specific service design Id
	 * @return
	 *         a list of provider sizes
	 */
	List<DesignProviderSizeMapping> findByDesignId(String designId);
}
