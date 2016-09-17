package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;

/**
 * Database access interface for accessing SUBSCRIPTION LOG table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface SubscriptionLogDao extends GenericDao<SubscriptionLog> {
	/**
	 * Finds a list of Log  by specific subscription.
	 * 
	 * @param subscription
	 *            specific subscription
	 * @return
	 *         a list of Subscription logs
	 */
	List<SubscriptionLog> findLogBySubscription(Subscription subscription);

}
