package com.hp.es.cto.sp.rest;

import java.io.InputStream;
import java.net.URISyntaxException;

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

/**
 * @author Victor
 */
public class SubscriptionResourceTest {

    private static final Logger logger = LoggerFactory
            .getLogger(SubscriptionResourceTest.class);

    private Dispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
                SubscriptionResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
    }

    @After
    public void after() {
    }

    @Test
    public void testSubscription() throws URISyntaxException {
        // list all subscription (expect none)
        MockHttpRequest request = MockHttpRequest.get("/v1/subscriptions");
        request.accept(MediaType.APPLICATION_XML);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all subscription by node (expect none)
        request = MockHttpRequest
                .get("/v1/subscriptions?csa_org_id=ff808081428eb41d0142fa6cfbb0591f");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        String respXml = new String(response.getOutput());
        Assert.assertFalse(respXml.contains("<subscription>"));

        // add subscription
        request = MockHttpRequest.post("/v1/subscriptions");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.APPLICATION_JSON);
        InputStream is = this.getClass().getResourceAsStream(
                "/subscription/add_subscription.json");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all subscription by csa_org_id (expect one recorder)
        request = MockHttpRequest
                .get("/v1/subscriptions?csa_org_id=ff808081428eb41d0142fa6cfbb0591f");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("<subscription>"));

        // list all subscription by owner_id (expect one recorder)
        request = MockHttpRequest
                .get("/v1/subscriptions?owner_id=ff808081428eb41d0142fa6cfbb0591o");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("<subscription>"));

        // list all subscription by project (expect one recorder)
        request = MockHttpRequest
                .get("/v1/subscriptions?project=project1");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("<subscription>"));

        // list all node by subscription_id (expect one recorder)
        request = MockHttpRequest
                .get("/v1/subscriptions?context_node_id=ff808081428eb41d0142fa6cfbb0591c");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("<subscription>"));
        int startindex = respXml.indexOf("<uuid>");
        int endindex = respXml.indexOf("</uuid>");
        String id = respXml.substring(startindex + 6, endindex);

        // get single node
        request = MockHttpRequest.get("/v1/subscriptions/" + id);
        request.accept(MediaType.APPLICATION_JSON);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // update metadata
        request = MockHttpRequest.put("/v1/subscriptions/" + id);
        request.accept(MediaType.APPLICATION_XML);
                request.contentType(MediaType.MULTIPART_FORM_DATA
                + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
        is = this.getClass().getResourceAsStream(
                "/subscription/update_subscription.txt");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // update metadata with json
        request = MockHttpRequest.put("/v1/subscriptions/" + id);
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.APPLICATION_JSON);
        is = this.getClass().getResourceAsStream("/subscription/update_subscription.json");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // delete subscription
        request = MockHttpRequest.delete("/v1/subscriptions/" + id);
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all subscription by node (expect none)
        request = MockHttpRequest
                .get("/v1/subscriptions?context_node_id=ff808081428eb41d0142fa6cfbb0591c");
        request.accept(MediaType.APPLICATION_XML);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertFalse(respXml.contains("<subscription>"));

    }
}
