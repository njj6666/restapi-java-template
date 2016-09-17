package com.hp.es.cto.sp.service.provider;

import static org.junit.Assert.assertEquals;
import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderSharedResource;
import com.hp.es.cto.sp.service.provider.ProviderSharedResourceService;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderSharedResourceServiceImplTest {
	private ProviderSharedResource providerSharedResource;
	@Autowired
	private ProviderSharedResourceService providerSharedResourceService;

	private static final String TEST_SERVER_NAME = "TEST_SERVER_NAME";
	private static final Integer CURRENT_CONNECTION = 0;
	private static final Integer MAX_CONNECTION = 100;

	@Before
	public void before() {
		String csaProviderId = UUID.randomUUID().toString();

		// Initial a ProviderRegion object.
		providerSharedResource = new ProviderSharedResource();
		providerSharedResource.setUuid(UUID.randomUUID().toString());
		providerSharedResource.setCsaProviderId(csaProviderId);
		providerSharedResource.setName(TEST_SERVER_NAME);
		providerSharedResource.setCurrentConnection(CURRENT_CONNECTION);
		providerSharedResource.setMaxConnection(MAX_CONNECTION);
	}

	@After
	public void after() {
	}

	@Test
	public void testCRUD() {
		providerSharedResource = providerSharedResourceService.create(providerSharedResource);

		String uuid = providerSharedResource.getUuid();
		System.out.println("New created provider region uuid -- " + uuid);

		assertEquals(1, providerSharedResourceService.findAll().size());

		providerSharedResource.setCurrentConnection(1);
		providerSharedResourceService.update(providerSharedResource);
		assertEquals(1, providerSharedResourceService.findById(uuid).getCurrentConnection().intValue());

		providerSharedResourceService.delete(providerSharedResource);
		assertEquals(0, providerSharedResourceService.findAll().size());
	}

	@Test
	public void testGetByProviderId() {
		providerSharedResource = providerSharedResourceService.create(providerSharedResource);

		String iaasProviderId = providerSharedResource.getCsaProviderId();
		System.out.println("provider id -- " + iaasProviderId);

		assertEquals(1, providerSharedResourceService.findByProviderId(iaasProviderId)
				.size());
		// add another
		providerSharedResource.setUuid(UUID.randomUUID().toString());
		providerSharedResource.setName(TEST_SERVER_NAME);
		providerSharedResource.setCurrentConnection(100);
		providerSharedResource.setMaxConnection(MAX_CONNECTION);
		providerSharedResourceService.create(providerSharedResource);
		assertEquals(2, providerSharedResourceService.findByProviderId(iaasProviderId)
				.size());
		
		assertEquals(1, providerSharedResourceService.findByProviderIdAndAvailable(iaasProviderId)
				.size());

		providerSharedResourceService.deleteByProviderId(iaasProviderId);
		assertEquals(0, providerSharedResourceService.findByProviderId(iaasProviderId)
				.size());
	}

}
