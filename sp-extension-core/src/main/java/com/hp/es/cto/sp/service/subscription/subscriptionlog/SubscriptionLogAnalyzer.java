package com.hp.es.cto.sp.service.subscription.subscriptionlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.util.StringUtil;

public class SubscriptionLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(SubscriptionLogAnalyzer.class);

	/**
	 * Analyze the raw OO run log and return the unified log
	 * 
	 * @param rawLog
	 *            raw OO run log
	 * @return unified log
	 */
	public SubscriptionLog analyze(SubscriptionLog rawLog) {
		logger.info("Analyzing OO log ...");
		if (!rawLog.getMessage().startsWith("[")){
			return rawLog;
		}
		OOLog rootLog = new OOLog(rawLog.getMessage());
		OOLog lastStep = rootLog.getLastStep("0");
		String path = lastStep.getFlowName();
		SubscriptionLog parsedLog = analyzeStep(rootLog, lastStep, path);
		copyContent(rawLog, parsedLog);
		return rawLog;
	}

	private SubscriptionLog analyzeStep(OOLog rootLog, OOLog step, String path) {
		logger.info("Analyzing " + step.getFlowName());
		// Check transition description
		String transitionDescription = step.getTransitionDescription();
		if (!StringUtil.isNullOrEmpty(transitionDescription)) {
			logger.info("Transition description " + transitionDescription);
			SubscriptionLog slog = new SubscriptionLog();
			// The format of transition description is message|tag1,tag2...
			String[] logExp = transitionDescription.split("\\|");
			String logMessage = logExp[0];
			slog.setMessage(logMessage);
			if (logExp.length == 2) {
				slog.setTag(logExp[1]);
			}
			slog.setPath(path + "/" + step.getStepName());
			return slog;
		}

		// the step is a basic operation
		if (step.isOperation()) {
			logger.info(step.getFlowName()+" is an operation");
			BasicOperationLogAnalyzer basicOperationLogAnalyzer = new BasicOperationLogAnalyzer();
			return basicOperationLogAnalyzer.analyze(step, path);
		}
		// the step is a sub flow
		else {
			logger.info(step.getFlowName()+" is a sub flow");
			OOLog lastStep = rootLog.getLastStep(step.getPath());
			if (lastStep == null) {
				BasicOperationLogAnalyzer basicOperationLogAnalyzer = new BasicOperationLogAnalyzer();
				return basicOperationLogAnalyzer.analyze(step, path);
			}
			else {
				return analyzeStep(rootLog, lastStep, path + "/" + step.getStepName());
			}
		}

	}

	private void copyContent(SubscriptionLog rawLog, SubscriptionLog parsedLog) {
		rawLog.setMessage(parsedLog.getMessage());
		rawLog.setDetail(parsedLog.getDetail());
		rawLog.setPath(parsedLog.getPath());
		rawLog.setTag(parsedLog.getTag());
	}

}
