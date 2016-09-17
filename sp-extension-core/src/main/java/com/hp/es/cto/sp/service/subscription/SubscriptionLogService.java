package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.GenericService;

public interface SubscriptionLogService extends GenericService<SubscriptionLog> {
	
	/**
	 * Finds a list of Logs by specific subscription.
	 * 
	 * @param subscription
	 *            specific subscription
	 * @return
	 *         a list of Subscription log
	 */
	List<SubscriptionLog> findLogBySubscription(Subscription subscription);

}
