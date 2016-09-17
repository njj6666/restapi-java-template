package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.OOLog;

public interface IOperationLogAnalyzer {
	
	/**
	 * Analyze OO operation step log
	 * @param step
	 * @return analyzed OO log
	 */
	public SubscriptionLog analyze(OOLog step);

}
