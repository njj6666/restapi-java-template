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

import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderRegionMapping;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegion;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegionAz;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderRegionServiceImplTest {
	private ProviderRegion providerRegion;
	private ProviderType pt;
	
	@Autowired
	private ProviderRegionService providerRegionService;
	@Autowired
	private ProviderTypeService providerTypeService;
	@Autowired
	private ProviderRegionAzService providerRegionAzService;
	@Autowired
	private OrgProviderRegionMappingService orgProviderRegionMappingService;

	private static final String TEST_IAAS_PROVIDER = "TEST_IAAS_PROVIDER_01";
	private static final String TEST_REGION_NAME1 = "TEST_REGION_NAME_01";
	private static final String TEST_REGION_NAME2 = "TEST_REGION_NAME_02";
	private static final String ORG_ID = "ORG_ID";
	
	@Before
	public void before() {
		pt = new ProviderType();
		pt.setUuid(UUID.randomUUID().toString());
		pt.setSubType("default");
		pt.setType(TEST_IAAS_PROVIDER);
		pt = providerTypeService.create(pt);

		// Initial a ProviderRegion object.
		providerRegion = new ProviderRegion();
		providerRegion.setUuid(UUID.randomUUID().toString());
		providerRegion.setProviderType(pt);
		providerRegion.setRegionName(TEST_REGION_NAME1);
		providerRegion.setRegionDisplayName(TEST_REGION_NAME1);
	}

	@After
	public void after() {
		providerTypeService.deleteByType(TEST_IAAS_PROVIDER);
	}

	@Test
	public void testCRUD() {
		providerRegion = providerRegionService.create(providerRegion);

		String uuid = providerRegion.getUuid();
		System.out.println("New created provider region uuid -- " + uuid);

		assertEquals(1, providerRegionService.findAll().size());
		
		assertEquals(1, providerRegionService.findByProviderTypeId(pt.getUuid()).size());
		
		assertEquals(1, providerRegionService.findByProviderType(TEST_IAAS_PROVIDER, "default").size());
		
		assertEquals(0, providerRegionService.findByProviderType("default",TEST_IAAS_PROVIDER).size());
		
		providerRegion.setRegionName(TEST_REGION_NAME2);
		providerRegionService.update(providerRegion);
		assertEquals(TEST_REGION_NAME2, providerRegionService.findById(uuid).getRegionName());

		providerRegionService.delete(providerRegion);
		assertEquals(0, providerRegionService.findAll().size());
	}
	
	@Test
	public void testMappingCRUD() {
		providerRegion = providerRegionService.create(providerRegion);

		String uuid = providerRegion.getUuid();
		System.out.println("New created provider region uuid -- " + uuid);
		
		ProviderRegion providerRegion2 = new ProviderRegion();
		providerRegion2.setUuid(UUID.randomUUID().toString());
		providerRegion2.setProviderType(pt);
		providerRegion2.setRegionName(TEST_REGION_NAME2);
		providerRegion2.setRegionDisplayName(TEST_REGION_NAME1);
		providerRegion2 = providerRegionService.create(providerRegion2);
		
		assertEquals(2, providerRegionService.findAll().size());
		
		assertEquals(2, providerRegionService.findByProviderTypeId(pt.getUuid()).size());
		
		assertEquals(2, providerRegionService.findByProviderType(TEST_IAAS_PROVIDER, "default").size());
		
		assertEquals(0, providerRegionService.findByProviderType("default",TEST_IAAS_PROVIDER).size());
		
		assertEquals(2, providerRegionService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID).size());
		
		OrgProviderRegionMapping orgmap = new OrgProviderRegionMapping();
		orgmap.setUuid(UUID.randomUUID().toString());
		orgmap.setOrgId(ORG_ID);
		orgmap.setProviderRegion(providerRegion);
		orgProviderRegionMappingService.create(orgmap);
		
		assertEquals(1, providerRegionService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID).size());
		
		providerRegion = providerRegionService.findById(uuid);
		providerRegionService.delete(providerRegion);
		assertEquals(1, providerRegionService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID).size());
		
	}

	@Test
	public void testAzCRUD() {
		providerRegion = providerRegionService.create(providerRegion);

		String uuid = providerRegion.getUuid();
		System.out.println("New created provider region uuid -- " + uuid);

		ProviderRegionAz providerRegionAz1 = new ProviderRegionAz();
		providerRegionAz1.setUuid(UUID.randomUUID().toString());
		providerRegionAz1.setAz("AZ1");
		providerRegionAz1.setProviderRegion(providerRegion);
		providerRegionAz1 = providerRegionAzService.create(providerRegionAz1);
		String uuid1 = providerRegionAz1.getUuid();

		assertEquals(1, providerRegionAzService.findByRegionId(uuid).size());

		assertEquals(0, providerRegionAzService.findByRegionId("testId").size());

		providerRegionAz1.setAz("AZ11");
		providerRegionAzService.update(providerRegionAz1);
		assertEquals("AZ11", providerRegionAzService.findById(uuid1).getAz());

		ProviderRegionAz providerRegionAZ2 = new ProviderRegionAz();
		providerRegionAZ2.setUuid(UUID.randomUUID().toString());
		providerRegionAZ2.setAz("AZ2");
		providerRegionAZ2.setProviderRegion(providerRegion);
		providerRegionAZ2 = providerRegionAzService.create(providerRegionAZ2);

		assertEquals(2, providerRegionAzService.findAll().size());

		providerRegionAzService.delete(providerRegionAz1);
		assertEquals(1, providerRegionAzService.findAll().size());

		providerRegion = providerRegionService.findById(uuid);
		providerRegionService.delete(providerRegion);
		assertEquals(0, providerRegionService.findAll().size());
		assertEquals(0, providerRegionAzService.findAll().size());
	}
}
