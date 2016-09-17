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

import com.hp.es.cto.sp.rest.provider.ProviderTypeResource;

/**
 * @author tanxu
 */
public class ProviderTypeResourceTest {

	private static final Logger logger = LoggerFactory.getLogger(ProviderTypeResourceTest.class);

	private Dispatcher dispatcher;

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(ProviderTypeResource.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);
	}

	@Test
	public void testProviderTypes() throws URISyntaxException {
		// list all types (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/provider-types");
		request.accept(MediaType.APPLICATION_XML);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("provider_type"));

		// add provider type
		request = MockHttpRequest.post("/v1/provider-types");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		InputStream is = this.getClass().getResourceAsStream("/provider/add_provider_type.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all types (expect one recorder)
		request = MockHttpRequest.get("/v1/provider-types");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_type"));
		
		request = MockHttpRequest.get("/v1/provider-types?type=testtype&sub_type=test_subtype");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_type"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		request = MockHttpRequest.get("/v1/provider-types?type=testtype&sub_type=test_subtype1");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_type"));

		// update provider type
		request = MockHttpRequest.put("/v1/provider-types/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		is = this.getClass().getResourceAsStream("/provider/update_provider_type.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// get by id
		request = MockHttpRequest.get("/v1/provider-types/" + id);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_sub_type"));

		// delete by id
		request = MockHttpRequest.delete("/v1/provider-types/" + id);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		request = MockHttpRequest.get("/v1/provider-types");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_type"));
	}

}
