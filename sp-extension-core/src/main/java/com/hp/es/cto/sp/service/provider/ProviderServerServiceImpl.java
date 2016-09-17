package com.hp.es.cto.sp.service.provider;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderServerDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderServer;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderServerServiceImpl extends GenericServiceImpl<ProviderServer> implements ProviderServerService {
	private ProviderServerDao providerServerDao;

	public void setProviderServerDao(ProviderServerDao providerServerDao) {
		this.providerServerDao = providerServerDao;
	}

	@Override
	public List<ProviderServer> findByProviderId(String iaasProviderId) {
		return providerServerDao.findByProviderId(iaasProviderId);
	}

	@Override
	public void deleteByProviderId(String iaasProviderId) {
		 providerServerDao.deleteByProviderId(iaasProviderId);
	}

	@Override
	public List<ProviderServer> findByProviderIdAndAllocated(String iaasProviderId, String isAllocated) {
		return providerServerDao.findByProviderIdAndAllocated(iaasProviderId,isAllocated);
	}

	@Override
	public List<ProviderServer> findByProviderIdAndAllocatedAndSize(
			String providerId, String isAllocated, String size) {
		return providerServerDao.findByProviderIdAndAllocatedAndSize(providerId,isAllocated,size);
	}

	@Override
	public List<ProviderServer> findByConditions(String providerId, String isAllocated, String size, String name, String osType, String ipAddress, String publicIpAddress, String privateIpAddress, String provider) {
		return providerServerDao.findByConditions(providerId, isAllocated, size, name, osType, ipAddress, publicIpAddress, privateIpAddress, provider);
	}

}
