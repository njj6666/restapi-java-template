package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.http;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.util.StringUtil;

public class CSAOperationLogAnalyzer extends HttpClientOperationLogAnalyzer{
	private final Logger logger = LoggerFactory.getLogger(CSAOperationLogAnalyzer.class);
	
	protected SubscriptionLog analyze(String errorMsg, String document) {
		SubscriptionLog slog = new SubscriptionLog();
		slog.setTag("CSA");
		if (!StringUtil.isNullOrEmpty(errorMsg)) {
			slog.setMessage(errorMsg);
			return slog;
		}
		if (StringUtil.isNullOrEmpty(document)) {
			slog.setMessage("Unknow error in CSA");
			return slog;
		}
		
		try {
			// error document: <ExceptionView><exceptionMessage></exceptionMessage></ExceptionView>
			if (document.contains("exceptionMessage")) {
				Document doc = DocumentHelper.parseText(document);
				String exceptionMsg = doc.getRootElement().elementText("exceptionMessage");
				if (exceptionMsg != null) {
					slog.setMessage(exceptionMsg);
				}
			}
			else {
				slog.setMessage(document);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return slog;
	}

}
