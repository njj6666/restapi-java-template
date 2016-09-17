package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.sitescope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.OOLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.IOperationLogAnalyzer;

public class SitescopeOperationLogAnalyzer implements IOperationLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(SitescopeOperationLogAnalyzer.class);

	@Override
	public SubscriptionLog analyze(OOLog step) {
		String errorMsg = step.getStepResult("returnResult");
		String template = step.getStepInput("source");
		String variables = step.getStepInput("variables");

		logger.debug("Sitescope Operation template: " + template);
		logger.debug("Sitescope Operation variables: " + variables);
		logger.debug("Sitescope Operation returnResult: " + errorMsg);

		SubscriptionLog slog = new SubscriptionLog();
		if (errorMsg != null) {
			slog.setMessage(errorMsg);
		}
		else {
			slog.setMessage("Unknow error occurs when deploying template " + template + " with variables " + variables);
		}
		slog.setTag("SiteScope");
		return slog;
	}

}
