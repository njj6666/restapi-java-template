package com.hp.es.cto.sp.service.ldap.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

public class LdapConnector {
	private static final String PRICINPAL = "cn=es-sp-readonly,ou=Applications,o=hp.com";
	private static final String CREDENTIALS = "SPreadonly";
	private static final String GROUP_SEARCH_BASE = "ou=Groups,o=hp.com";
	
	private static final String ATTR_CN = "cn";
	private static final String ATTR_DN = "dn";
	
	private DirContext context = null;

	public LdapConnector(String ldapUrl) {
		try {
			init(ldapUrl);
		}
		catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		if (context != null) {
			try {
				context.close();
			}
			catch (NamingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public List<Properties> findGroup(String attributeName, String attributeVal) {
		List<Properties> result = new ArrayList<Properties>();

		// Initial search controls
		SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(new String[] { "objectClass", "cn" });
		ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

		// Initial search filter
		String filter = "(" + attributeName + "=" + attributeVal + ")";

		// Try to find
		try {
			NamingEnumeration<SearchResult> answer = context.search(GROUP_SEARCH_BASE, filter, ctls);
			while (answer.hasMore()) {
				Properties groupProp = new Properties();
				SearchResult sr = answer.next();
				groupProp.put(ATTR_CN, sr.getAttributes().get(ATTR_CN).get() + "");
				groupProp.put(ATTR_DN, sr.getNameInNamespace());
				result.add(groupProp);
			}
			answer.close();
			return result;
		}
		catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	private void init(String ldapUrl) throws NamingException {
		// Environment
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, PRICINPAL);
		env.put(Context.SECURITY_CREDENTIALS, CREDENTIALS);
		env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put(LdapContext.CONTROL_FACTORIES, "com.sun.jndi.ldap.ControlFactory");

		// Connect
		context = new InitialDirContext(env);
	}
}
