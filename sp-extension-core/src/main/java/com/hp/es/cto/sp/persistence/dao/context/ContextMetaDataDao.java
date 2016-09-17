package com.hp.es.cto.sp.persistence.dao.context;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;

/**
 * Database access interface for accessing CONTEXT_META_DATA table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 *  @param <T>
 *            generic type for ORM entity
 * @author Victor
 */
public interface ContextMetaDataDao extends GenericDao<ContextMetaData> {
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
	 * delete a list of Context Meta Data of specific csaOrgId.
	 * 
	 * @param csaOrgId
	 *            specific csaOrgId
	 * @return
	 *        
	 */
	void deleteByOrgId(String csaOrgId);

	/**
	 * delete a list of Context Meta Data of contextMetaDataId with all sub nodes and mappings.
	 * 
	 * @param contextMetaDataId
	 *            specific contextMetaDataId
	 * @return
	 *        
	 */
	void deleteByContextMetaDataId(String contextMetaDataId);

}
