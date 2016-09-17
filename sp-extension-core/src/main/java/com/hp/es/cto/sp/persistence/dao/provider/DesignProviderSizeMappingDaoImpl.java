 package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.DesignProviderSizeMapping;

/**
 * Database access implementation class for accessing DESIGN_PROVIDER_SIZE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class DesignProviderSizeMappingDaoImpl extends GenericDaoImpl<DesignProviderSizeMapping> implements DesignProviderSizeMappingDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DesignProviderSizeMapping> findByDesignId(String designId) {
		return getHibernateTemplate().find("from DesignProviderSizeMapping t where t.serviceDesignId = ?", designId);
	}

}
