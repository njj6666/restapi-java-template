package com.hp.es.cto.sp.service.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegion;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderRegionServiceImpl extends GenericServiceImpl<ProviderRegion> implements ProviderRegionService {
	private ProviderRegionDao providerRegionDao;

	/**
	 * @param providerRegionDao
	 *            the providerRegionDao to set
	 */
	public void setProviderRegionDao(ProviderRegionDao providerRegionDao) {
		this.providerRegionDao = providerRegionDao;
	}

	@Override
	public List<ProviderRegion> findByProviderTypeId(String iaasProvider) {
		String sql = "SELECT * from PROVIDER_REGION where PROVIDER_TYPE_ID = :iaasProvider ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iaasProvider", iaasProvider);
		List<ProviderRegion> providerRegion = providerRegionDao.queryEntityBySql(sql, params, ProviderRegion.class);
		return providerRegion;
	}

	@Override
	public List<ProviderRegion> findByProviderType(String providerType, String subProviderType) {
		String sql = "SELECT * from PROVIDER_REGION where PROVIDER_TYPE_ID in ( select t.UUID from PROVIDER_TYPE t WHERE t.TYPE = :pType and t.SUB_TYPE = :subType )";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pType", providerType);
		params.put("subType", subProviderType);
		List<ProviderRegion> providerRegion = providerRegionDao.queryEntityBySql(sql, params, ProviderRegion.class);
		return providerRegion;
	}

	@Override
	public List<ProviderRegion> findByProviderType(String providerType) {
		String sql = "SELECT * from PROVIDER_REGION where PROVIDER_TYPE_ID in ( select t.UUID from PROVIDER_TYPE t WHERE t.TYPE = :pType )";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pType", providerType);
		List<ProviderRegion> providerRegion = providerRegionDao.queryEntityBySql(sql, params, ProviderRegion.class);
		return providerRegion;
	}

	@Override
	public List<ProviderRegion> findByProviderTypeWithConstraint(String providerType, String subType, String orgId) {
		// get all regions
		List<ProviderRegion> providerRegions = findByProviderType(providerType, subType);
		if (providerRegions == null || providerRegions.size() <= 0) {
			return new ArrayList<ProviderRegion>();
		}

		// get regions mapped by org
		String sql1 = "SELECT * from ORG_PROVIDER_REGION where ORG_ID = :orgId and PROVIDER_REGION_ID in (SELECT UUID from PROVIDER_REGION where PROVIDER_TYPE_ID in (SELECT UUID from PROVIDER_TYPE where TYPE = :type and SUB_TYPE = :subtype))";
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orgId", orgId);
		params1.put("type", providerType);
		params1.put("subtype", subType);
		List<String> orgRegionIds = providerRegionDao.queryStringPropertyBySql(sql1, params1, "PROVIDER_REGION_ID");
		if (orgRegionIds != null && orgRegionIds.size() > 0) {
			providerRegions = filterRegionId(providerRegions, orgRegionIds);
		}

		return providerRegions;
	}
	
	private List<ProviderRegion> filterRegionId(List<ProviderRegion> providerRegions, List<String> regionIds) {
		List<ProviderRegion> returnProviderRegions = new ArrayList<ProviderRegion>();
		for (ProviderRegion region : providerRegions) {
			if (regionIds.contains(region.getUuid())) {
				returnProviderRegions.add(region);
			}
		}
		return returnProviderRegions;
	}
}
