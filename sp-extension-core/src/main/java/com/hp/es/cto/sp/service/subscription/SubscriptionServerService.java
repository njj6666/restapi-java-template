package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.service.GenericService;

public interface SubscriptionServerService extends GenericService<SubscriptionServer> {
	
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
	 * @param subscriptionId
	 */
	
	void deleteBySubscriptionId(String subscriptionId);
	
	/**
	 * find server by subscription Id and IP
	 * @param subscriptionId
	 * @param ipAddress
	 */
	List<SubscriptionServer> findBySubscriptionAndIp(String subscriptionId,
			String ipAddress);
}
