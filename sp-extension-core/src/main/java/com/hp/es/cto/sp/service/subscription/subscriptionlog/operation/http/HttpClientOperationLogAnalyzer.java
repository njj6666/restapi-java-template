package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.OOLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.IOperationLogAnalyzer;
import com.hp.es.cto.sp.util.StringUtil;

public class HttpClientOperationLogAnalyzer implements IOperationLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(HttpClientOperationLogAnalyzer.class);

	@Override
	public SubscriptionLog analyze(OOLog step) {
		String url = step.getStepInput("url");
		String errorMsg = step.getStepResult("errorMessage");
		String document = step.getStepResult("document");

		logger.debug("Http Client url: " + url);
		logger.debug("Http Client errorMessage: " + errorMsg);
		logger.debug("Http Client document: " + document);

		return analyze(errorMsg, document);

	}

	/**
	 * Analyze the error message or the returned document of http clients
	 * 
	 * @param errorMsg
	 * @param document
	 * @return parsed error message or document
	 */
	protected SubscriptionLog analyze(String errorMsg, String document) {
		SubscriptionLog slog = new SubscriptionLog();
		slog.setTag("HttpOperation");
		if (!StringUtil.isNullOrEmpty(errorMsg)) {
			slog.setMessage(errorMsg);
		}
		else if (!StringUtil.isNullOrEmpty(document)) {
			slog.setMessage(document);
		}
		else {
			slog.setMessage("Unknown error in Http Client operation");
		}
		return slog;
	}

}
