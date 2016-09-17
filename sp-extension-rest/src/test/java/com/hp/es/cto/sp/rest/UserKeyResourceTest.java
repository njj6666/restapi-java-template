package com.hp.es.cto.sp.rest;

import java.io.InputStream;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author tanxu
 */
public class UserKeyResourceTest {
    
    private static final Logger logger = LoggerFactory.getLogger(UserKeyResourceTest.class);
    
    private Dispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(UserKeyResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
    }

    @Test
    public void testListUserKeys() throws URISyntaxException {
        // list all key (expect none)
        MockHttpRequest request = MockHttpRequest.get("/v1/user-keys?email=xuqing.tan@hp.com");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        String respXml = new String(response.getOutput());
        Assert.assertFalse(respXml.contains("user-key"));

        // add user key
        request = MockHttpRequest.post("/v1/user-keys");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
        InputStream is = this.getClass().getResourceAsStream("/add_user_key.txt");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all keys (expect one recorder)
        request = MockHttpRequest.get("/v1/user-keys?email=xuqing.tan@hp.com");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        Assert.assertTrue(respXml.contains("user-key"));
        // TODO parse the response xml with XPath or marshall to object
    }

}
