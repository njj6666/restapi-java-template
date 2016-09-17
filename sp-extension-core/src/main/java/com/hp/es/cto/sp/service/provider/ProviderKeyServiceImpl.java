package com.hp.es.cto.sp.service.provider;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderKeyDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderKeyServiceImpl extends GenericServiceImpl<ProviderKey> implements ProviderKeyService {

	private ProviderKeyDao providerKeyDao;

	public void setProviderKeyDao(ProviderKeyDao providerKeyDao) {
		this.providerKeyDao = providerKeyDao;
	}

	@Override
	public List<ProviderKey> findByProviderIdAndRegion(String providerId, String region) {
		return providerKeyDao.findByProviderIdAndRegion(providerId, region);
	}

	@Override
	public List<ProviderKey> findByProviderId(String providerId) {
		return providerKeyDao.findByProviderId(providerId);
	}
}
