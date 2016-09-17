package com.hp.es.cto.sp.service.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.OrgProviderTypeMappingDao;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderTypeMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class OrgProviderTypeMappingServiceImpl extends GenericServiceImpl<OrgProviderTypeMapping> implements OrgProviderTypeMappingService {
	private OrgProviderTypeMappingDao orgProviderTypeMappingDao;

	/**
	 * @param orgProviderTypeMappingDao
	 *            the orgProviderTypeMappingDao to set
	 */
	public void setOrgProviderTypeMappingDao(OrgProviderTypeMappingDao orgProviderTypeMappingDao) {
		this.orgProviderTypeMappingDao = orgProviderTypeMappingDao;
	}

	@Override
	public List<OrgProviderTypeMapping> findByOrgId(String orgId) {
		return orgProviderTypeMappingDao.findByOrgId(orgId);
	}

	@Override
	public void deleteByOrgId(String orgId) {
		orgProviderTypeMappingDao.excuteBySql("delete from ORG_PROVIDER_TYPE where ORG_ID=?", orgId);
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
		sb.append("INSERT INTO ORG_PROVIDER_TYPE (UUID, ORG_ID, PROVIDER_TYPE_ID) ");
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
		orgProviderTypeMappingDao.excuteBySql(sb.toString());
	}

	@Override
	public List<OrgProviderTypeMapping> findByOrgIdAndType(String orgId, String type) {
		// get sizes mapped by org
		String sql1 = "SELECT * from ORG_PROVIDER_TYPE where ORG_ID = :orgId and PROVIDER_TYPE_ID in (SELECT UUID from PROVIDER_TYPE where TYPE = :type)";
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orgId", orgId);
		params1.put("type", type);
		List<OrgProviderTypeMapping> orgTypes = orgProviderTypeMappingDao.queryEntityBySql(sql1, params1, OrgProviderTypeMapping.class);
		return orgTypes;
	}

	@Override
	public void deleteByOrgIdAndProviderType(String orgId, String providerType) {
		orgProviderTypeMappingDao.excuteBySql("delete from ORG_PROVIDER_TYPE where ORG_ID=? and PROVIDER_TYPE_ID in (SELECT UUID from PROVIDER_TYPE where TYPE =?)", orgId, providerType);
	}

}
