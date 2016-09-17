package com.hp.es.cto.sp.service.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.OrgProviderSizeMappingDao;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderSizeMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class OrgProviderSizeMappingServiceImpl extends GenericServiceImpl<OrgProviderSizeMapping> implements OrgProviderSizeMappingService {
	private OrgProviderSizeMappingDao orgProviderSizeMappingDao;

	/**
	 * @param orgProviderSizeMappingDao
	 *            the orgProviderSizeMappingDao to set
	 */
	public void setOrgProviderSizeMappingDao(OrgProviderSizeMappingDao orgProviderSizeMappingDao) {
		this.orgProviderSizeMappingDao = orgProviderSizeMappingDao;
	}

	@Override
	public List<OrgProviderSizeMapping> findByOrgId(String orgId) {
		return orgProviderSizeMappingDao.findByOrgId(orgId);
	}

	@Override
	public void deleteByOrgId(String orgId) {
		orgProviderSizeMappingDao.excuteBySql("delete from ORG_PROVIDER_SIZE where ORG_ID=?", orgId);
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
		sb.append("INSERT INTO ORG_PROVIDER_SIZE (UUID, ORG_ID, PROVIDER_SIZE_ID) ");
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
		orgProviderSizeMappingDao.excuteBySql(sb.toString());
	}

	@Override
	public List<OrgProviderSizeMapping> findByOrgIdAndTypeId(String orgId, String typeId) {
		// get sizes mapped by org
		String sql1 = "SELECT * from ORG_PROVIDER_SIZE where ORG_ID = :orgId and PROVIDER_SIZE_ID in (SELECT UUID from PROVIDER_SIZE where PROVIDER_TYPE_ID = :typeId)";
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orgId", orgId);
		params1.put("typeId", typeId);
		List<OrgProviderSizeMapping> orgSizes = orgProviderSizeMappingDao.queryEntityBySql(sql1, params1, OrgProviderSizeMapping.class);
		return orgSizes;
	}

	@Override
	public void deleteByOrgIdAndProviderId(String orgId, String providerTypeId) {
		orgProviderSizeMappingDao.excuteBySql("delete from ORG_PROVIDER_SIZE where ORG_ID=? and PROVIDER_SIZE_ID in (SELECT UUID from PROVIDER_SIZE where PROVIDER_TYPE_ID =?)", orgId, providerTypeId);
	}

}
