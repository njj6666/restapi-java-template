package com.hp.es.cto.sp.security.authsource;

import java.util.Iterator;

import javax.ws.rs.core.MediaType;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.cache.CacheSource;
import com.hp.es.cto.sp.cache.NoValueAvailableException;
import com.hp.es.cto.sp.util.CSAClient;

public class UserCacheSource implements CacheSource {

	private static final Logger logger = LoggerFactory.getLogger(UserCacheSource.class);

	@Override
	public Object getForCache(Object key) throws NoValueAvailableException, Exception {
		String[] orguser = ((String) key).split("::");
		if (orguser.length != 2) {
			throw new Exception("No organization or username : " + key);
		}
		String organization = orguser[0];
		String name = orguser[1];

		String path = "/rest/login/" + organization + "/" + name + ".xml";
		CSAClient client = new CSAClient();
		String objXML = client.invoke(path,MediaType.APPLICATION_XML);
		Document doc = DocumentHelper.parseText(objXML);
		Element rootElt = doc.getRootElement();

		// Get user id
		String id = null;
		String emailAddress = null;
		Iterator iter = rootElt.elementIterator("id");
		if (iter.hasNext()) {
			Element itemEle = (Element) iter.next();
			id = itemEle.getText();
			logger.debug("user Id: " + id);
		}

		// Get user email
		iter = rootElt.elementIterator("emailAddress");
		if (iter.hasNext()) {
			Element itemEle = (Element) iter.next();
			emailAddress = itemEle.getText();
			logger.debug("emailAddress: " + emailAddress);
		}

		return new CSAUser(id, emailAddress);
	}

}
