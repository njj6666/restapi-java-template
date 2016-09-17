package com.hp.es.cto.sp.service.subscription.subscriptionlog.matching;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.es.cto.sp.service.subscription.subscriptionlog.OOLog;

public class OperationMatcher {

	/**
	 * Match the operation step to the operation signature
	 * @param step
	 * @param operationSig
	 * @return if the step is an instance of the operation
	 */
	public boolean match(OOLog step, OperationSignature operationSig) {
		// Match step name to possible operation name
		String stepName = step.getStepName();
		List<String> opNames = operationSig.getNames();
		for (String opName : opNames) {
			if (stepName.contains(opName)) {
				return true;
			}
		}
		// Match inputs, the inputs in signature must be in step
		Map<String, String> inputs = operationSig.getInputs();
		if (inputs == null){
			return false;
		}
		Iterator<String> inputNames = inputs.keySet().iterator();
		while (inputNames.hasNext()) {
			String inputName = inputNames.next();
			String stepInput = step.getStepInput(inputName);
			if (stepInput == null) {
				return false;
			}
			String inputVal = inputs.get(inputName);
			if (inputVal != null && !stepInput.equals(inputVal)){
				return false;
			}
		}
		return true;
	}

}
