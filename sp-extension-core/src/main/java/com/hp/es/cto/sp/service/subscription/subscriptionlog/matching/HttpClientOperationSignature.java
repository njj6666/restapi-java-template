package com.hp.es.cto.sp.service.subscription.subscriptionlog.matching;

import java.util.ArrayList;
import java.util.HashMap;

public class HttpClientOperationSignature extends OperationSignature {

	public HttpClientOperationSignature() {
		possibleNames = new ArrayList<String>();
		possibleNames.add("Http Client");
		
		inputs = new HashMap<String, String>();
		inputs.put("url", null);
	}

}
