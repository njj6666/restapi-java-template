package com.hp.es.cto.sp.service.subscription.subscriptionlog.matching;

import java.util.ArrayList;
import java.util.HashMap;

public class SSHOperationSignature extends OperationSignature {

	public SSHOperationSignature() {
		possibleNames = new ArrayList<String>();
		possibleNames.add("SSHExec");
		possibleNames.add("SecureCopy");
		
		inputs = new HashMap<String, String>();
		inputs.put("hostname", null);
		inputs.put("username", null);
		inputs.put("port", "22");
	}

}
