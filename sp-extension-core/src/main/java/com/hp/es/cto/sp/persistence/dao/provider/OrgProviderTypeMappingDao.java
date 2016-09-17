package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderTypeMapping;

/**
 * Database access interface for accessing ORG_PROVIDER_TYPE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface OrgProviderTypeMappingDao extends GenericDao<OrgProviderTypeMapping> {
	/**
	 * Finds a list of provider Types of specific Organization.
	 * 
	 * @param orgId
	 *            specific orgId
	 * @return
	 *         a list of provider Types
	 */
	List<OrgProviderTypeMapping> findByOrgId(String orgId);
}
