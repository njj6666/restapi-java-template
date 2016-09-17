package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;

/**
 * Database access implementation class for accessing SUBSCRIPTION_SERVER table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class SubscriptionServerDaoImpl extends
		GenericDaoImpl<SubscriptionServer> implements
		SubscriptionServerDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionServer> findServerBySubscription(
			Subscription subscription) {
		return getHibernateTemplate().find("from SubscriptionServer t where t.subscription = ?", subscription);
	}

	@Override
	public void deleteBySubscriptionId(String subscriptionId) {
		String sql = "delete from SUBSCRIPTION_SERVER where SUBSCRIPTION_ID='"
				+ subscriptionId + "'";
		super.excuteBySql(sql);
	}
}
