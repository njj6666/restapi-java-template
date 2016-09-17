package com.hp.es.cto.sp.service.subscription.subscriptionlog.operation.http;

import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.util.StringUtil;

public class HPCSOperationLogAnalyzer extends HttpClientOperationLogAnalyzer {
	private final Logger logger = LoggerFactory.getLogger(HPCSOperationLogAnalyzer.class);
	
	/**
	 * Analyze return message of HPCS
	 */
	protected SubscriptionLog analyze(String errorMsg, String document) {
		if (StringUtil.isNullOrEmpty(errorMsg)) {
			errorMsg = document;
		}

		return parse(errorMsg);
	}
	
	public SubscriptionLog parse(String errorMsg) {
		SubscriptionLog slog = new SubscriptionLog();
		if (StringUtil.isNullOrEmpty(errorMsg)){
			slog.setMessage("Unknown error in Openstack");
			return slog;
		}
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(errorMsg);

			Iterator<String> errorNode = node.getFieldNames();
			if (!errorNode.hasNext()) {
				slog.setMessage(node.toString());
			}
			String errorType = errorNode.next();
			JsonNode errorDescription = node.get(errorType);
			JsonNode codeNode = errorDescription.get("code");
			JsonNode messageNode = errorDescription.get("message");
			JsonNode detailsNode = errorDescription.get("details");

			if (detailsNode != null) {
				slog.setDetail(detailsNode.asText());
			}

			String message = errorType;
			if (codeNode != null) {
				message = codeNode.asText() + " " + message;
			}
			if (messageNode != null) {
				message = message + " " + messageNode.asText();
			}
			slog.setMessage(message);
		}
		catch (JsonProcessingException e) {
			slog.setMessage(errorMsg);
		}
		catch (IOException e) {
			slog.setMessage(errorMsg);
			logger.error(e.getMessage(), e);
		}

		return slog;

	}

}
