package com.hp.es.cto.sp.rest;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.service.subscription.ProductVersionService;
import com.hp.es.cto.sp.service.subscription.SubscriptionServerProductService;
import com.hp.es.cto.sp.service.subscription.SubscriptionServerService;
import com.hp.es.cto.sp.service.subscription.SubscriptionService;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

/**
 * @author Victor
 */
public class SubscriptionServerProductResourceTest {

    private static final Logger logger = LoggerFactory
            .getLogger(SubscriptionServerProductResourceTest.class);

    private Dispatcher dispatcher;

    private SubscriptionService subscriptionService;
    private SubscriptionServerService subscriptionServerService;
    private SubscriptionServerProductService subscriptionServerProductService;
    private ProductVersionService productVersionService;
    // CSA ORG ID
    private static final String OWNER1_ID = "8a80828f470f283901470fb467720001";
    private static final String CSA_ORG1_ID = "8a80828f470f283901470fb467720002";
    private static final String CONTEXT_NODE1_ID = "8a80828f470f283901470fb467720003";
    private static final String PROJECT1 = "project1";

    // Subscription1
    private static final String SUBSCRIPTION1_NAME = "name1";

    private static final String SUBSCRIPTION1_STATUS = "online";

    private static final String PROVIDER = "ecs";
    private static final String SIZE = "small";
    private static final String PUBLIC_IPADDRESS = "16.157.53.157";
    private static final String PRIVATE_IPADDRESS = "192.168.1.2";

    @Before
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
                SubscriptionServerProductResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
        // Initialize IoC
        SpringUtil springUtil = new SpringUtil("testbean.xml");
        subscriptionService = springUtil
                .getBean(BeanNames.SUBSCRIPTION_SERVICE);
        subscriptionServerService = springUtil
                .getBean(BeanNames.SUBSCRIPTION_SERVER_SERVICE);
        subscriptionServerProductService = springUtil
                .getBean(BeanNames.SUBSCRIPTION_SERVER_PRODUCT_SERVICE);
        productVersionService = springUtil
                .getBean(BeanNames.PRODUCT_VERSION_SERVICE);
        prepareData();
    }

    @After
    public void after() {
        clearData();
    }

    @Test
    public void testContextNodeProviderMapping() throws URISyntaxException {
        
    	
    	// list all mapping (expect none)
        MockHttpRequest request = MockHttpRequest
                .get("/v1/subscription-server-products");
        request.accept(MediaType.APPLICATION_XML);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        //find all product version
        request = MockHttpRequest
                .get("/v1/subscription-server-products/versions");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        String respXml = new String(response.getOutput());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        
    	// list product versions by product name
        request = MockHttpRequest
                .get("/v1/subscription-server-products/versions/oracle");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        respXml = new String(response.getOutput());
        System.out.println(respXml);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        
        // list all mapping by node (expect none)
        request = MockHttpRequest
                .get("/v1/subscription-server-products?server_id=ff808081428eb41d0142fa6cfbb0591f");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("subscription_server_product"));

        // add mapping - nginx , test default version
        request = MockHttpRequest.post("/v1/subscription-server-products");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.MULTIPART_FORM_DATA
                + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
        InputStream is = this.getClass().getResourceAsStream(
                "/subscription/add_product.txt");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("1.6.2"));
        

        // add mapping - oracle , use primary version 12.1
        request = MockHttpRequest.post("/v1/subscription-server-products");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.APPLICATION_JSON);
        is = this.getClass().getResourceAsStream(
                "/subscription/add_product.json");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("12.1.0.2"));

        // list all mapping by nodes (expect one recorder)
        request = MockHttpRequest
                .get("/v1/subscription-server-products?server_id=ff808081428eb41d0142fa6cfbb0591f");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("subscription_server_product"));
        
        
        // update mapping
        request = MockHttpRequest.put("/v1/subscription-server-products/ff808081428eb41d0142fa6cfbb0591g/13.1");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("13.1"));
        

        // delete mapping
        request = MockHttpRequest
                .delete("/v1/subscription-server-products?server_id=ff808081428eb41d0142fa6cfbb0591f");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all mapping by node (expect none)
        request = MockHttpRequest
                .get("/v1/subscription-server-products?server_id=ff808081428eb41d0142fa6cfbb0591f");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertFalse(respXml.contains("subscription_server_product"));
    
    }

    private void clearData() {
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
        subscriptionService.create(subscription1);

        // Add Subscription1 Server1
        SubscriptionServer ss11 = new SubscriptionServer();
        ss11.setUuid("ff808081428eb41d0142fa6cfbb0591f");
        ss11.setDns("dns1");
        ss11.setIaasProvider(PROVIDER);
        ss11.setSimplifiedDns("sdns1");
        ss11.setCsaProviderId(UUID.randomUUID().toString());
        ss11.setPrivateIpAddress(PRIVATE_IPADDRESS);
        ss11.setPublicIpAddress(PUBLIC_IPADDRESS);
        ss11.setSize(SIZE);
        ss11.setSubscription(subscription1);
        subscriptionServerService.create(ss11);
        
        
     // Add Product Version oracle 11.2
        ProductVersion pv1 = new ProductVersion();
        pv1.setDefaultFlag("Y");
        pv1.setFullVersion("11.2.0.4");
        pv1.setPrimaryVersion("11.2");
        pv1.setProductName("oracle");
        pv1.setUuid(UUID.randomUUID().toString());
        productVersionService.create(pv1);
        
        
     // Add Product Version oracle 12.1
        ProductVersion pv2 = new ProductVersion();
        pv2.setDefaultFlag("N");
        pv2.setFullVersion("12.1.0.2");
        pv2.setPrimaryVersion("12.1");
        pv2.setProductName("oracle");
        pv2.setUuid(UUID.randomUUID().toString());
        productVersionService.create(pv2);
        
     // Add Product Version nginx 1.6
        ProductVersion pv3 = new ProductVersion();
        pv3.setDefaultFlag("Y");
        pv3.setFullVersion("1.6.2");
        pv3.setPrimaryVersion("1.6");
        pv3.setProductName("nginx");
        pv3.setUuid(UUID.randomUUID().toString());
        productVersionService.create(pv3);
        
     // Add Subscription1 Server1 product1 for update test case
        SubscriptionServerProduct ssp111 = new SubscriptionServerProduct();
        ssp111.setUuid("ff808081428eb41d0142fa6cfbb0591g");
        ssp111.setProductName("oracle");
        ssp111.setProductVersion("11.2.0.4");
        ssp111.setSubscriptionServer(ss11);
        subscriptionServerProductService.create(ssp111);
        
        


    }
}
