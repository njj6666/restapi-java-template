package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;

/**
 * Database access interface for accessing SUBSCRIPTION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface SubscriptionPropertyDao extends GenericDao<SubscriptionProperty> {

	/**
	 * Finds a list of Property list by specific subscription.
	 * 
	 * @param subscription
	 *            specific subscription
	 * @return
	 *         a list of Subscription Property
	 */
	List<SubscriptionProperty> findPropertyBySubscription(Subscription subscription);
}
