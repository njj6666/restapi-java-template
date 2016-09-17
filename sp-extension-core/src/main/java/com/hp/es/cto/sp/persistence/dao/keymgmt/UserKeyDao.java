package com.hp.es.cto.sp.persistence.dao.keymgmt;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;

/**
 * Database access interface for accessing USER_KEY table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Dream
 */
public interface UserKeyDao extends GenericDao<UserKey> {
	/**
	 * Gets a list of UserKey by user email.
	 * 
	 * @param email
	 *            user email
	 * @return
	 *         a list of UserKey
	 */
	public List<UserKey> findByEmail(String email);
}
