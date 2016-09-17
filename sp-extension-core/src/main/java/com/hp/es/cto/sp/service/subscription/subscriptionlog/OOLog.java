package com.hp.es.cto.sp.service.subscription.subscriptionlog;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OOLog {
	private final Logger logger = LoggerFactory.getLogger(OOLog.class);
	private JsonNode stepNode;

	public OOLog(String message) {
		// Parse the raw log to JSON object
		ObjectMapper mapper = new ObjectMapper();
		try {
			message = message.replaceAll("\"", "\\\"");
			String logs = "{\"logs\":" + message + "}";
			logger.info(logs);
			JsonNode logData = mapper.readTree(logs);
			stepNode = logData.get("logs");
		}
		catch (JsonParseException e) {
			logger.error("Cannot parse JSON message: " + message, e);
		}
		catch (JsonMappingException e) {
			logger.error("Cannot map JSON message: " + message, e);
		}
		catch (Exception e) {
			logger.error("Cannot parse JSON message: " + message, e);
		}
	}

	public OOLog(JsonNode stepNode) {
		this.stepNode = stepNode;
	}

	/**
	 * Get the direct last child step given a specified path prefix
	 * For example:
	 * The first step at the first level: 0
	 * The second direct child step inside the first step: 0.1
	 * 
	 * @param pathPrefix
	 * @return
	 */
	public OOLog getLastStep(String pathPrefix) {
		if (stepNode == null) {
			return null;
		}
		int maxNum = -1;
		JsonNode lastStep = null;
		logger.debug("pathPrefix "+pathPrefix);
		Iterator<JsonNode> ite = stepNode.getElements();
		while (ite.hasNext()) {
			JsonNode step = ite.next();
			JsonNode stepInfo = step.get("stepInfo");
			if (stepInfo == null) {
				continue;
			}
			String path = stepInfo.get("path").getTextValue();
			logger.debug("path "+path);
			if (!path.startsWith(pathPrefix) || path.equals(pathPrefix)) {
				continue;
			}
			String subPath = path.substring(pathPrefix.length() + 1);
			logger.debug("subPath "+subPath);
			// We want the direct child, so the sub path should just be a number
			if (subPath.isEmpty() || subPath.contains(".")) {
				continue;
			}
			
			int num = Integer.parseInt(subPath);
			if (num > maxNum) {
				maxNum = num;
				lastStep = step;
			}
		}
		if (lastStep == null) {
			return null;
		}
		return new OOLog(lastStep);
	}

	/**
	 * Get the path of the step
	 * 
	 * @return the path of the step
	 */
	public String getPath() {
		if (stepNode == null) {
			return null;
		}
		JsonNode stepInfo = stepNode.get("stepInfo");
		if (stepInfo == null) {
			return null;
		}
		return stepInfo.get("path").getTextValue();
	}

	/**
	 * Get the transition description of the step if there is any
	 * 
	 * @param step
	 * @return the transition description of the step or null
	 */
	public String getTransitionDescription() {
		if (stepNode == null) {
			return null;
		}
		JsonNode stepTransitionLog = stepNode.get("stepTransitionLog");
		if (stepTransitionLog == null) {
			return null;
		}
		return stepTransitionLog.get("transitionDescription").getTextValue();
	}

	/**
	 * Get the name of the flow
	 * 
	 * @return the name of the flow
	 */
	public String getFlowName() {
		if (stepNode == null) {
			return null;
		}
		JsonNode stepInfo = stepNode.get("stepInfo");
		if (stepInfo == null) {
			return null;
		}
		return stepInfo.get("flowName").getTextValue();
	}

	/**
	 * Get the name of the step
	 * 
	 * @return the name of the step
	 */
	public String getStepName() {
		if (stepNode == null) {
			return null;
		}
		JsonNode stepInfo = stepNode.get("stepInfo");
		if (stepInfo == null) {
			return null;
		}
		return stepInfo.get("stepName").getTextValue();
	}

	/**
	 * Check if the step is a basic operation, type can be "Operation" and "SUBFLOW"
	 * 
	 * @return if the step is a basic operation
	 */
	public boolean isOperation() {
		if (stepNode == null) {
			return false;
		}
		JsonNode stepInfo = stepNode.get("stepInfo");
		if (stepInfo == null) {
			return false;
		}
		return "Operation".equalsIgnoreCase(stepInfo.get("type").getTextValue());
	}

	/**
	 * Get the exception of the step
	 * 
	 * @return the exception of the step
	 */
	public String getStepException() {
		if (stepNode == null) {
			return null;
		}
		JsonNode result = stepNode.get("rawResult");
		if (result == null) {
			return null;
		}
		JsonNode exception = result.get("exception");
		if (exception == null) {
			return null;
		}
		return exception.getTextValue();
	}

	/**
	 * Get the result of the step
	 * 
	 * @return the result of the step
	 */
	public String getStepResult(String resultName) {
		if (stepNode == null) {
			return null;
		}
		JsonNode result = stepNode.get("rawResult");
		if (result == null) {
			return null;
		}
		JsonNode exception = result.get(resultName);
		if (exception == null) {
			return null;
		}
		return exception.getTextValue();
	}

	/**
	 * Get input with the specified name
	 * 
	 * @param inputName
	 * @return the value of the input
	 */
	public String getStepInput(String inputName) {
		if (stepNode == null) {
			return null;
		}
		JsonNode inputs = stepNode.get("stepInputs");
		if (inputs == null) {
			return null;
		}
		Iterator<JsonNode> ite = inputs.getElements();
		while (ite.hasNext()) {
			JsonNode input = ite.next();
			JsonNode name = input.get("name");
			if (inputName.equals(name.getTextValue())) {
				if (input.get("value") == null) {
					return null;
				}
				return input.get("value").getTextValue();
			}
		}
		return null;
	}
}
