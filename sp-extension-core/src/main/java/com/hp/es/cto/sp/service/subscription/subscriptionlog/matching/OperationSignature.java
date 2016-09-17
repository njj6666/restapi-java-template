package com.hp.es.cto.sp.service.subscription.subscriptionlog.matching;

import java.util.List;
import java.util.Map;

public abstract class OperationSignature {
	
	protected List<String> possibleNames;
	protected Map<String, String> inputs;
	
	public List<String> getNames(){
		return possibleNames;
	}
	
	public Map<String,String> getInputs(){
		return inputs;
	}

}
