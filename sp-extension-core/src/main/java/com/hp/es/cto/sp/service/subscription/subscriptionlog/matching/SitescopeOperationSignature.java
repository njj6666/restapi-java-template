package com.hp.es.cto.sp.service.subscription.subscriptionlog.matching;

import java.util.ArrayList;

public class SitescopeOperationSignature extends OperationSignature {

	public SitescopeOperationSignature() {
		possibleNames = new ArrayList<String>();
		possibleNames.add("Deploy Template");
		possibleNames.add("Delete Monitor");
	}

}
