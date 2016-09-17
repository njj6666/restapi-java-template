package com.hp.es.cto.sp.service.ldap;

import java.util.Properties;

/**
 * LDAP query service interface.
 * 
 * @author Dream
 * 
 */
public interface LdapQueryService {
	Properties findGroupByName(String ldapUrl, String groupName);
}
