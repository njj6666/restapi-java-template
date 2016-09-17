package com.hp.es.cto.sp.service.keymgmt;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.keymgmt.UserKeyDao;
import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class UserKeyServiceImpl extends GenericServiceImpl<UserKey> implements UserKeyService {
	private UserKeyDao userKeyDao;

	public void setUserKeyDao(UserKeyDao userKeyDao) {
		this.userKeyDao = userKeyDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserKey> findByEmail(String email) {
		return userKeyDao.findByEmail(email);
	}

}
