package com.hp.es.cto.sp.service.subscription.subscriptionlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.matching.HttpClientOperationSignature;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.matching.OperationMatcher;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.matching.PowershellOperationSignature;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.matching.SSHOperationSignature;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.matching.SitescopeOperationSignature;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.IOperationLogAnalyzer;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.http.CSAOperationLogAnalyzer;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.http.HPCSOperationLogAnalyzer;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.http.HttpClientOperationLogAnalyzer;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.powershell.PowerShellOperationLogAnalyzer;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.sitescope.SitescopeOperationLogAnalyzer;
import com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.ssh.SSHOperationLogAnalyzer;

/**
 * This class is responsible for matching operation signature and
 * delegating analysis to the appropriate analyzer.
 * 
 * Currently the pattern to be matched is hard-coded.
 * 
 * @author Pinghua
 * 
 */
public class BasicOperationLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(BasicOperationLogAnalyzer.class);

	/**
	 * Analyze the result of the basic operation
	 * 
	 * @param step
	 *            OO run log of a step
	 * @param path
	 *            path of the parent flow
	 * @return parsed log
	 */
	public SubscriptionLog analyze(OOLog step, String path) {
		String stepName = step.getStepName();
		logger.info("Analyze log of operation " + stepName);
		SubscriptionLog slog = null;
		IOperationLogAnalyzer operationAnalyzer = null;
		OperationMatcher matcher = new OperationMatcher();
		if (matcher.match(step, new SSHOperationSignature())) {
			operationAnalyzer = new SSHOperationLogAnalyzer();
			slog = operationAnalyzer.analyze(step);
			slog.setTag("SPAF");
		}
		else if (matcher.match(step, new PowershellOperationSignature())) {
			operationAnalyzer = new PowerShellOperationLogAnalyzer();
			slog = operationAnalyzer.analyze(step);
			slog.setTag("SPAF");
		}
		else if (matcher.match(step, new HttpClientOperationSignature())) {
			slog = analyzeHttpOperation(step);
		}
		else if (matcher.match(step, new SitescopeOperationSignature())) {
			operationAnalyzer = new SitescopeOperationLogAnalyzer();
			slog = operationAnalyzer.analyze(step);
		}
		else {
			slog = new SubscriptionLog();
			slog.setTag("UnsupportedOperation");
			slog.setMessage(step.getStepResult("returnResult"));
		}

		slog.setPath(path + "/" + step.getStepName());

		// Set exception as detailed error message
		String stepException = step.getStepException();
		if (stepException != null) {
			slog.setDetail(stepException);
		}
		return slog;
	}

	private SubscriptionLog analyzeHttpOperation(OOLog step) {
		String url = step.getStepInput("url");
		logger.debug("url: "+url);
		if (url.contains("csa")) {
			IOperationLogAnalyzer operationAnalyzer = new CSAOperationLogAnalyzer();
			return operationAnalyzer.analyze(step);
		}
		else if (url.contains("sp-content")) {
			IOperationLogAnalyzer operationAnalyzer = new HttpClientOperationLogAnalyzer();
			SubscriptionLog slog = operationAnalyzer.analyze(step);
			slog.setTag("SPExtension");
			return slog;
		}
		else if (url.contains("hpcloudsvc")) {
			IOperationLogAnalyzer operationAnalyzer = new HPCSOperationLogAnalyzer();
			SubscriptionLog slog = operationAnalyzer.analyze(step);
			slog.setTag("HPCS");
			return slog;
		}
		else {
			IOperationLogAnalyzer operationAnalyzer = new HttpClientOperationLogAnalyzer();
			return operationAnalyzer.analyze(step);
		}
	}

}
