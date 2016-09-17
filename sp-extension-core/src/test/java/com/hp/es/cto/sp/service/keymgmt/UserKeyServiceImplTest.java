package com.hp.es.cto.sp.service.keymgmt;

import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;
import com.hp.es.cto.sp.service.keymgmt.UserKeyService;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserKeyServiceImplTest {

	@Autowired
	private UserKeyService userKeyService;

	private UserKey userKey;

	private static final String TEST_LDAP_EMAIL = "test123@hp.com";
	private static final String TEST_KEY_NAME_1 = "testkey123";
	private static final String TEST_KEY_NAME_2 = "testkey1234";

	@Before
	public void before() {
		// Initial a UserKey object.
		userKey = new UserKey();
		userKey.setUuid(UUID.randomUUID().toString());
		userKey.setLdapEmail(TEST_LDAP_EMAIL);
		userKey.setName(TEST_KEY_NAME_1);
		userKey.setKeyFile(new byte[] { 't', 'e', 's', 't', '1', '2', '3' });
	}

	@After
	public void after() {
	}

	@Test
	public void testCRUD() {
		userKey = userKeyService.create(userKey);
		Assert.assertEquals(TEST_KEY_NAME_1, userKey.getName());
		Assert.assertNotNull(userKey.getKeyFile());

		String uuid = userKey.getUuid();
		System.out.println("New created user key uuid -- " + uuid);

		userKey = userKeyService.findByEmail(TEST_LDAP_EMAIL).get(0);
		Assert.assertEquals(TEST_LDAP_EMAIL, userKey.getLdapEmail());

		userKey.setName(TEST_KEY_NAME_2);
		userKeyService.update(userKey);
		Assert.assertEquals(TEST_KEY_NAME_2, userKeyService.findById(uuid).getName());

		userKeyService.delete(userKey);
		Assert.assertNull(userKeyService.findById(uuid));
	}
}
