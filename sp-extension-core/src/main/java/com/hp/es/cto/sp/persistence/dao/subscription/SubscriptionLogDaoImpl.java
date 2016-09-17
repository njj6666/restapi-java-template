package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;

/**
 * Database access implementation class for accessing SUBSCRIPTION_LOG table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionLogDao
 * 
 * @author Victor
 */
public class SubscriptionLogDaoImpl extends
		GenericDaoImpl<SubscriptionLog> implements
		SubscriptionLogDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionLog> findLogBySubscription(
			Subscription subscription) {
		return getHibernateTemplate().find("from SubscriptionLog t where t.subscription = ? order by t.logTime DESC ", subscription);
	}

}
