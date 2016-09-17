package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionServerDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class SubscriptionServerServiceImpl extends
		GenericServiceImpl<SubscriptionServer> implements
		SubscriptionServerService {
	private SubscriptionServerDao subscriptionServerDao;

	public void setSubscriptionServerDao(SubscriptionServerDao subscriptionServerDao) {
		this.subscriptionServerDao = subscriptionServerDao;
	}

	@Override
	public List<SubscriptionServer> findServerBySubscription(
			Subscription subscription) {
		return subscriptionServerDao.findServerBySubscription(subscription);
	}

	@Override
	public void deleteBySubscriptionId(String subscriptionId) {
		subscriptionServerDao.deleteBySubscriptionId(subscriptionId);
	}

	@Override
	public List<SubscriptionServer> findBySubscriptionAndIp(
			String subscriptionId, String ipAddress) {
		String sql = "SELECT * from SUBSCRIPTION_SERVER where SUBSCRIPTION_ID = and IP_ADDRESS = ?";
		List<SubscriptionServer> nodes = subscriptionServerDao.queryEntityBySql(sql,
				SubscriptionServer.class, subscriptionId, ipAddress);
		return nodes;
	}
}
