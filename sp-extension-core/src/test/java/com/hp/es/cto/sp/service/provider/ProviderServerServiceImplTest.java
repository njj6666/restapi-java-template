package com.hp.es.cto.sp.service.provider;

import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;
import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderServer;
import com.hp.es.cto.sp.service.provider.ProviderServerService;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderServerServiceImplTest {
	private ProviderServer providerServer;
	@Autowired
	private ProviderServerService providerServerService;

	private static final String TEST_SERVER_NAME = "TEST_SERVER_NAME";
	private static final String OS_TYPE = "Linux";
	private static final String PROVIDER = "ecs";
	private static final String SIZE = "small";
	private static final String IPADDRESS = "16.157.53.157";
	private static final String PUBLIC_IPADDRESS = "16.157.53.157";
	private static final String PRIVATE_IPADDRESS = "192.168.1.2";
	private static final String ASSIGNED_FALSE = "false";
	private static final String ASSIGNED_TRUE = "true";

	@Before
	public void before() {
		String csaProviderId = UUID.randomUUID().toString();

		// Initial a ProviderRegion object.
		providerServer = new ProviderServer();
		providerServer.setUuid(UUID.randomUUID().toString());
		providerServer.setCsaProviderId(csaProviderId);
		providerServer.setIpAddress(IPADDRESS);
		providerServer.setName(TEST_SERVER_NAME);
		providerServer.setOsType(OS_TYPE);
		providerServer.setPrivateIpAddress(PRIVATE_IPADDRESS);
		providerServer.setProvider(PROVIDER);
		providerServer.setPublicIpAddress(PUBLIC_IPADDRESS);
		providerServer.setSize(SIZE);
		providerServer.setIsAllocated(ASSIGNED_FALSE);

	}

	@After
	public void after() {
	}

	@Test
	public void testCRUD() {
		providerServer = providerServerService.create(providerServer);

		String uuid = providerServer.getUuid();
		System.out.println("New created provider region uuid -- " + uuid);

		assertEquals(1, providerServerService.findAll().size());

		providerServer.setIsAllocated(ASSIGNED_TRUE);
		providerServerService.update(providerServer);
		assertEquals(ASSIGNED_TRUE, providerServerService.findById(uuid)
				.getIsAllocated());

		providerServerService.delete(providerServer);
		assertEquals(0, providerServerService.findAll().size());
	}

	@Test
	public void testGetByProviderId() {
		providerServer = providerServerService.create(providerServer);

		String iaasProviderId = providerServer.getCsaProviderId();
		System.out.println("provider id -- " + iaasProviderId);

		assertEquals(1, providerServerService.findByProviderId(iaasProviderId)
				.size());
		// add another
		providerServer.setUuid(UUID.randomUUID().toString());
		providerServer.setIpAddress("");
		providerServer.setPublicIpAddress("");
		providerServer.setPrivateIpAddress("");
		providerServerService.create(providerServer);
		assertEquals(2, providerServerService.findByProviderId(iaasProviderId)
				.size());

		providerServerService.deleteByProviderId(iaasProviderId);
		assertEquals(0, providerServerService.findByProviderId(iaasProviderId)
				.size());
	}

	@Test
	public void testDuplicateIpaddressCreate() {
		providerServer = providerServerService.create(providerServer);

		String uuid = providerServer.getUuid();
		System.out.println("New created provider region uuid -- " + uuid);

		// empty
		providerServer.setUuid(UUID.randomUUID().toString());
		providerServer.setIpAddress("");
		providerServer.setPublicIpAddress("");
		providerServer.setPrivateIpAddress("");
		try {
			providerServerService.create(providerServer);
			assertTrue(true);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// empty2
		providerServer.setUuid(UUID.randomUUID().toString());
		providerServer.setIpAddress("");
		providerServer.setPublicIpAddress("");
		providerServer.setPrivateIpAddress("");
		try {
			providerServerService.create(providerServer);
			assertTrue(false);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// same ipaddress
		providerServer.setUuid(UUID.randomUUID().toString());
		providerServer.setIpAddress(IPADDRESS);
		providerServer.setPublicIpAddress("");
		providerServer.setPrivateIpAddress("");
		try {
			providerServerService.create(providerServer);
			assertTrue(false);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// same privateIpaddress
		providerServer.setIpAddress("");
		providerServer.setPublicIpAddress("");
		providerServer.setPrivateIpAddress(PRIVATE_IPADDRESS);
		try {
			providerServerService.create(providerServer);
			assertTrue(false);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// same publicIpaddress
		providerServer.setIpAddress("");
		providerServer.setPublicIpAddress(PUBLIC_IPADDRESS);
		providerServer.setPrivateIpAddress("");
		try {
			providerServerService.create(providerServer);
			assertTrue(false);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// delete
		providerServer.setUuid(uuid);
		providerServerService.delete(providerServer);
		assertEquals(1, providerServerService.findAll().size());
	}
}
