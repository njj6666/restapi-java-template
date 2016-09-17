package com.hp.es.cto.sp.rest;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

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

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.service.subscription.SubscriptionService;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

/**
 * @author Victor
 */
public class SubscriptionPropertyResourceTest {

    private static final Logger logger = LoggerFactory
            .getLogger(SubscriptionPropertyResourceTest.class);

    private Dispatcher dispatcher;

    private SubscriptionService subscriptionService;

    // CSA ORG ID
    private static final String OWNER1_ID = "8a80828f470f283901470fb467720001";
    private static final String CSA_ORG1_ID = "8a80828f470f283901470fb467720002";
    private static final String CONTEXT_NODE1_ID = "8a80828f470f283901470fb467720003";
    private static final String PROJECT1 = "project1";

    // Subscription1
    private static final String SUBSCRIPTION1_NAME = "name1";

    private static final String SUBSCRIPTION1_STATUS = "online";

    @Before
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
                SubscriptionPropertyResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
        // Initialize IoC
        SpringUtil springUtil = new SpringUtil("testbean.xml");
        subscriptionService = springUtil
                .getBean(BeanNames.SUBSCRIPTION_SERVICE);
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
                .get("/v1/subscription-properties");
        request.accept(MediaType.APPLICATION_XML);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all mapping by node (expect none)
        request = MockHttpRequest
                .get("/v1/subscription-properties?subscription_id=ff808081428eb41d0142fa6cfbb0591f");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        String respXml = new String(response.getOutput());
        Assert.assertFalse(respXml.contains("subscription_property"));

        // add mapping
        request = MockHttpRequest.post("/v1/subscription-properties");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.MULTIPART_FORM_DATA
                + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
        InputStream is = this.getClass().getResourceAsStream(
                "/subscription/add_property.txt");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // add mapping with json
        request = MockHttpRequest.post("/v1/subscription-properties");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.APPLICATION_JSON);
        is = this.getClass().getResourceAsStream(
                "/subscription/add_property.json");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all mapping by nodes (expect one recorder)
        request = MockHttpRequest
                .get("/v1/subscription-properties?subscription_id=ff808081428eb41d0142fa6cfbb0591f");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("subscription_property"));
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
        subscription1.setUuid("ff808081428eb41d0142fa6cfbb0591f");
        subscription1.setContextNodeId(CONTEXT_NODE1_ID);
        subscription1.setCsaOrgId(CSA_ORG1_ID);
        subscription1.setName(SUBSCRIPTION1_NAME);
        subscription1.setOwnerId(OWNER1_ID);
        subscription1.setStatus(SUBSCRIPTION1_STATUS);
        subscription1.setProject(PROJECT1);
        subscriptionService.create(subscription1);
    }
}
