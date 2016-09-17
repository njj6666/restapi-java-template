package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.OOLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.IOperationLogAnalyzer;

public class SSHOperationLogAnalyzer implements IOperationLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(SSHOperationLogAnalyzer.class);

	@Override
	public SubscriptionLog analyze(OOLog step) {
		String errorMsg = step.getStepResult("returnStderr");
		String host = step.getStepInput("hostname");
		
		logger.debug("SSH Operation host: " + host);
		logger.debug("SSH Operation returnResult: " + errorMsg);
		
		SubscriptionLog slog = new SubscriptionLog();
		if (errorMsg != null) {
			errorMsg = "Host: " + host + " " + errorMsg;
			slog.setMessage(errorMsg);
		}
		else {
			slog.setMessage("Unknow error occurs in " + host);
		}
		slog.setTag("SSHOperation");
		return slog;
	}

}
