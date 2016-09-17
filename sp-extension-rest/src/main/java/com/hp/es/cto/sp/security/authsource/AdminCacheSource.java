package com.hp.es.cto.sp.security.authsource;

import java.util.Iterator;

import javax.ws.rs.core.MediaType;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hp.es.cto.sp.cache.CacheSource;
import com.hp.es.cto.sp.cache.NoValueAvailableException;
import com.hp.es.cto.sp.util.CSAClient;

public class AdminCacheSource implements CacheSource {

	@Override
	public Object getForCache(Object key) throws NoValueAvailableException, Exception {
		CSAClient client = new CSAClient();
		String path = "/rest/organization?userIdentifier=" + key;
		String objXML = client.invoke(path,MediaType.APPLICATION_XML);
		Document doc = DocumentHelper.parseText(objXML);
		Element rootElt = doc.getRootElement();
		if (rootElt == null) {
			return false;
		}
		Iterator iter = rootElt.elementIterator("organization");
		if (!iter.hasNext()) {
			return false;
		}
		return true;
	}

}
