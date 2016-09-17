package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderSharedResource;

/**
 * Database access implementation class for accessing PROVIDER_RESOURCE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ProviderSharedResourceDaoImpl extends
		GenericDaoImpl<ProviderSharedResource> implements
		ProviderSharedResourceDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSharedResource> findByProviderId(String iaasProviderId) {
		return getHibernateTemplate().find(
				"from ProviderSharedResource t where t.csaProviderId = ?",
				iaasProviderId);
	}

	@Override
	public void deleteByProviderId(String iaasProviderId) {
		String sql = "delete from PROVIDER_SHARED_RESOURCE where CSA_PROVIDER_ID='"
				+ iaasProviderId + "'";
		super.excuteBySql(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSharedResource> findByProviderIdAndAvailable(
			String iaasProviderId) {
		return getHibernateTemplate()
				.find("from ProviderSharedResource t where t.csaProviderId = ? and t.currentConnection < t.maxConnection",
						iaasProviderId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSharedResource> findByProviderIdAndUnvailable(
			String iaasProviderId) {
		return getHibernateTemplate()
				.find("from ProviderSharedResource t where t.csaProviderId = ? and t.currentConnection >= t.maxConnection",
						iaasProviderId);
	}

}
