package com.hp.es.cto.sp.service.subscription;

import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class SubscriptionServiceImplTest {
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private SubscriptionServerService subscriptionServerService;
	@Autowired
	private SubscriptionPropertyService subscriptionPropertyService;
	@Autowired
	private SubscriptionServerProductService subscriptionServerProductService;
	@Autowired
	private ProductVersionService productVersionService;
	@Autowired
	private SubscriptionLogService subscriptionLogService;
	@Autowired
	private SessionFactory sessionFactory;
	private Session session;

	// CSA ORG ID
	private static final String OWNER1_ID = "8a80828f470f283901470fb467720001";
	private static final String CSA_ORG1_ID = "8a80828f470f283901470fb467720002";
	private static final String CONTEXT_NODE1_ID = "8a80828f470f283901470fb467720003";
	private static final String PROJECT1 = "project1";

	private static final String OWNER2_ID = "8a80828f470f283901470fb467720004";
	private static final String CSA_ORG2_ID = "8a80828f470f283901470fb467720005";
	private static final String CONTEXT_NODE2_ID = "8a80828f470f283901470fb467720006";
	private static final String PROJECT2 = "project2";

	// Subscription1
	private static final String SUBSCRIPTION1_NAME = "name1";
	private static final String SUBSCRIPTION2_NAME = "name2";

	private static final String SUBSCRIPTION1_STATUS = "online";
	private static final String SUBSCRIPTION2_STATUS = "canceled";

	private static final String PROVIDER = "ecs";
	private static final String SIZE = "small";
	private static final String PUBLIC_IPADDRESS = "16.157.53.157";
	private static final String PRIVATE_IPADDRESS = "192.168.1.2";

	@Before
	public void before() {
		session = SessionFactoryUtils.getSession(sessionFactory, true);
		prepareData();
	}

	@After
	public void after() {
		clearData();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.releaseSession(session, sessionFactory);
	}

	@Test
	public void test() {
		session.setFlushMode(FlushMode.NEVER);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));

		assertEquals(2, subscriptionService.findAll().size());
		
		assertEquals(1, subscriptionService.findByCondition(0, 10, "", "", "", PROJECT1, "", "", "", "", "", "product1", "version1").size());
		
		assertEquals(1, subscriptionService.countByCondition("", "", "", "", "", "", "", "", "", "product1", "version1").intValue());

		assertEquals(2, subscriptionService.findByContextNodeId(0, 10, CONTEXT_NODE1_ID, "", "", "", "", "").size());

		assertEquals(2, subscriptionService.findByContextNodeId(0, 10, CONTEXT_NODE1_ID, "", "2010-10-10", "", "", "").size());

		assertEquals(0, subscriptionService.findByContextNodeId(0, 10, CONTEXT_NODE1_ID, "", "2019-10-10", "", "", "").size());

		assertEquals(2, subscriptionService.findByContextNodeId(0, 10, CONTEXT_NODE1_ID, "", "", "2019-10-10", "", "").size());

		assertEquals(0, subscriptionService.findByContextNodeId(0, 10, CONTEXT_NODE1_ID, "", "", "2010-10-10", "", "").size());

		assertEquals(1, subscriptionService.findByContextNodeId(0, 10, CONTEXT_NODE1_ID, SUBSCRIPTION1_STATUS, "", "", "", "").size());

		assertEquals(1, subscriptionService.findByOrgId(0, 10, CSA_ORG2_ID, "", "", "", "", "").size());

		assertEquals(1, subscriptionService.findByOwnerId(0, 10, OWNER1_ID, "", "", "", "", "").size());

		assertEquals(2, subscriptionService.findByProject(0, 10, PROJECT1, "", "", "", "", "").size());

		assertEquals(2, subscriptionService.findByProject(0, 10, "pro", "", "", "", "", "").size());

		assertEquals(0, subscriptionService.findByProject(0, 10, PROJECT2, "", "", "", "", "").size());

		Subscription subscription1 = subscriptionService.findByOwnerId(0, 10, OWNER1_ID, "", "", "", "", "").get(0);
		Subscription subscription2 = subscriptionService.findByOrgId(0, 10, CSA_ORG2_ID, "", "", "", "", "").get(0);

		assertEquals(3, subscriptionPropertyService.findAll().size());
		assertEquals(2, subscriptionPropertyService.findPropertyBySubscription(subscription1).size());
		assertEquals(2, subscriptionLogService.findLogBySubscription(subscription1).size());
		
		assertEquals(3, subscriptionServerService.findAll().size());
		assertEquals(1, subscriptionServerService.findServerBySubscription(subscription2).size());
		assertEquals(1, subscriptionLogService.findLogBySubscription(subscription2).size());
		SubscriptionServer server = subscriptionServerService.findServerBySubscription(subscription2).get(0);

		assertEquals(4, subscriptionServerProductService.findAll().size());
		assertEquals(2, subscriptionServerProductService.findProductByServer(server).size());

		assertEquals(1, subscription2.getSubscriptionServer().size());
		assertEquals(1, subscription2.getSubscriptionProperty().size());
		SubscriptionProperty subscriptionProperty = subscription2.getSubscriptionProperty().get(0);
		subscriptionProperty.getPropertyName();
		SubscriptionServer subscriptionServer = subscription2.getSubscriptionServer().get(0);
		assertEquals(2, subscription2.getSubscriptionServer().get(0).getProduct().size());
		SubscriptionServerProduct subscriptionServerProduct = subscriptionServer.getProduct().get(0);
		subscriptionServerProduct.getProductName();
		assertEquals(9, subscriptionServerProduct.getProductVersion().length());
		
		ProductVersion productVersion1 = new ProductVersion();
		ProductVersion productVersion2 = new ProductVersion();
		productVersion1.setProductName("oracle");
		productVersion1.setPrimaryVersion("11.2");
		productVersion1 = productVersionService.findVersionByPrimaryVersion(productVersion1);
		assertEquals("11.2.0.4",productVersion1.getFullVersion());
		
		productVersion2.setProductName("oracle");
		productVersion2 = productVersionService.findDefaultVersion(productVersion2);
		assertEquals("11.2.0.4",productVersion2.getFullVersion());
		
		List<ProductVersion> oracleVersions = productVersionService.findVersionByProduct("oracle");
		assertEquals(2,oracleVersions.size());
		

	}

	private void clearData() {
		session.setFlushMode(FlushMode.ALWAYS);
		List<Subscription> list = subscriptionService.findAll();
		for (Subscription subscription : list) {
			subscriptionService.delete(subscription);
		}
	}

	private void prepareData() {
		// Add Subscription1
		Subscription subscription1 = new Subscription();
		subscription1.setUuid(UUID.randomUUID().toString());
		subscription1.setContextNodeId(CONTEXT_NODE1_ID);
		subscription1.setCsaOrgId(CSA_ORG1_ID);
		subscription1.setName(SUBSCRIPTION1_NAME);
		subscription1.setOwnerId(OWNER1_ID);
		subscription1.setStatus(SUBSCRIPTION1_STATUS);
		subscription1.setProject(PROJECT1);
		subscription1.setStartTime(new Date());
		subscriptionService.create(subscription1);

		// Add Log for Subscription1
		SubscriptionLog log1 = new SubscriptionLog();
		log1.setComponent("component1");
		log1.setEvent("Install");
		log1.setLogLevel("INFO");
		log1.setLogTime(new Date());
		log1.setMessage("log1");
		log1.setSubscription(subscription1);
		log1.setDetail("detail");
		log1.setPath("path");
		log1.setTag("tag");
		log1.setUuid(UUID.randomUUID().toString());
		subscriptionLogService.create(log1);

		SubscriptionLog log2 = new SubscriptionLog();
		log2.setComponent("component2");
		log2.setEvent("terminate");
		log2.setLogLevel("INFO");
		log2.setLogTime(new Date());
		log2.setMessage("log2");
		log2.setSubscription(subscription1);
		log2.setUuid(UUID.randomUUID().toString());
		log2.setDetail("detail");
		log2.setPath("path");
		log2.setTag("tag");
		subscriptionLogService.create(log2);

		// Add Subscription2
		Subscription subscription2 = new Subscription();
		subscription2.setUuid(UUID.randomUUID().toString());
		subscription2.setContextNodeId(CONTEXT_NODE1_ID);
		subscription2.setCsaOrgId(CSA_ORG2_ID);
		subscription2.setName(SUBSCRIPTION2_NAME);
		subscription2.setOwnerId(OWNER2_ID);
		subscription2.setStatus(SUBSCRIPTION2_STATUS);
		subscription2.setProject(PROJECT1);
		subscription2.setStartTime(new Date());
		subscriptionService.create(subscription2);

		// Add Log for Subscription2
		SubscriptionLog log21 = new SubscriptionLog();
		log21.setComponent("component1");
		log21.setEvent("Install");
		log21.setLogLevel("INFO");
		log21.setLogTime(new Date());
		log21.setMessage("log1");
		log21.setSubscription(subscription2);
		log21.setUuid(UUID.randomUUID().toString());
		log21.setDetail("detail");
		log21.setPath("path");
		log21.setTag("tag");
		subscriptionLogService.create(log21);

		// Add Subscription1 Property
		SubscriptionProperty sp11 = new SubscriptionProperty();
		sp11.setSubscription(subscription1);
		sp11.setPropertyName("name11");
		sp11.setPropertyValue("value11");
		sp11.setUuid(UUID.randomUUID().toString());
		subscriptionPropertyService.create(sp11);

		SubscriptionProperty sp12 = new SubscriptionProperty();
		sp12.setSubscription(subscription1);
		sp12.setPropertyName("name12");
		sp12.setPropertyValue("value12");
		sp12.setUuid(UUID.randomUUID().toString());
		subscriptionPropertyService.create(sp12);

		// Add Subscription2 Property
		SubscriptionProperty sp2 = new SubscriptionProperty();
		sp2.setSubscription(subscription2);
		sp2.setPropertyName("name2");
		sp2.setPropertyValue("value2");
		sp2.setUuid(UUID.randomUUID().toString());
		subscriptionPropertyService.create(sp2);

		// Add Subscription1 Server
		SubscriptionServer ss1 = new SubscriptionServer();
		ss1.setUuid(UUID.randomUUID().toString());
		ss1.setDns("dns1");
		ss1.setIaasProvider(PROVIDER);
		ss1.setSimplifiedDns("sdns1");
		ss1.setCsaProviderId(UUID.randomUUID().toString());
		ss1.setPrivateIpAddress(PRIVATE_IPADDRESS);
		ss1.setPublicIpAddress(PUBLIC_IPADDRESS);
		ss1.setSize(SIZE);
		ss1.setSubscription(subscription1);
		subscriptionServerService.create(ss1);

		SubscriptionServer ss11 = new SubscriptionServer();
		ss11.setUuid(UUID.randomUUID().toString());
		ss11.setDns("dns1");
		ss11.setIaasProvider(PROVIDER);
		ss11.setSimplifiedDns("sdns1");
		ss11.setCsaProviderId(UUID.randomUUID().toString());
		ss11.setPrivateIpAddress(PRIVATE_IPADDRESS);
		ss11.setPublicIpAddress(PUBLIC_IPADDRESS);
		ss11.setSize(SIZE);
		ss11.setSubscription(subscription1);
		subscriptionServerService.create(ss11);

		// Add Subscription2 Server
		SubscriptionServer ss2 = new SubscriptionServer();
		ss2.setUuid(UUID.randomUUID().toString());
		ss2.setDns("dns1");
		ss2.setIaasProvider(PROVIDER);
		ss2.setSimplifiedDns("sdns1");
		ss2.setCsaProviderId(UUID.randomUUID().toString());
		ss2.setPrivateIpAddress(PRIVATE_IPADDRESS);
		ss2.setPublicIpAddress(PUBLIC_IPADDRESS);
		ss2.setSize(SIZE);
		ss2.setSubscription(subscription2);
		subscriptionServerService.create(ss2);

		// Add Subscription1 Server1 Product
		SubscriptionServerProduct ssp1 = new SubscriptionServerProduct();
		ssp1.setUuid(UUID.randomUUID().toString());
		ssp1.setSubscriptionServer(ss1);
		ssp1.setProductName("product1");
		ssp1.setProductVersion("version1");
		subscriptionServerProductService.create(ssp1);

		// Add Subscription1 Server11 Product
		SubscriptionServerProduct ssp11 = new SubscriptionServerProduct();
		ssp11.setUuid(UUID.randomUUID().toString());
		ssp11.setSubscriptionServer(ss11);
		ssp11.setProductName("product11");
		ssp11.setProductVersion("version11");
		subscriptionServerProductService.create(ssp11);

		// Add Subscription2 Server21 Product
		SubscriptionServerProduct ssp21 = new SubscriptionServerProduct();
		ssp21.setUuid(UUID.randomUUID().toString());
		ssp21.setSubscriptionServer(ss2);
		ssp21.setProductName("product21");
		ssp21.setProductVersion("version21");
		subscriptionServerProductService.create(ssp21);

		SubscriptionServerProduct ssp22 = new SubscriptionServerProduct();
		ssp22.setUuid(UUID.randomUUID().toString());
		ssp22.setSubscriptionServer(ss2);
		ssp22.setProductName("product22");
		ssp22.setProductVersion("version22");
		subscriptionServerProductService.create(ssp22);
		
		// Add oracle 11.2 Product Version 
		ProductVersion pv = new ProductVersion();
		ProductVersion pv2 = new ProductVersion();
		pv.setUuid(UUID.randomUUID().toString());
		pv.setDefaultFlag("Y");
		pv.setFullVersion("11.2.0.4");
		pv.setPrimaryVersion("11.2");
		pv.setProductName("oracle");
		pv.setUrl("abc");
		pv2 = productVersionService.create(pv);
		
		//add oracle 12.1 product version
		ProductVersion pv3 = new ProductVersion();
		pv3.setUuid(UUID.randomUUID().toString());
		pv3.setDefaultFlag("N");
		pv3.setFullVersion("12.1.0.4");
		pv3.setPrimaryVersion("12.1");
		pv3.setProductName("oracle");
		pv3.setUrl("abc");
		productVersionService.create(pv3);
		

	}

}
