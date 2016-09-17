package com.hp.es.cto.sp.service.ldap.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

public class LdapConnectorSample {
	private static String principal = "cn=admin,o=hp.com";
	private static String credentials = "sp*ld@p#adm1n";

	private static String ldapUrl = "ldaps://192.85.180.228:636";
	private static String rootEntry = "ou=People, o=hp.com";

	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, credentials);
		env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put(LdapContext.CONTROL_FACTORIES, "com.sun.jndi.ldap.ControlFactory");

		// Connect
		DirContext ctx = new InitialDirContext(env);
		SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(new String[] { "objectClass", "uid", "mail", "cn", "userPassword" });
		ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

		String filter = "(&(objectClass=person)(objectClass=inetOrgPerson)(uid=jun.zhang14@hp.com))";
		 NamingEnumeration<SearchResult> answer = ctx.search(rootEntry, filter, ctls);
		//NamingEnumeration<SearchResult> answer = ctx.search("ou=Groups,o=hp.com","(&(objectClass=groupOfNames)(member=uid=jun.zhang14@hp.com,ou=People,o=hp.com))", ctls);

		while (answer.hasMore()) {
			SearchResult sr = answer.next();
			String cn = sr.getAttributes().get("cn").get() + "";
			String dn = sr.getNameInNamespace();
			String name = sr.getName();
			String password = new String((byte[])sr.getAttributes().get("userPassword").get() );
			// String uid = sr.getAttributes().get("uid").get() + "";
			// String mail = sr.getAttributes().get("mail").get() + "";
			// String memberOf = sr.getAttributes().get("memberOf").get() + "";
			System.out.println("Query result:\n" + cn + "\n" + dn + " \n" + name + " \n" + password);
		}
		answer.close();
		ctx.close();
	}
}
