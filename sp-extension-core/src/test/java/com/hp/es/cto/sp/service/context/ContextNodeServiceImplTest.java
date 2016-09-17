package com.hp.es.cto.sp.service.context;

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

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ContextNodeServiceImplTest {
	@Autowired
	private ContextMetaDataService contextMetaDataService;
	@Autowired
	private ContextNodeService contextNodeService;
	@Autowired
	private ContextNodeLdapMappingService contextNodeLdapMappingService;
	@Autowired
	private ContextNodeProviderMappingService contextNodeProviderMappingService;

	String nodeDepAuuid = new String();
	String nodeDepBuuid = new String();
	String nodeSolAuuid = new String();
	String nodeSolBuuid = new String();
	String nodeSolCuuid = new String();
	String lifeAuuid = new String();
	String lifeBuuid = new String();
	String lifeCuuid = new String();
	String envAuuid = new String();
	String envBuuid = new String();
	String envCuuid = new String();
	String envDuuid = new String();

	// CSA ORG ID
	private static final String CSA_ORG_ID = "8a80828f470f283901470fb467720003";
	// META DATA Level
	private static final String CONTEXT_META_DATA_NAME_ORG = "SP_RS";
	private static final String CONTEXT_META_DATA_NAME_DEP = "Department";
	private static final String CONTEXT_META_DATA_NAME_SOLUTION = "Solution";
	private static final String CONTEXT_META_DATA_NAME_LIFECYCLE = "Life Cycle";
	private static final String CONTEXT_META_DATA_NAME_ENV = "Environment";

	// Node Name
	private static final String CONTEXT_NODE_NAME_ORG = "SP_RS";

	private static final String CONTEXT_NODE_NAME_DEPA = "dept A";
	private static final String CONTEXT_NODE_NAME_DEPB = "dept B";

	private static final String CONTEXT_NODE_NAME_SOLUTIONA = "solution-a";
	private static final String CONTEXT_NODE_NAME_SOLUTIONB = "solution-b";
	private static final String CONTEXT_NODE_NAME_SOLUTIONC = "solution-c";
	private static final String CONTEXT_NODE_NAME_LIFECYCLEA = "lifecycle-a";
	private static final String CONTEXT_NODE_NAME_LIFECYCLEB = "lifecycle-b";
	private static final String CONTEXT_NODE_NAME_LIFECYCLEC = "lifecycle-c";

	private static final String CONTEXT_NODE_NAME_ENVA = "environment-a";
	private static final String CONTEXT_NODE_NAME_ENVB = "environment-b";
	private static final String CONTEXT_NODE_NAME_ENVC = "environment-c";
	private static final String CONTEXT_NODE_NAME_ENVD = "environment-d";

	private static final String PROVIDER_ENV_SOLUTION_A = "8a80828f4710305801471e5d84220978";
	private static final String PROVIDER_ENV_B = "8a80828f4710305801471e61772f097b";
	private static final String PROVIDER_ENV_C = "8a80828f4710305801471e55f7cf0969";
	private static final String PROVIDER_SOLUTION_C = "8a80828f4710305801471e648913097e";

	private static final String LDAP_ENV_A = "cn=Bellucci-Adm,ou=Bellucci,ou=Groups,o=hp.com";
	private static final String LDAP_LIFE_C = "cn=Bellucci-Adm-sub2,cn=Bellucci-Adm,ou=Bellucci,ou=Groups,o=hp.com";

	private static final int LEVEL_ZERO = 0;
	private static final int LEVEL_ONE = 1;
	private static final int LEVEL_TWO = 2;
	private static final int LEVEL_THREE = 3;
	private static final int LEVEL_FOUR = 4;

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
		prepareData();
	}

	@After
	public void after() {
		clearData();
	}

	@Test
	@Ignore
	public void testFindAllByParentIdAndLdapDns() {
		List<String> ldapDnList = new ArrayList<String>();
		ldapDnList.add("cn=Bellucci-Adm,ou=Bellucci,ou=Groups,o=hp.com");
		List<ContextNode> contextNodes = contextNodeService
				.findAllByParentIdAndLdapDns(nodeDepAuuid, ldapDnList);
		assertEquals(1, contextNodes.size());
		for (ContextNode node : contextNodes) {
			System.out.println(node.getUuid());
			System.out.println(node.getName());
			System.out.println(node.getDisplayName());
		}
	}

	@Test
	@Ignore
	public void testFindProvidersByNodeIdAndLdapDns() {
		List<String> ldapDnList = new ArrayList<String>();
		ldapDnList.add("cn=Bellucci-Admxxx,ou=Bellucci,ou=Groups,o=hp.com");
		List<String> rpIds = contextNodeService
				.findProvidersByNodeIdAndLdapDns(nodeDepAuuid, ldapDnList);
		assertEquals(2, rpIds.size());
		for (String rpid : rpIds) {
			System.out.println(rpid);
		}

		rpIds = contextNodeService.findProvidersByNodeIdAndLdapDns(
				nodeDepBuuid, ldapDnList);
		assertEquals(1, rpIds.size());
		for (String rpid : rpIds) {
			System.out.println(rpid);
		}

		ldapDnList.add("cn=Bellucci-Adm,ou=Bellucci,ou=Groups,o=hp.com");
		rpIds = contextNodeService.findProvidersByNodeIdAndLdapDns(
				nodeDepAuuid, ldapDnList);
		for (String rpid : rpIds) {
			System.out.println(rpid);
		}
		assertEquals(2, rpIds.size());
		
		ldapDnList.add("cn=Bellucci-Adm-sub2,cn=Bellucci-Adm,ou=Bellucci,ou=Groups,o=hp.com");
		rpIds = contextNodeService.findProvidersByNodeIdAndLdapDns(
				nodeDepAuuid, ldapDnList);
		for (String rpid : rpIds) {
			System.out.println(rpid);
		}
		assertEquals(3, rpIds.size());
	}

	@Test
	public void testRemoveAllNodesAndMappingByNodeId() {
		contextNodeService
				.removeAllNodesAndMappingByNodeId(nodeDepAuuid);
		assertEquals(5, contextNodeService.findAll().size());
		assertEquals(1, contextNodeProviderMappingService.findAll().size());
		assertEquals(0, contextNodeLdapMappingService.findAll().size());
	}

	@Test
	public void testFindbyCSAOrgId() {
		List<ContextNode> list = contextNodeService.findByCsaOrgId(CSA_ORG_ID);
		assertEquals(13, list.size());
	}
	
	@Test
	@Ignore
	public void testFindPath() {
		String path = contextNodeService.findContextPathByNodeId(envDuuid);
		assertEquals("", path);
	}
	

	private void clearData() {
		contextMetaDataService.deleteByOrgId(CSA_ORG_ID);
	}

	private void prepareData() {
		// Add org
		ContextMetaData orgContextMataData = new ContextMetaData();
		orgContextMataData.setUuid(UUID.randomUUID().toString());
		orgContextMataData.setCsaOrgId(CSA_ORG_ID);
		orgContextMataData.setLevel(LEVEL_ZERO);
		orgContextMataData.setName(CONTEXT_META_DATA_NAME_ORG);
		orgContextMataData = contextMetaDataService.create(orgContextMataData);

		// add DEP
		ContextMetaData depContextMataData = new ContextMetaData();
		depContextMataData.setUuid(UUID.randomUUID().toString());
		depContextMataData.setCsaOrgId(CSA_ORG_ID);
		depContextMataData.setLevel(LEVEL_ONE);
		depContextMataData.setName(CONTEXT_META_DATA_NAME_DEP);
		depContextMataData = contextMetaDataService.create(depContextMataData);

		// add SOLUTION
		ContextMetaData solContextMataData = new ContextMetaData();
		solContextMataData.setUuid(UUID.randomUUID().toString());
		solContextMataData.setCsaOrgId(CSA_ORG_ID);
		solContextMataData.setLevel(LEVEL_TWO);
		solContextMataData.setName(CONTEXT_META_DATA_NAME_SOLUTION);
		solContextMataData = contextMetaDataService.create(solContextMataData);

		// add Life
		ContextMetaData lifeContextMataData = new ContextMetaData();
		lifeContextMataData.setUuid(UUID.randomUUID().toString());
		lifeContextMataData.setCsaOrgId(CSA_ORG_ID);
		lifeContextMataData.setLevel(LEVEL_THREE);
		lifeContextMataData.setName(CONTEXT_META_DATA_NAME_LIFECYCLE);
		lifeContextMataData = contextMetaDataService.create(lifeContextMataData);

		// add Env
		ContextMetaData envContextMataData = new ContextMetaData();
		envContextMataData.setUuid(UUID.randomUUID().toString());
		envContextMataData.setCsaOrgId(CSA_ORG_ID);
		envContextMataData.setLevel(LEVEL_FOUR);
		envContextMataData.setName(CONTEXT_META_DATA_NAME_ENV);
		envContextMataData = contextMetaDataService.create(envContextMataData);

		// add org node
		ContextNode orgContextNode = new ContextNode();
		orgContextNode.setUuid(UUID.randomUUID().toString());
		orgContextNode.setName(CONTEXT_NODE_NAME_ORG);
		orgContextNode.setMetaData(orgContextMataData);
		orgContextNode = contextNodeService.create(orgContextNode);

		// add depa node
		ContextNode depAContextNode = new ContextNode();
		depAContextNode.setUuid(UUID.randomUUID().toString());
		depAContextNode.setName(CONTEXT_NODE_NAME_DEPA);
		depAContextNode.setDisplayName(CONTEXT_NODE_NAME_DEPA);
		depAContextNode.setParentNode(orgContextNode);
		depAContextNode.setMetaData(depContextMataData);
		depAContextNode = contextNodeService.create(depAContextNode);
		nodeDepAuuid = depAContextNode.getUuid();

		// add depb node
		ContextNode depBContextNode = new ContextNode();
		depBContextNode.setUuid(UUID.randomUUID().toString());
		depBContextNode.setName(CONTEXT_NODE_NAME_DEPB);
		depBContextNode.setDisplayName(CONTEXT_NODE_NAME_DEPB);
		depBContextNode.setParentNode(orgContextNode);
		depBContextNode.setMetaData(depContextMataData);
		depBContextNode = contextNodeService.create(depBContextNode);
		nodeDepBuuid = depBContextNode.getUuid();

		// add solutionA node
		ContextNode solAContextNode = new ContextNode();
		solAContextNode.setUuid(UUID.randomUUID().toString());
		solAContextNode.setName(CONTEXT_NODE_NAME_SOLUTIONA);
		solAContextNode.setDisplayName(CONTEXT_NODE_NAME_SOLUTIONA);
		solAContextNode.setParentNode(depAContextNode);
		solAContextNode.setMetaData(solContextMataData);
		solAContextNode = contextNodeService.create(solAContextNode);
		nodeSolAuuid = solAContextNode.getUuid();

		// add solutionB node
		ContextNode solBContextNode = new ContextNode();
		solBContextNode.setUuid(UUID.randomUUID().toString());
		solBContextNode.setName(CONTEXT_NODE_NAME_SOLUTIONB);
		solBContextNode.setDisplayName(CONTEXT_NODE_NAME_SOLUTIONB);
		solBContextNode.setParentNode(depAContextNode);
		solBContextNode.setMetaData(solContextMataData);
		solBContextNode = contextNodeService.create(solBContextNode);
		nodeSolBuuid = solBContextNode.getUuid();

		// add solutionC node
		ContextNode solCContextNode = new ContextNode();
		solCContextNode.setUuid(UUID.randomUUID().toString());
		solCContextNode.setName(CONTEXT_NODE_NAME_SOLUTIONC);
		solCContextNode.setDisplayName(CONTEXT_NODE_NAME_SOLUTIONC);
		solCContextNode.setParentNode(depBContextNode);
		solCContextNode.setMetaData(solContextMataData);
		solCContextNode = contextNodeService.create(solCContextNode);
		nodeSolCuuid = solCContextNode.getUuid();

		// add LifeCycleA node
		ContextNode lifeAContextNode = new ContextNode();
		lifeAContextNode.setUuid(UUID.randomUUID().toString());
		lifeAContextNode.setName(CONTEXT_NODE_NAME_LIFECYCLEA);
		lifeAContextNode.setDisplayName(CONTEXT_NODE_NAME_LIFECYCLEA);
		lifeAContextNode.setParentNode(solCContextNode);
		lifeAContextNode.setMetaData(lifeContextMataData);
		lifeAContextNode = contextNodeService.create(lifeAContextNode);
		lifeAuuid = lifeAContextNode.getUuid();

		// add LifeCycleB node
		ContextNode lifeBContextNode = new ContextNode();
		lifeBContextNode.setUuid(UUID.randomUUID().toString());
		lifeBContextNode.setName(CONTEXT_NODE_NAME_LIFECYCLEB);
		lifeBContextNode.setDisplayName(CONTEXT_NODE_NAME_LIFECYCLEB);
		lifeBContextNode.setParentNode(solAContextNode);
		lifeBContextNode.setMetaData(lifeContextMataData);
		lifeBContextNode = contextNodeService.create(lifeBContextNode);
		lifeBuuid = lifeBContextNode.getUuid();

		// add LifeCycleC node
		ContextNode lifeCContextNode = new ContextNode();
		lifeCContextNode.setUuid(UUID.randomUUID().toString());
		lifeCContextNode.setName(CONTEXT_NODE_NAME_LIFECYCLEC);
		lifeCContextNode.setDisplayName(CONTEXT_NODE_NAME_LIFECYCLEC);
		lifeCContextNode.setParentNode(solBContextNode);
		lifeCContextNode.setMetaData(lifeContextMataData);
		lifeCContextNode = contextNodeService.create(lifeCContextNode);
		lifeCuuid = lifeCContextNode.getUuid();

		// add ENVA node
		ContextNode envAContextNode = new ContextNode();
		envAContextNode.setUuid(UUID.randomUUID().toString());
		envAContextNode.setName(CONTEXT_NODE_NAME_ENVA);
		envAContextNode.setDisplayName(CONTEXT_NODE_NAME_ENVA);
		envAContextNode.setParentNode(lifeBContextNode);
		envAContextNode.setMetaData(envContextMataData);
		envAContextNode = contextNodeService.create(envAContextNode);
		envAuuid = envAContextNode.getUuid();

		// add ENVB node
		ContextNode envBContextNode = new ContextNode();
		envBContextNode.setUuid(UUID.randomUUID().toString());
		envBContextNode.setName(CONTEXT_NODE_NAME_ENVB);
		envBContextNode.setDisplayName(CONTEXT_NODE_NAME_ENVB);
		envBContextNode.setParentNode(lifeBContextNode);
		envBContextNode.setMetaData(envContextMataData);
		envBContextNode = contextNodeService.create(envBContextNode);
		envBuuid = envBContextNode.getUuid();

		// add ENVC node
		ContextNode envCContextNode = new ContextNode();
		envCContextNode.setUuid(UUID.randomUUID().toString());
		envCContextNode.setName(CONTEXT_NODE_NAME_ENVC);
		envCContextNode.setDisplayName(CONTEXT_NODE_NAME_ENVC);
		envCContextNode.setParentNode(lifeCContextNode);
		envCContextNode.setMetaData(envContextMataData);
		envCContextNode = contextNodeService.create(envCContextNode);
		envCuuid = envCContextNode.getUuid();

		// add ENVD node
		ContextNode envDContextNode = new ContextNode();
		envDContextNode.setUuid(UUID.randomUUID().toString());
		envDContextNode.setName(CONTEXT_NODE_NAME_ENVD);
		envDContextNode.setDisplayName(CONTEXT_NODE_NAME_ENVD);
		envDContextNode.setParentNode(lifeAContextNode);
		envDContextNode.setMetaData(envContextMataData);
		envDContextNode = contextNodeService.create(envDContextNode);
		envDuuid = envDContextNode.getUuid();

		// Add LDAP Mapping
		ContextNodeLdapMapping contextNodeLdapMapping = new ContextNodeLdapMapping();
		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(envAContextNode);
		contextNodeLdapMapping.setLdapDn(LDAP_ENV_A);
		contextNodeLdapMappingService.create(contextNodeLdapMapping);

		contextNodeLdapMapping.setUuid(UUID.randomUUID().toString());
		contextNodeLdapMapping.setNode(lifeCContextNode);
		contextNodeLdapMapping.setLdapDn(LDAP_LIFE_C);
		contextNodeLdapMappingService.create(contextNodeLdapMapping);

		// Add Provider Mapping
		ContextNodeProviderMapping contextNodeProviderMapping = new ContextNodeProviderMapping();
		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(envAContextNode);
		contextNodeProviderMapping.setCsaProviderId(PROVIDER_ENV_SOLUTION_A);
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(solAContextNode);
		contextNodeProviderMapping.setCsaProviderId(PROVIDER_ENV_SOLUTION_A);
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(envBContextNode);
		contextNodeProviderMapping.setCsaProviderId(PROVIDER_ENV_B);
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(envCContextNode);
		contextNodeProviderMapping.setCsaProviderId(PROVIDER_ENV_C);
		contextNodeProviderMappingService.create(contextNodeProviderMapping);

		contextNodeProviderMapping.setUuid(UUID.randomUUID().toString());
		contextNodeProviderMapping.setNode(solCContextNode);
		contextNodeProviderMapping.setCsaProviderId(PROVIDER_SOLUTION_C);
		contextNodeProviderMappingService.create(contextNodeProviderMapping);
	}

}
