package com.hp.es.cto.sp.service.subscription.subscriptionlog.matching;

import java.util.ArrayList;
import java.util.HashMap;

public class PowershellOperationSignature extends OperationSignature{
	public PowershellOperationSignature() {
		possibleNames = new ArrayList<String>();
		possibleNames.add("PowerShell");
		possibleNames.add("remote session");
		possibleNames.add("remote transfer");
		
		inputs = new HashMap<String, String>();
		inputs.put("host", null);
		inputs.put("username", null);
		inputs.put("script", null);
	}
}
