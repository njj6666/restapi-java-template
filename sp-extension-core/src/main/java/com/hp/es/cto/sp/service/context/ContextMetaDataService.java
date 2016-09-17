package com.hp.es.cto.sp.service.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.service.GenericService;

public interface ContextMetaDataService extends GenericService<ContextMetaData> {
	/**
	 * Finds a list of Context Meta Data of specific csaOrgId.
	 * 
	 * @param csaOrgId
	 *            specific csaOrgId
	 * @return
	 *         a list of Context Meta Data
	 */
	List<ContextMetaData> findByOrgId(String csaOrgId);
	
	/**
	 * remove MetaData and all below levels as well as all the context nodes and mappings 
	 * 
	 * @param contextMetaDataId
	 *            the context Meta Data ID
	 * @return
	 *        
	 */
	void deleteAllRelatedContext(String contextMetaDataId);
	
	/**
	 * Delete a list of Context Meta Data of specific csaOrgId as well as all the context nodes and mappings .
	 * 
	 * @param csaOrgId
	 *            specific csaOrgId
	 * @return
	 *         
	 */
	void deleteByOrgId(String csaOrgId);
	
}
