package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderRegionMapping;

/**
 * Database access interface for accessing ORG_PROVIDER_REGION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface OrgProviderRegionMappingDao extends GenericDao<OrgProviderRegionMapping> {
	/**
	 * Finds a list of provider Regions of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 *         a list of provider Regions
	 */
	List<OrgProviderRegionMapping> findByOrgId(String orgId);
}
