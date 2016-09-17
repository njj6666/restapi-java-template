package com.hp.es.cto.sp.service.ldap;

import java.util.List;
import java.util.Properties;

import com.hp.es.cto.sp.service.ldap.util.LdapConnector;

/**
 * Implementation service class of LdapQueryService
 * 
 * @author Dream
 * @see com.hp.es.cto.sp.service.ldap.LdapQueryService
 * 
 */
public class LdapQueryServiceImpl implements LdapQueryService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Properties findGroupByName(String ldapUrl, String groupName) {
		LdapConnector ldap = new LdapConnector(ldapUrl);
		List<Properties> groups = ldap.findGroup("cn", groupName);
		ldap.close();
		
		return groups.size() > 0 ? groups.get(0) : null;
	}
}
