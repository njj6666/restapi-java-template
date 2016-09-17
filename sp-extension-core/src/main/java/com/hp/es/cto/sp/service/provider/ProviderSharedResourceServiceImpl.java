package com.hp.es.cto.sp.service.provider;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderSharedResourceDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderSharedResource;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderSharedResourceServiceImpl extends
		GenericServiceImpl<ProviderSharedResource> implements
		ProviderSharedResourceService {
	private ProviderSharedResourceDao providerSharedResourceDao;

	public void setProviderSharedResourceDao(ProviderSharedResourceDao providerSharedResourceDao) {
		this.providerSharedResourceDao = providerSharedResourceDao;
	}

	@Override
	public List<ProviderSharedResource> findByProviderId(String iaasProviderId) {
		return providerSharedResourceDao.findByProviderId(iaasProviderId);
	}
	
	@Override
	public void deleteByProviderId(String iaasProviderId) {
		providerSharedResourceDao.deleteByProviderId(iaasProviderId);
	}

	@Override
	public List<ProviderSharedResource> findByProviderIdAndAvailable(
			String iaasProviderId) {
		return providerSharedResourceDao.findByProviderIdAndAvailable(iaasProviderId);
	}

	@Override
	public List<ProviderSharedResource> findByProviderIdAndUnvailable(
			String iaasProviderId) {
		return providerSharedResourceDao.findByProviderIdAndUnvailable(iaasProviderId);
	}
}
