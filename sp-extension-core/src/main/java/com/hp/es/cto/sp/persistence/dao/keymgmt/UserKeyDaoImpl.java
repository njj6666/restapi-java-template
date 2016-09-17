package com.hp.es.cto.sp.persistence.dao.keymgmt;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;

/**
 * Database access implementation class for accessing USER_KEY table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.keymgmt.UserKeyDao
 * 
 * @author Dream
 */
public class UserKeyDaoImpl extends GenericDaoImpl<UserKey> implements UserKeyDao {
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserKey> findByEmail(String email) {
		return getHibernateTemplate().find("from UserKey t where t.ldapEmail = ?", email);
	}
}
