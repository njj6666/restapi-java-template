package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;

/**
 * Database access interface for accessing SUBSCRIPTION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface SubscriptionServerDao extends GenericDao<SubscriptionServer> {
	/**
	 * Finds a list of Server list by specific subscription.
	 * 
	 * @param subscription
	 *            specific subscription
	 * @return
	 *         a list of Subscription Server
	 */
	List<SubscriptionServer> findServerBySubscription(Subscription subscription);

	/**
	 * delete server by subscription Id
	 * @param subscription
	 */
	void deleteBySubscriptionId(String subscriptionId);

}
