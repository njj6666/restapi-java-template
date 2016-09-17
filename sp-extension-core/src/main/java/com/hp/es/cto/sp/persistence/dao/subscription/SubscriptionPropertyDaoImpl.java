package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;

/**
 * Database access implementation class for accessing PROVIDER_RESOURCE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class SubscriptionPropertyDaoImpl extends
		GenericDaoImpl<SubscriptionProperty> implements
		SubscriptionPropertyDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionProperty> findPropertyBySubscription(
			Subscription subscription) {
		return getHibernateTemplate().find("from SubscriptionProperty t where t.subscription = ?", subscription);
	}

}
