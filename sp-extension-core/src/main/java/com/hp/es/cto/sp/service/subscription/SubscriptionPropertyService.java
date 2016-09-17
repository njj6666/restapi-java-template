package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;
import com.hp.es.cto.sp.service.GenericService;

public interface SubscriptionPropertyService extends GenericService<SubscriptionProperty> {
	
	/**
	 * Finds a list of Server list by specific subscription.
	 * 
	 * @param subscription
	 *            specific subscription
	 * @return
	 *         a list of Subscription Property
	 */
	List<SubscriptionProperty> findPropertyBySubscription(Subscription subscription);
}
