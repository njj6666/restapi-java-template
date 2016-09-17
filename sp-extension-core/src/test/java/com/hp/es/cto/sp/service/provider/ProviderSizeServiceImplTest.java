package com.hp.es.cto.sp.service.provider;

import static org.junit.Assert.assertEquals;
import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.es.cto.sp.persistence.entity.provider.DesignProviderSizeMapping;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderSizeMapping;
import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderTypeMapping;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderSize;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderSizeServiceImplTest {
	private ProviderSize providerSize;
	private ProviderType pt;
	private ProviderType pt2;

	@Autowired
	private ProviderSizeService providerSizeService;
	@Autowired
	private ProviderTypeService providerTypeService;
	@Autowired
	private OrgProviderSizeMappingService orgProviderSizeMappingService;
	@Autowired
	private OrgProviderTypeMappingService orgProviderTypeMappingService;
	@Autowired
	private DesignProviderSizeMappingService designProviderSizeMappingService;

	private static final String TEST_IAAS_PROVIDER = "TEST_IAAS_PROVIDER_01";
	private static final String TEST_Size_NAME1 = "TEST_Size_NAME_01";
	private static final String TEST_Size_NAME2 = "TEST_Size_NAME_02";
	private static final String TEST_Size_NAME3 = "TEST_Size_NAME_03";
	private static final String ORG_ID = "ORG_ID";
	private static final String DESIGN_ID = "DESIGN_ID";

	@Before
	public void before() {
		pt = new ProviderType();
		pt.setUuid(UUID.randomUUID().toString());
		pt.setSubType("default");
		pt.setType(TEST_IAAS_PROVIDER);
		pt = providerTypeService.create(pt);
		
		pt2 = new ProviderType();
		pt2.setUuid(UUID.randomUUID().toString());
		pt2.setSubType("default2");
		pt2.setType(TEST_IAAS_PROVIDER);
		pt2 = providerTypeService.create(pt2);

		// Initial a ProviderSize object.
		providerSize = new ProviderSize();
		providerSize.setUuid(UUID.randomUUID().toString());
		providerSize.setProviderType(pt);
		providerSize.setSize(TEST_Size_NAME1);
	}

	@After
	public void after() {
		providerTypeService.deleteByType(TEST_IAAS_PROVIDER);
	}

	@Test
	public void testCRUD() {
		providerSize = providerSizeService.create(providerSize);

		String uuid = providerSize.getUuid();
		System.out.println("New created provider Size uuid -- " + uuid);

		assertEquals(1, providerSizeService.findAll().size());
		
		assertEquals(1, providerSizeService.findByProviderTypeId(pt.getUuid()).size());
		
		assertEquals(1, providerSizeService.findByProviderType(TEST_IAAS_PROVIDER, "default").size());
		
		assertEquals(0, providerSizeService.findByProviderType("default",TEST_IAAS_PROVIDER).size());
		
		providerSize.setSize(TEST_Size_NAME2);
		providerSizeService.update(providerSize);
		assertEquals(TEST_Size_NAME2, providerSizeService.findById(uuid).getSize());

		providerSizeService.delete(providerSize);
		assertEquals(0, providerSizeService.findAll().size());
	}
	
	@Test
	public void testTypeMappingCRUD() {
		assertEquals(2,  providerTypeService.findByTypeWithConstraint(TEST_IAAS_PROVIDER, ORG_ID).size());
		
		OrgProviderTypeMapping orgmap = new OrgProviderTypeMapping();
		orgmap.setUuid(UUID.randomUUID().toString());
		orgmap.setOrgId(ORG_ID);
		orgmap.setProviderType(pt);
		orgProviderTypeMappingService.create(orgmap);
		
		assertEquals(1,  providerTypeService.findByTypeWithConstraint(TEST_IAAS_PROVIDER, ORG_ID).size());
		
		pt = providerTypeService.findById(pt.getUuid());
		providerTypeService.delete(pt);
		
		assertEquals(1,  providerTypeService.findByTypeWithConstraint(TEST_IAAS_PROVIDER, ORG_ID).size());
	}

	@Test
	public void testMappingCRUD() {
		providerSize = providerSizeService.create(providerSize);

		String uuid = providerSize.getUuid();
		System.out.println("New created provider Size uuid -- " + uuid);

		ProviderSize providerSize2 = new ProviderSize();
		providerSize2.setUuid(UUID.randomUUID().toString());
		providerSize2.setSize(TEST_Size_NAME2);
		providerSize2.setProviderType(pt);
		providerSize2 = providerSizeService.create(providerSize2);
		
		ProviderSize providerSize3 = new ProviderSize();
		providerSize3.setUuid(UUID.randomUUID().toString());
		providerSize3.setSize(TEST_Size_NAME3);
		providerSize3.setProviderType(pt);
		providerSize3 = providerSizeService.create(providerSize3);

		assertEquals(3, providerSizeService.findByProviderTypeId(pt.getUuid()).size());
		
		assertEquals(3, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		OrgProviderSizeMapping orgmap = new OrgProviderSizeMapping();
		orgmap.setUuid(UUID.randomUUID().toString());
		orgmap.setOrgId(ORG_ID);
		orgmap.setProviderSize(providerSize);
		orgProviderSizeMappingService.create(orgmap);
		OrgProviderSizeMapping orgmap2 = new OrgProviderSizeMapping();
		orgmap2.setUuid(UUID.randomUUID().toString());
		orgmap2.setOrgId(ORG_ID);
		orgmap2.setProviderSize(providerSize2);
		orgProviderSizeMappingService.create(orgmap2);
		assertEquals(2, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		
		DesignProviderSizeMapping designmap = new DesignProviderSizeMapping();
		designmap.setUuid(UUID.randomUUID().toString());
		designmap.setServiceDesignId(DESIGN_ID);
		designmap.setProviderSize(providerSize);
		designProviderSizeMappingService.create(designmap);
		DesignProviderSizeMapping designmap3 = new DesignProviderSizeMapping();
		designmap3.setUuid(UUID.randomUUID().toString());
		designmap3.setServiceDesignId(DESIGN_ID);
		designmap3.setProviderSize(providerSize3);
		designProviderSizeMappingService.create(designmap3);
		assertEquals(2, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		assertEquals(1, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		providerSize = providerSizeService.findById(uuid);
		providerSizeService.delete(providerSize);
		assertEquals(1, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		assertEquals(1, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		orgProviderSizeMappingService.deleteByOrgId(ORG_ID);
		
		assertEquals(1, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		designProviderSizeMappingService.deleteByDesignId(DESIGN_ID);
		
		assertEquals(2, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
	}
	
	@Test
	public void testMappingRest() {
		providerSize = providerSizeService.create(providerSize);

		String uuid = providerSize.getUuid();
		System.out.println("New created provider Size uuid -- " + uuid);

		ProviderSize providerSize2 = new ProviderSize();
		providerSize2.setUuid(UUID.randomUUID().toString());
		providerSize2.setSize(TEST_Size_NAME2);
		providerSize2.setProviderType(pt);
		providerSize2 = providerSizeService.create(providerSize2);
		
		ProviderSize providerSize3 = new ProviderSize();
		providerSize3.setUuid(UUID.randomUUID().toString());
		providerSize3.setSize(TEST_Size_NAME3);
		providerSize3.setProviderType(pt);
		providerSize3 = providerSizeService.create(providerSize3);
		
		ProviderSize providerSize4 = new ProviderSize();
		providerSize4.setUuid(UUID.randomUUID().toString());
		providerSize4.setSize(TEST_Size_NAME2);
		providerSize4.setProviderType(pt2);
		providerSize4 = providerSizeService.create(providerSize4);
		
		ProviderSize providerSize5 = new ProviderSize();
		providerSize5.setUuid(UUID.randomUUID().toString());
		providerSize5.setSize(TEST_Size_NAME3);
		providerSize5.setProviderType(pt2);
		providerSize5 = providerSizeService.create(providerSize5);

		assertEquals(3, providerSizeService.findByProviderTypeId(pt.getUuid()).size());
		
		assertEquals(3, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		OrgProviderSizeMapping orgmap = new OrgProviderSizeMapping();
		orgmap.setUuid(UUID.randomUUID().toString());
		orgmap.setOrgId(ORG_ID);
		orgmap.setProviderSize(providerSize);
		orgProviderSizeMappingService.create(orgmap);
		OrgProviderSizeMapping orgmap2 = new OrgProviderSizeMapping();
		orgmap2.setUuid(UUID.randomUUID().toString());
		orgmap2.setOrgId(ORG_ID);
		orgmap2.setProviderSize(providerSize2);
		orgProviderSizeMappingService.create(orgmap2);
		OrgProviderSizeMapping orgmap4 = new OrgProviderSizeMapping();
		orgmap4.setUuid(UUID.randomUUID().toString());
		orgmap4.setOrgId(ORG_ID);
		orgmap4.setProviderSize(providerSize4);
		orgProviderSizeMappingService.create(orgmap4);
		OrgProviderSizeMapping orgmap5 = new OrgProviderSizeMapping();
		orgmap5.setUuid(UUID.randomUUID().toString());
		orgmap5.setOrgId(ORG_ID);
		orgmap5.setProviderSize(providerSize5);
		orgProviderSizeMappingService.create(orgmap5);
		assertEquals(2, orgProviderSizeMappingService.findByOrgIdAndTypeId(ORG_ID, pt.getUuid()).size());
		assertEquals(4, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		
		DesignProviderSizeMapping designmap = new DesignProviderSizeMapping();
		designmap.setUuid(UUID.randomUUID().toString());
		designmap.setServiceDesignId(DESIGN_ID);
		designmap.setProviderSize(providerSize);
		designProviderSizeMappingService.create(designmap);
		DesignProviderSizeMapping designmap3 = new DesignProviderSizeMapping();
		designmap3.setUuid(UUID.randomUUID().toString());
		designmap3.setServiceDesignId(DESIGN_ID);
		designmap3.setProviderSize(providerSize3);
		designProviderSizeMappingService.create(designmap3);
		DesignProviderSizeMapping designmap4 = new DesignProviderSizeMapping();
		designmap4.setUuid(UUID.randomUUID().toString());
		designmap4.setServiceDesignId(DESIGN_ID);
		designmap4.setProviderSize(providerSize4);
		designProviderSizeMappingService.create(designmap4);
		DesignProviderSizeMapping designmap5 = new DesignProviderSizeMapping();
		designmap5.setUuid(UUID.randomUUID().toString());
		designmap5.setServiceDesignId(DESIGN_ID);
		designmap5.setProviderSize(providerSize5);
		designProviderSizeMappingService.create(designmap5);
		assertEquals(2, designProviderSizeMappingService.findByDesignIdAndTypeId(DESIGN_ID, pt2.getUuid()).size());
		assertEquals(4, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		assertEquals(1, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		providerSize = providerSizeService.findById(uuid);
		providerSizeService.delete(providerSize);
		assertEquals(3, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		assertEquals(3, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		orgProviderSizeMappingService.deleteByOrgIdAndProviderId(ORG_ID, pt2.getUuid());
		assertEquals(1, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		
		assertEquals(0, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		designProviderSizeMappingService.deleteByDesignIdAndProviderId(DESIGN_ID, pt2.getUuid());
		assertEquals(1, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		assertEquals(0, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
	}
	
	@Test
	@Ignore
	public void testCascadeDelete() {
		providerSize = providerSizeService.create(providerSize);

		String uuid = providerSize.getUuid();
		System.out.println("New created provider Size uuid -- " + uuid);

		ProviderSize providerSize2 = new ProviderSize();
		providerSize2.setUuid(UUID.randomUUID().toString());
		providerSize2.setSize(TEST_Size_NAME2);
		providerSize2.setProviderType(pt);
		providerSize2 = providerSizeService.create(providerSize2);
		String uuid2 = providerSize2.getUuid();
		
		ProviderSize providerSize3 = new ProviderSize();
		providerSize3.setUuid(UUID.randomUUID().toString());
		providerSize3.setSize(TEST_Size_NAME3);
		providerSize3.setProviderType(pt);
		providerSize3 = providerSizeService.create(providerSize3);
		String uuid3 = providerSize3.getUuid();

		assertEquals(3, providerSizeService.findByProviderTypeId(pt.getUuid()).size());
		
		assertEquals(3, providerSizeService.findByProviderTypeWithConstraint(TEST_IAAS_PROVIDER, "default", ORG_ID, DESIGN_ID).size());
		
		List<String> orgSize = new ArrayList<String>();
		orgSize.add(uuid);
		orgSize.add(uuid2);
		orgProviderSizeMappingService.createByOrgId(ORG_ID, orgSize);
		assertEquals(2, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		
		List<String> designSize = new ArrayList<String>();
		designSize.add(uuid);
		designSize.add(uuid3);
		designProviderSizeMappingService.createByDesignId(DESIGN_ID, designSize);
		assertEquals(2, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		providerSize = providerSizeService.findById(uuid);
		providerSizeService.delete(providerSize);
		assertEquals(1, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		assertEquals(1, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
		
		providerTypeService.deleteByType(TEST_IAAS_PROVIDER);
		assertEquals(0,	providerTypeService.findAll().size());
		assertEquals(0, providerSizeService.findAll().size());
		assertEquals(0, orgProviderSizeMappingService.findByOrgId(ORG_ID).size());
		assertEquals(0, designProviderSizeMappingService.findByDesignId(DESIGN_ID).size());
	}
}
