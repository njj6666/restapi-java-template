package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionPropertyDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class SubscriptionPropertyServiceImpl extends
		GenericServiceImpl<SubscriptionProperty> implements
		SubscriptionPropertyService {
	private SubscriptionPropertyDao subscriptionPropertyDao;

	public void setSubscriptionPropertyDao(SubscriptionPropertyDao subscriptionPropertyDao) {
		this.subscriptionPropertyDao = subscriptionPropertyDao;
	}

	@Override
	public List<SubscriptionProperty> findPropertyBySubscription(
			Subscription subscription) {
		return subscriptionPropertyDao.findPropertyBySubscription(subscription);
	}

	

}
