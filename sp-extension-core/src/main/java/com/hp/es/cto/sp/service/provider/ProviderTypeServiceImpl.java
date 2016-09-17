package com.hp.es.cto.sp.service.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.ProviderTypeDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProviderTypeServiceImpl extends GenericServiceImpl<ProviderType> implements ProviderTypeService {
	private ProviderTypeDao providerTypeDao;

	/**
	 * @param providerKeyDao
	 *            the providerKeyDao to set
	 */
	public void setProviderTypeDao(ProviderTypeDao providerTypeDao) {
		this.providerTypeDao = providerTypeDao;
	}

	@Override
	public ProviderType findByType(String type, String subType) {
		String sql = "SELECT * from PROVIDER_TYPE where TYPE = :type and SUB_TYPE = :subType ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("subType", subType);
		List<ProviderType> typeList = providerTypeDao.queryEntityBySql(sql, params, ProviderType.class);
		if (typeList != null & typeList.size() > 0) {
			return typeList.get(0);
		}
		return null;
	}

	@Override
	public List<ProviderType> findByType(String type) {
		String sql = "SELECT * from PROVIDER_TYPE where TYPE = :type ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		List<ProviderType> typeList = providerTypeDao.queryEntityBySql(sql, params, ProviderType.class);
		return typeList;
	}

	@Override
	public void deleteByType(String type) {
		try {
			List<ProviderType> types = findByType(type);
			if (types != null && types.size() > 0) {
				for (ProviderType t : types) {
					delete(t);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ProviderType> findByTypeWithConstraint(String type, String orgId) {
		// get all sub types
		List<ProviderType> providerTypes = findByType(type);
		if (providerTypes == null || providerTypes.size() <= 0) {
			return new ArrayList<ProviderType>();
		}

		// get sub types mapped by org
		String sql1 = "SELECT * from ORG_PROVIDER_TYPE where ORG_ID = :orgId and PROVIDER_TYPE_ID in (SELECT UUID from PROVIDER_TYPE where TYPE = :type )";
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orgId", orgId);
		params1.put("type", type);
		List<String> orgTypeIds = providerTypeDao.queryStringPropertyBySql(sql1, params1, "PROVIDER_TYPE_ID");
		if (orgTypeIds != null && orgTypeIds.size() > 0) {
			providerTypes = filterTypeId(providerTypes, orgTypeIds);
		}
		return providerTypes;
	}
	
	private List<ProviderType> filterTypeId(List<ProviderType> providerTypes, List<String> orgTypeIds) {
		List<ProviderType> returnProviderTypes = new ArrayList<ProviderType>();
		for (ProviderType type : providerTypes) {
			if (orgTypeIds.contains(type.getUuid())) {
				returnProviderTypes.add(type);
			}
		}
		return returnProviderTypes;
	}

}
