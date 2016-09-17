package com.hp.es.cto.sp.service.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.provider.DesignProviderSizeMappingDao;
import com.hp.es.cto.sp.persistence.entity.provider.DesignProviderSizeMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class DesignProviderSizeMappingServiceImpl extends GenericServiceImpl<DesignProviderSizeMapping> implements DesignProviderSizeMappingService {
	private DesignProviderSizeMappingDao designProviderSizeMappingDao;

	/**
	 * @param DesignProviderSizeMappingDao
	 *            the DesignProviderSizeMappingDao to set
	 */
	public void setDesignProviderSizeMappingDao(DesignProviderSizeMappingDao designProviderSizeMappingDao) {
		this.designProviderSizeMappingDao = designProviderSizeMappingDao;
	}

	@Override
	public List<DesignProviderSizeMapping> findByDesignId(String designId) {
		return designProviderSizeMappingDao.findByDesignId(designId);
	}

	@Override
	public void deleteByDesignId(String designId) {
		designProviderSizeMappingDao.excuteBySql("delete from DESIGN_PROVIDER_SIZE where SERVICE_DESIGN_ID=?", designId);
	}

	@Override
	public void createByDesignId(String designId, List<String> sizes) {
		if (designId == null) {
			return;
		}
		
		if (sizes == null || sizes.size()<=0) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("Insert into DESIGN_PROVIDER_SIZE (UUID, SERVICE_DESIGN_ID, PROVIDER_SIZE_ID) ");
		for (int i = 0; i < sizes.size(); i++) {
			String id = UUID.randomUUID().toString();
			sb.append("Select '");
			sb.append(id);
			sb.append("','");
			sb.append(designId);
			sb.append("','");
			sb.append(sizes.get(i));
			sb.append("'");
			if (i < sizes.size() - 1) {
				sb.append(" UNION ALL ");
			}
		}
		designProviderSizeMappingDao.excuteBySql(sb.toString());
	}

	@Override
	public List<DesignProviderSizeMapping> findByDesignIdAndTypeId(String designId, String typeId) {
		// get sizes mapped by design
		String sql2 = "SELECT * from DESIGN_PROVIDER_SIZE where SERVICE_DESIGN_ID = :designId and PROVIDER_SIZE_ID in (SELECT UUID from PROVIDER_SIZE where PROVIDER_TYPE_ID = :typeId)";
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("designId", designId);
		params2.put("typeId", typeId);
		List<DesignProviderSizeMapping> designSizes = designProviderSizeMappingDao.queryEntityBySql(sql2, params2, DesignProviderSizeMapping.class);
		return designSizes;
	}

	@Override
	public void deleteByDesignIdAndProviderId(String designId, String providerTypeId) {
		designProviderSizeMappingDao.excuteBySql("delete from DESIGN_PROVIDER_SIZE where SERVICE_DESIGN_ID=? and PROVIDER_SIZE_ID in (SELECT UUID from PROVIDER_SIZE where PROVIDER_TYPE_ID =?)", designId, providerTypeId);
	}

}
