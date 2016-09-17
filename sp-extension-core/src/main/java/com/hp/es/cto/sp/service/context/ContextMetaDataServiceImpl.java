package com.hp.es.cto.sp.service.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.context.ContextMetaDataDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ContextMetaDataServiceImpl extends
		GenericServiceImpl<ContextMetaData> implements ContextMetaDataService {
	private ContextMetaDataDao contextMetaDataDao;

	public void setContextMetaDataDao(ContextMetaDataDao contextMetaDataDao) {
		this.contextMetaDataDao = contextMetaDataDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContextMetaData> findByOrgId(String orgId) {
		return contextMetaDataDao.findByOrgId(orgId);
	}

	@Override
	public void deleteByOrgId(String csaOrgId) {
		List<ContextMetaData> list = contextMetaDataDao.findByOrgId(csaOrgId);
		for(int i=0;i<list.size();i++){
			contextMetaDataDao.delete(list.get(i));
		}
	}

	@Override
	public void deleteAllRelatedContext(String contextMetaDataId) {
		// get the Meta Data with ID
		ContextMetaData contextMetaData = contextMetaDataDao
				.findById(contextMetaDataId);
		
		// get the OrgID and level
		String csaOrgId = contextMetaData.getCsaOrgId();
		int level = contextMetaData.getLevel();
		
		// get the Meta Data ID with level larger than current level
		String sql = "Select * from CONTEXT_META_DATA where CSA_ORG_ID = :orgId and level >= :level";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", csaOrgId);
		params.put("level", level);
		List<ContextMetaData> metaDataIdlist = contextMetaDataDao.queryEntityBySql(sql, params, ContextMetaData.class);
		
		for(int i=0;i<metaDataIdlist.size();i++){
			contextMetaDataDao.delete(metaDataIdlist.get(i));
		}
	}
}
