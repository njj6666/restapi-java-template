package com.hp.es.cto.sp.service.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.OrgProviderRegionMappingDao;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderRegionMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class OrgProviderRegionMappingServiceImpl extends GenericServiceImpl<OrgProviderRegionMapping> implements OrgProviderRegionMappingService {
	private OrgProviderRegionMappingDao orgProviderRegionMappingDao;

	/**
	 * @param orgProviderRegionMappingDao
	 *            the orgProviderRegionMappingDao to set
	 */
	public void setOrgProviderRegionMappingDao(OrgProviderRegionMappingDao orgProviderRegionMappingDao) {
		this.orgProviderRegionMappingDao = orgProviderRegionMappingDao;
	}

	@Override
	public List<OrgProviderRegionMapping> findByOrgId(String orgId) {
		return orgProviderRegionMappingDao.findByOrgId(orgId);
	}

	@Override
	public void deleteByOrgId(String orgId) {
		orgProviderRegionMappingDao.excuteBySql("delete from ORG_PROVIDER_REGION where ORG_ID=?", orgId);
	}

	@Override
	public void createByOrgId(String orgId, List<String> sizes) {
		if (orgId == null) {
			return;
		}
		
		if (sizes == null || sizes.size()<=0) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ORG_PROVIDER_REGION (UUID, ORG_ID, PROVIDER_REGION_ID) ");
		for (int i = 0; i < sizes.size(); i++) {
			String id = UUID.randomUUID().toString();
			sb.append("SELECT '");
			sb.append(id);
			sb.append("','");
			sb.append(orgId);
			sb.append("','");
			sb.append(sizes.get(i));
			sb.append("'");
			if (i < sizes.size() - 1) {
				sb.append(" UNION ALL ");
			}
		}
		orgProviderRegionMappingDao.excuteBySql(sb.toString());
	}

	@Override
	public List<OrgProviderRegionMapping> findByOrgIdAndTypeId(String orgId, String typeId) {
		// get sizes mapped by org
		String sql1 = "SELECT * from ORG_PROVIDER_REGION where ORG_ID = :orgId and PROVIDER_REGION_ID in (SELECT UUID from PROVIDER_REGION where PROVIDER_TYPE_ID = :typeId)";
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orgId", orgId);
		params1.put("typeId", typeId);
		List<OrgProviderRegionMapping> orgRegions = orgProviderRegionMappingDao.queryEntityBySql(sql1, params1, OrgProviderRegionMapping.class);
		return orgRegions;
	}

	@Override
	public void deleteByOrgIdAndProviderId(String orgId, String providerTypeId) {
		orgProviderRegionMappingDao.excuteBySql("delete from ORG_PROVIDER_REGION where ORG_ID=? and PROVIDER_REGION_ID in (SELECT UUID from PROVIDER_REGION where PROVIDER_TYPE_ID =?)", orgId, providerTypeId);
	}

}
