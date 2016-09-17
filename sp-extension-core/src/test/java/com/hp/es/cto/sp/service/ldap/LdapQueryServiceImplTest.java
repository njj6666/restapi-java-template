package com.hp.es.cto.sp.service.ldap;

import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class LdapQueryServiceImplTest {
	@Autowired
	private LdapQueryService ldapQueryService;

	@Before
	public void before() {
	}

	@After
	public void after() {
	}

	@Test
	public void test() {
		Properties groupProp = ldapQueryService.findGroupByName("ldaps://ts.serviceplatform.ensv.hp.com:636", "es-cto-cs-support");
		assertNotNull(groupProp);

		groupProp = ldapQueryService.findGroupByName("ldaps://ts.serviceplatform.ensv.hp.com:636", "group-not-exist-123");
		assertNull(groupProp);
	}
}
