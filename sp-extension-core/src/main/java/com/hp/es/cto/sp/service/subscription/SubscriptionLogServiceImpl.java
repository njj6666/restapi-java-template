package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionLogDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.GenericServiceImpl;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.SubscriptionLogAnalyzer;

@Named
public class SubscriptionLogServiceImpl extends GenericServiceImpl<SubscriptionLog> implements SubscriptionLogService {
	private SubscriptionLogDao subscriptionLogDao;

	public void setSubscriptionLogDao(SubscriptionLogDao subscriptionLogDao) {
		this.subscriptionLogDao = subscriptionLogDao;
	}

	@Override
	public List<SubscriptionLog> findLogBySubscription(Subscription subscription) {
		return subscriptionLogDao.findLogBySubscription(subscription);
	}

	@Override
	public SubscriptionLog create(SubscriptionLog subscriptionLog) {
		SubscriptionLogAnalyzer analyzer = new SubscriptionLogAnalyzer();
		SubscriptionLog parsedLog = analyzer.analyze(subscriptionLog);
		return subscriptionLogDao.create(parsedLog);
	}
}
