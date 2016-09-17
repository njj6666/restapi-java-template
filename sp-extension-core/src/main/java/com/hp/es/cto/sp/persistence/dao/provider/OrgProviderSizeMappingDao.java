package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderSizeMapping;

/**
 * Database access interface for accessing ORG_PROVIDER_SIZE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface OrgProviderSizeMappingDao extends GenericDao<OrgProviderSizeMapping> {
	/**
	 * Finds a list of provider sizes of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 *         a list of provider sizes
	 */
	List<OrgProviderSizeMapping> findByOrgId(String orgId);
}
