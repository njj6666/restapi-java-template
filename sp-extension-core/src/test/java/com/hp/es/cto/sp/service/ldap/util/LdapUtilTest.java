package com.hp.es.cto.sp.service.ldap.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LdapUtilTest {
	@Test
	public void test_getParentDn() {
		String s1 = null;
		String s2 = "";
		String s3 = "cn=Bellucci-Adm-sub";
		String s4 = "cn=Bellucci-Adm-sub,cn=Bellucci-Adm,u=Bellucci,ou=Groups,o=hp.com";
		
		Assert.assertEquals("", LdapUtil.getParentDn(s1));
		Assert.assertEquals("", LdapUtil.getParentDn(s2));
		Assert.assertEquals("", LdapUtil.getParentDn(s3));
		Assert.assertEquals("cn=Bellucci-Adm,u=Bellucci,ou=Groups,o=hp.com", LdapUtil.getParentDn(s4));
	}
	
	@Test
	public void test_getParentsDn() {
		String s1 = null;
		String s2 = "";
		String s3 = "cn=Bellucci-Adm-sub";
		String s4 = "cn=Bellucci-Adm-sub,cn=Bellucci-Adm,ou=Bellucci,ou=Groups,o=hp.com";
		
		List<String> resultList = LdapUtil.getParentsDn(s1);
		Assert.assertEquals(0, resultList.size());
		
		resultList = LdapUtil.getParentsDn(s2);
		Assert.assertEquals(0, resultList.size());
		
		resultList = LdapUtil.getParentsDn(s3);
		Assert.assertEquals(0, resultList.size());
		
		resultList = LdapUtil.getParentsDn(s4);
		Assert.assertEquals(4, resultList.size());

	}
	
	@Test
	public void test_isParentsDn() {
		String s1 = null;
		String s2 = "";
		String s3 = "cn=Bellucci-Adm-sub,cn=Bellucci-Adm,u=Bellucci,ou=Groups,o=hp.com";
		
		String p1 = null;
		String p2 = "";
		String p3 = "cn=Bellucci-Adm-sub";
		String p4 = "cn=Bellucci-Adm,u=Bellucci,ou=Groups,o=hp.com";
		
		Assert.assertFalse(LdapUtil.isParentDn(s1, p4));
		Assert.assertFalse(LdapUtil.isParentDn(s2, p4));
		Assert.assertFalse(LdapUtil.isParentDn(s3, p1));
		Assert.assertFalse(LdapUtil.isParentDn(s3, p2));
		Assert.assertFalse(LdapUtil.isParentDn(s3, p3));
		Assert.assertTrue(LdapUtil.isParentDn(s3, p4));
	}

	@Test
	public void test_addBaseDn() {
		String s1 = null;
		String s2 = "";
		String s3 = "cn=Bellucci-Adm-sub,cn=Bellucci-Adm,u=Bellucci";
		
		String p1 = null;
		String p2 = "";
		String p3 = "ou=Groups,o=hp.com";
		Assert.assertEquals(s1, LdapUtil.addBaseDn(s1, p3));
		Assert.assertEquals(s2, LdapUtil.addBaseDn(s2, p3));
		Assert.assertEquals(s3, LdapUtil.addBaseDn(s3, p1));
		Assert.assertEquals(s3, LdapUtil.addBaseDn(s3, p2));
		Assert.assertEquals("cn=Bellucci-Adm-sub,cn=Bellucci-Adm,u=Bellucci,ou=Groups,o=hp.com", LdapUtil.addBaseDn(s3, p3));
		
	}

}
