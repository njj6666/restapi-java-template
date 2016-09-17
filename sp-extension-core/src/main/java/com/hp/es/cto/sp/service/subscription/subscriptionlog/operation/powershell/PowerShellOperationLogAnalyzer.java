package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.powershell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.OOLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.IOperationLogAnalyzer;

public class PowerShellOperationLogAnalyzer implements IOperationLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(PowerShellOperationLogAnalyzer.class);

	@Override
	public SubscriptionLog analyze(OOLog step) {
		String errorMsg = step.getStepResult("returnResult");
		String host = step.getStepInput("host");

		logger.debug("PowerShell Operation host: " + host);
		logger.debug("PowerShell Operation returnResult: " + errorMsg);

		SubscriptionLog slog = new SubscriptionLog();
		if (errorMsg != null) {
			errorMsg = "Host: " + host + " " + errorMsg;
			slog.setMessage(errorMsg);
		}
		else {
			slog.setMessage("Unknow error occurs in " + host);
		}
		slog.setTag("PowerShellOperation");
		return slog;
	}

}
