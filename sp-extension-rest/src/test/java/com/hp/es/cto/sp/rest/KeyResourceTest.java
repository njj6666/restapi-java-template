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

import com.hp.es.cto.sp.rest.provider.KeyResource;
import com.hp.es.cto.sp.rest.provider.ProviderKeyResource;

/**
 * 
 * @author tanxu
 */
public class KeyResourceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(KeyResourceTest.class);
	
	private Dispatcher dispatcher;

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(KeyResource.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);
        noDefaults = new SpringPOJOResourceFactory(ProviderKeyResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
	}

	@Test
	public void testProviderKeys() throws URISyntaxException {
        // list all keys (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/keys/providers/ff808081428eb41d0142fa6cfbb0591f");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        String respXml = new String(response.getOutput());
        Assert.assertFalse(respXml.contains("ff808081428eb41d0142fa6cfbb0591f"));

        // add user key
        request = MockHttpRequest.post("/v1/provider-keys");
        request.contentType(MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
        InputStream is = this.getClass().getResourceAsStream("/add_provider_key.txt");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all provider keys by provider id (expected one recorder)
        request = MockHttpRequest.get("/v1/keys/providers/ff808081428eb41d0142fa6cfbb0591f");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        // TODO parse the xml response or marshall to object, rather than regex match
        Assert.assertTrue(respXml.contains("ff808081428eb41d0142fa6cfbb0591f"));
    }

}
