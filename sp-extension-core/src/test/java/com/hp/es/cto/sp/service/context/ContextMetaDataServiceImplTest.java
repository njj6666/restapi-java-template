package com.hp.es.cto.sp.service.context;

import static org.junit.Assert.*;
import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;

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

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ContextMetaDataServiceImplTest {
	private ContextMetaData contextMataData;
	@Autowired
	private ContextMetaDataService contextMetaDataService;
	@Autowired
	private ContextNodeService contextNodeService;
	@Autowired
	private ContextNodeLdapMappingService contextNodeLdapMappingService;
	@Autowired
	private ContextNodeProviderMappingService contextNodeProviderMappingService;

	private static final String CONTEXT_META_DATA_NAME_ORG = "Organization";
	private static final String CONTEXT_NODE_NAME_ORG = "myorg";
	private static final String CONTEXT_META_DATA_NAME_NEW_ORG = "newOrganization";
	private static final String CONTEXT_META_DATA_NAME_DEP = "Department";
	private static final String CONTEXT_NODE_NAME_DEP1 = "dep1";
	private static final String CONTEXT_NODE_NAME_DEP2 = "dep2";
	private static final String CONTEXT_META_DATA_NAME_SOLUTION = "SOLUTION";
	private static final String CONTEXT_NODE_NAME_SOLUTION1 = "solution1";
	private static final String CONTEXT_NODE_NAME_SOLUTION2 = "solution2";
	private static final String CONTEXT_NODE_NAME_SOLUTION3 = "solution3";
	private static final int LEVEL_ZERO = 0;
	private static final int LEVEL_ONE = 1;
	private static final int LEVEL_TWO = 2;

	@Before
	public void before() {
		SpringUtil springUtil = new SpringUtil("testbean.xml");
		contextMetaDataService = springUtil
				.getBean(BeanNames.CONTEXT_METE_DATA_SERVICE);
		contextNodeService = springUtil.getBean(BeanNames.CONTEXT_NODE_SERVICE);
		contextNodeLdapMappingService = springUtil
				.getBean(BeanNames.CONTEXT_NODE_LDAP_MAPPING_SERVICE);
		contextNodeProviderMappingService = springUtil
				.getBean(BeanNames.CONTEXT_NODE_PROVIDER_MAPPING_SERVICE);
		String csaOrgId = UUID.randomUUID().toString();

		// Initial a MetaData object.
		contextMataData = new ContextMetaData();
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setCsaOrgId(csaOrgId);
		contextMataData.setLevel(LEVEL_ZERO);
		contextMataData.setName(CONTEXT_META_DATA_NAME_ORG);
	}

	@After
	public void after() {
	}

	@Test
	public void testCRUD() {
		contextMataData = contextMetaDataService.create(contextMataData);

		String uuid = contextMataData.getUuid();
		System.out.println("New created Mata Data uuid -- " + uuid);

		assertEquals(1, contextMetaDataService.findAll().size());

		contextMataData.setName(CONTEXT_META_DATA_NAME_NEW_ORG);
		contextMetaDataService.update(contextMataData);
		assertEquals(CONTEXT_META_DATA_NAME_NEW_ORG, contextMetaDataService
				.findById(uuid).getName());

		contextMetaDataService.delete(contextMataData);
		assertEquals(0, contextMetaDataService.findAll().size());
	}

	@Test
	public void testGetByOrgId() {
		contextMataData = contextMetaDataService.create(contextMataData);

		String csaOrgId = contextMataData.getCsaOrgId();
		System.out.println("csa org id -- " + csaOrgId);

		assertEquals(1, contextMetaDataService.findByOrgId(csaOrgId).size());

		// add another
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setLevel(LEVEL_ONE);
		contextMataData.setName(CONTEXT_META_DATA_NAME_DEP);
		contextMetaDataService.create(contextMataData);
		assertEquals(2, contextMetaDataService.findByOrgId(csaOrgId).size());

		contextMetaDataService.deleteByOrgId(csaOrgId);
		assertEquals(0, contextMetaDataService.findByOrgId(csaOrgId).size());
	}

	@Test
	public void testGetByMetaDataId() {
		contextMataData = contextMetaDataService.create(contextMataData);
		String rootuuid = contextMataData.getUuid();

		String csaOrgId = contextMataData.getCsaOrgId();
		System.out.println("csa org id -- " + csaOrgId);

		assertEquals(1, contextMetaDataService.findByOrgId(csaOrgId).size());

		// add another
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setLevel(LEVEL_ONE);
		contextMataData.setName(CONTEXT_META_DATA_NAME_DEP);
		contextMetaDataService.create(contextMataData);
		assertEquals(2, contextMetaDataService.findByOrgId(csaOrgId).size());

		contextMetaDataService.deleteAllRelatedContext(rootuuid);
		assertEquals(0, contextMetaDataService.findByOrgId(csaOrgId).size());
	}

	@Test
	public void testDuplicateCreate() {
		contextMataData = contextMetaDataService.create(contextMataData);

		String uuid = contextMataData.getUuid();
		System.out.println("New created meta data uuid -- " + uuid);

		// only change the level
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setLevel(LEVEL_ONE);
		try {
			contextMetaDataService.create(contextMataData);
			assertTrue(false);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// only change the name
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setName(CONTEXT_META_DATA_NAME_DEP);
		contextMataData.setLevel(LEVEL_ZERO);
		try {
			contextMetaDataService.create(contextMataData);
			assertTrue(false);
		} catch (DataIntegrityViolationException e) {
			assertTrue(true);
		} catch (UncategorizedSQLException e) {
			assertTrue(true);
		}

		// only change the org
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setName(CONTEXT_META_DATA_NAME_DEP);
		contextMataData.setCsaOrgId(UUID.randomUUID().toString());
		contextMataData = contextMetaDataService.create(contextMataData);
		String uuid2 = contextMataData.getUuid();

		// delete
		contextMataData.setUuid(uuid);
		contextMetaDataService.delete(contextMataData);
		assertEquals(1, contextMetaDataService.findAll().size());

		contextMataData.setUuid(uuid2);
		contextMetaDataService.delete(contextMataData);
		assertEquals(0, contextMetaDataService.findAll().size());
	}

	@Test
	public void testRemoveAllContextNode() {
		contextMataData = contextMetaDataService.create(contextMataData);

		String csaOrgId = contextMataData.getCsaOrgId();
		System.out.println("csa org id -- " + csaOrgId);

		assertEquals(1, contextMetaDataService.findByOrgId(csaOrgId).size());

		// add org node
		ContextNode orgContextNode = new ContextNode();
		orgContextNode.setUuid(UUID.randomUUID().toString());
		orgContextNode.setName(CONTEXT_NODE_NAME_ORG);
		orgContextNode.setMetaData(contextMataData);
		orgContextNode = contextNodeService.create(orgContextNode);

		// add DEP
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setLevel(LEVEL_ONE);
		contextMataData.setName(CONTEXT_META_DATA_NAME_DEP);
		contextMataData = contextMetaDataService.create(contextMataData);
		assertEquals(2, contextMetaDataService.findByOrgId(csaOrgId).size());

		// add dep1 node
		ContextNode dep1ContextNode = new ContextNode();
		dep1ContextNode.setUuid(UUID.randomUUID().toString());
		dep1ContextNode.setName(CONTEXT_NODE_NAME_DEP1);
		dep1ContextNode.setParentNode(orgContextNode);
		dep1ContextNode.setMetaData(contextMataData);
		dep1ContextNode = contextNodeService.create(dep1ContextNode);

		// add dep2 node
		ContextNode dep2ContextNode = new ContextNode();
		dep2ContextNode.setUuid(UUID.randomUUID().toString());
		dep2ContextNode.setName(CONTEXT_NODE_NAME_DEP2);
		dep2ContextNode.setParentNode(orgContextNode);
		dep2ContextNode.setMetaData(contextMataData);
		dep2ContextNode = contextNodeService.create(dep2ContextNode);

		// add SOLUTION
		contextMataData.setUuid(UUID.randomUUID().toString());
		contextMataData.setLevel(LEVEL_TWO);
		contextMataData.setName(CONTEXT_META_DATA_NAME_SOLUTION);
		contextMataData = contextMetaDataService.create(contextMataData);
		String solutionuuid = contextMataData.getUuid();
		assertEquals(3, contextMetaDataService.findByOrgId(csaOrgId).size());

		// add solution1 node
		ContextNode sol1ContextNode = new ContextNode();
		sol1ContextNode.setUuid(UUID.randomUUID().toString());
		sol1ContextNode.setName(CONTEXT_NODE_NAME_SOLUTION1);
		sol1ContextNode.setParentNode(dep1ContextNode);
		sol1ContextNode.setMetaData(contextMataData);
		sol1ContextNode = contextNodeService.create(sol1ContextNode);

		// add solution2 node
		ContextNode sol2ContextNode = new ContextNode();
		sol2ContextNode.setUuid(UUID.randomUUID().toString());
		sol2ContextNode.setName(CONTEXT_NODE_NAME_SOLUTION2);
		sol2ContextNode.setParentNode(dep1ContextNode);
		sol2ContextNode.setMetaData(contextMataData);
		sol2ContextNode = contextNodeService.create(sol2ContextNode);

		// add solution3 node
		ContextNode sol3ContextNode = new ContextNode();
		sol3ContextNode.setUuid(UUID.randomUUID().toString());
		sol3ContextNode.setName(CONTEXT_NODE_NAME_SOLUTION3);
		sol3ContextNode.setParentNode(dep2ContextNode);
		sol3ContextNode.setMetaData(contextMataData);
		sol3ContextNode = contextNodeService.create(sol3ContextNode);

		// Add Mapping for Dep1
		ContextNodeLdapMapping contextNodeLdapMapping = new ContextNodeLdapMapping();
		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(dep1ContextNode);
		contextNodeLdapMapping.setLdapDn("o=dep1.com");
		contextNodeLdapMappingService.create(contextNodeLdapMapping);
		ContextNodeProviderMapping contextNodeProviderMapping = new ContextNodeProviderMapping();
		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(dep1ContextNode);
		contextNodeProviderMapping.setCsaProviderId(UUID.randomUUID()
				.toString());
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		// Add Mapping for Dep2
		contextNodeLdapMapping = new ContextNodeLdapMapping();
		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(dep2ContextNode);
		contextNodeLdapMapping.setLdapDn("o=dep2.com");
		contextNodeLdapMappingService.create(contextNodeLdapMapping);
		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(dep2ContextNode);
		contextNodeProviderMapping.setCsaProviderId(UUID.randomUUID()
				.toString());
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		// Add Mapping for Solution1
		contextNodeLdapMapping = new ContextNodeLdapMapping();
		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(sol1ContextNode);
		contextNodeLdapMapping.setLdapDn("o=sol1.com");
		contextNodeLdapMappingService.create(contextNodeLdapMapping);
		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(sol1ContextNode);
		contextNodeProviderMapping.setCsaProviderId(UUID.randomUUID()
				.toString());
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		// Add Mapping for Solution2
		contextNodeLdapMapping = new ContextNodeLdapMapping();
		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(sol1ContextNode);
		contextNodeLdapMapping.setLdapDn("o=sol2.com");
		contextNodeLdapMappingService.create(contextNodeLdapMapping);
		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(sol1ContextNode);
		contextNodeProviderMapping.setCsaProviderId(UUID.randomUUID()
				.toString());
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		// Add Mapping for Solution3
		contextNodeLdapMapping = new ContextNodeLdapMapping();
		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(sol3ContextNode);
		contextNodeLdapMapping.setLdapDn("o=sol3.com");
		contextNodeLdapMappingService.create(contextNodeLdapMapping);
		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(sol3ContextNode);
		contextNodeProviderMapping.setCsaProviderId(UUID.randomUUID()
				.toString());
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		assertEquals(3, contextMetaDataService.findByOrgId(csaOrgId).size());
		assertEquals(6, contextNodeService.findAll().size());
		assertEquals(5, contextNodeLdapMappingService.findAll().size());
		assertEquals(5, contextNodeProviderMappingService.findAll().size());

		contextMetaDataService.deleteAllRelatedContext(solutionuuid);
		assertEquals(2, contextMetaDataService.findByOrgId(csaOrgId).size());
		assertEquals(3, contextNodeService.findAll().size());
		assertEquals(2, contextNodeLdapMappingService.findAll().size());
		assertEquals(2, contextNodeProviderMappingService.findAll().size());

		contextMetaDataService.deleteByOrgId(csaOrgId);
		assertEquals(0, contextMetaDataService.findByOrgId(csaOrgId).size());
		assertEquals(0, contextNodeService.findAll().size());
		assertEquals(0, contextNodeLdapMappingService.findAll().size());
		assertEquals(0, contextNodeProviderMappingService.findAll().size());
	}

}
