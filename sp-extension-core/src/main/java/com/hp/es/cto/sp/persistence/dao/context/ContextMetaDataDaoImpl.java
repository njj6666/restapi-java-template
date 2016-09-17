package com.hp.es.cto.sp.persistence.dao.context;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;

/**
 * Database access implementation class for accessing CONTEXT_META_DATA table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ContextMetaDataDaoImpl extends GenericDaoImpl<ContextMetaData>
		implements ContextMetaDataDao {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextMetaData> findByOrgId(String csaOrgId) {
		return getHibernateTemplate().find(
				"from ContextMetaData t where t.csaOrgId = ?", csaOrgId);
	}

	@Override
	public void deleteByOrgId(String csaOrgId) {
	}

	@Override
	public void deleteByContextMetaDataId(String contextMetaDataId) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContextMetaData delete(final ContextMetaData metadata) {
		/*
		 * Session session = getSession(); Object obj =
		 * session.load(ContextMetaData.class, metadata.getUuid());
		 * session.delete(obj); return null;
		 */
		@SuppressWarnings("rawtypes")
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Object groupObj = session.load(ContextMetaData.class,
						metadata.getUuid());
				session.delete(groupObj);
				return null;
			}
		};
		getHibernateTemplate().execute(callback);
		return null;
	}

}
