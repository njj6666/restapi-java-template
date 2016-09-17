package com.hp.es.cto.sp.persistence.dao.provider;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;

/**
 * Database access implementation class for accessing PROVIDER_TYPE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ProviderTypeDaoImpl extends GenericDaoImpl<ProviderType> implements ProviderTypeDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public ProviderType delete(final ProviderType providerType) {
		/*
		 * Session session = getSession(); Object obj =
		 * session.load(ContextMetaData.class, metadata.getUuid());
		 * session.delete(obj); return null;
		 */
		@SuppressWarnings("rawtypes")
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Object groupObj = session.load(ProviderType.class,
						providerType.getUuid());
				session.delete(groupObj);
				return null;
			}
		};
		getHibernateTemplate().execute(callback);
		return null;
	}
}
