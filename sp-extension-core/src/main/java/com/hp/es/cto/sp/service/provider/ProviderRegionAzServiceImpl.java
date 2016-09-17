package com.hp.es.cto.sp.service.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionAzDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegionAz;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderRegionAzServiceImpl extends GenericServiceImpl<ProviderRegionAz> implements ProviderRegionAzService {
	private ProviderRegionAzDao providerRegionAzDao;

	/**
	 * @param providerRegionAzDao
	 *            the providerRegionAzDao to set
	 */
	public void setProviderRegionAzDao(ProviderRegionAzDao providerRegionAzDao) {
		this.providerRegionAzDao = providerRegionAzDao;
	}

	@Override
	public List<ProviderRegionAz> findByRegionId(String regionId) {
		String sql = "SELECT * from PROVIDER_REGION_AZ where REGION_ID = :regionId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regionId", regionId);
		List<ProviderRegionAz> providerRegion = providerRegionAzDao.queryEntityBySql(sql, params, ProviderRegionAz.class);
		return providerRegion;
	}

}
