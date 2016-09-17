package com.hp.es.cto.sp.service.keymgmt;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;
import com.hp.es.cto.sp.service.GenericService;

public interface UserKeyService extends GenericService<UserKey> {
	/**
	 * Gets a list of UserKey by user email.
	 * 
	 * @param email
	 *            user email
	 * @return
	 *         a list of UserKey
	 */
	List<UserKey> findByEmail(String email);
}
