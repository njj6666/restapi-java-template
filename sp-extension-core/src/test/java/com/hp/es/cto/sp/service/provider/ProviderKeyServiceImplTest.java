package com.hp.es.cto.sp.service.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;
import com.hp.es.cto.sp.service.provider.ProviderKeyService;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderKeyServiceImplTest {
	@Autowired
	private ProviderKeyService providerKeyService;

	private ProviderKey providerKey;

	private static final String TEST_CSA_PROVIDER_ID_1 = "CSA_TEST";
	private static final String TEST_CSA_PROVIDER_ID_2 = "CSA_TEST";
	private static final String TEST_REGION = "WEST";
	private static final String TEST_USERNAME = "test_username";
	private static final String TEST_PASSWORD = "test_password";
	private static final String TEST_NAME = "name123";

	@Before
	public void before() {
		// Initial a ProviderKey object.
		providerKey = new ProviderKey();
		providerKey.setUuid(UUID.randomUUID().toString());
		providerKey.setCsaProviderId(TEST_CSA_PROVIDER_ID_1);
		providerKey.setRegionName(TEST_REGION);
		providerKey.setUsername(TEST_USERNAME);
		providerKey.setPassword(TEST_PASSWORD);
		providerKey.setName(TEST_NAME);
		providerKey.setKeyFile(new byte[] { 't', 'e', 's', 't', '1', '2', '3' });
	}

	@After
	public void after() {
	}

	@Test
	public void testCRUD() {
		providerKey = providerKeyService.create(providerKey);
		assertEquals(TEST_CSA_PROVIDER_ID_1, providerKey.getCsaProviderId());

		String uuid = providerKey.getUuid();
		System.out.println("New created provider key uuid -- " + uuid);

		assertEquals(1, providerKeyService.findAll().size());

		providerKey.setCsaProviderId(TEST_CSA_PROVIDER_ID_2);
		providerKeyService.update(providerKey);
		assertEquals(TEST_CSA_PROVIDER_ID_2, providerKeyService.findById(uuid).getCsaProviderId());

		assertEquals(TEST_CSA_PROVIDER_ID_2, providerKeyService.findByProviderId(TEST_CSA_PROVIDER_ID_2).get(0)
				.getCsaProviderId());

		assertEquals(TEST_CSA_PROVIDER_ID_2,
				providerKeyService.findByProviderIdAndRegion(TEST_CSA_PROVIDER_ID_2, TEST_REGION).get(0)
						.getCsaProviderId());

		providerKeyService.delete(providerKey);
		assertNull(providerKeyService.findById(uuid));
	}
}
