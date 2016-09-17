package com.hp.es.cto.sp.service.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderSizeDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderSize;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderSizeServiceImpl extends GenericServiceImpl<ProviderSize> implements ProviderSizeService {
	private ProviderSizeDao providerSizeDao;

	/**
	 * @param providerSizeDao
	 *            the providerSizeDao to set
	 */
	public void setProviderSizeDao(ProviderSizeDao providerSizeDao) {
		this.providerSizeDao = providerSizeDao;
	}

	@Override
	public List<ProviderSize> findByProviderTypeId(String providerTypeId) {
		String sql = "SELECT * from PROVIDER_SIZE where PROVIDER_TYPE_ID = :iaasProvider ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iaasProvider", providerTypeId);
		List<ProviderSize> providerSize = providerSizeDao.queryEntityBySql(sql, params, ProviderSize.class);
		return providerSize;
	}

	@Override
	public List<ProviderSize> findByProviderType(String providerType, String subProviderType) {
		String sql = "SELECT * from PROVIDER_SIZE where PROVIDER_TYPE_ID in ( select T.UUID from PROVIDER_TYPE T WHERE T.TYPE = :type and T.SUB_TYPE = :subType )";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", providerType);
		params.put("subType", subProviderType);
		List<ProviderSize> providerSizes = providerSizeDao.queryEntityBySql(sql, params, ProviderSize.class);
		return providerSizes;
	}

	@Override
	public List<ProviderSize> findByProviderTypeWithConstraint(String providerType, String subType, String orgId, String designId) {
		// get all sizes
		List<ProviderSize> providerSizes = findByProviderType(providerType, subType);
		if (providerSizes == null || providerSizes.size() <= 0) {
			return new ArrayList<ProviderSize>();
		}

		// get sizes mapped by org
		String sql1 = "SELECT * from ORG_PROVIDER_SIZE where ORG_ID = :orgId and PROVIDER_SIZE_ID in (SELECT UUID from PROVIDER_SIZE where PROVIDER_TYPE_ID in (SELECT UUID from PROVIDER_TYPE where TYPE = :type and SUB_TYPE = :subtype))";
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orgId", orgId);
		params1.put("type", providerType);
		params1.put("subtype", subType);
		List<String> orgSizeIds = providerSizeDao.queryStringPropertyBySql(sql1, params1, "PROVIDER_SIZE_ID");
		if (orgSizeIds != null && orgSizeIds.size() > 0) {
			providerSizes = filterSizeId(providerSizes, orgSizeIds);
		}

		// get sizes mapped by design
		String sql2 = "SELECT * from DESIGN_PROVIDER_SIZE where SERVICE_DESIGN_ID = :designId and PROVIDER_SIZE_ID in (SELECT UUID from PROVIDER_SIZE where PROVIDER_TYPE_ID in (SELECT UUID from PROVIDER_TYPE where TYPE = :type and SUB_TYPE = :subtype))";
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("designId", designId);
		params2.put("type", providerType);
		params2.put("subtype", subType);
		List<String> designSizeIds = providerSizeDao.queryStringPropertyBySql(sql2, params2, "PROVIDER_SIZE_ID");
		if (designSizeIds != null && designSizeIds.size() > 0) {
			providerSizes = filterSizeId(providerSizes, designSizeIds);
		}
		return providerSizes;
	}

	private List<ProviderSize> filterSizeId(List<ProviderSize> providerSizes, List<String> sizeIds) {
		List<ProviderSize> returnProviderSizes = new ArrayList<ProviderSize>();
		for (ProviderSize size : providerSizes) {
			if (sizeIds.contains(size.getUuid())) {
				returnProviderSizes.add(size);
			}
		}
		return returnProviderSizes;
	}

}
