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

import com.hp.es.cto.sp.rest.provider.ProviderSharedResResource;

/**
 * @author Victor
 */
public class ProviderSharedResResourceTest {

	private static final Logger logger = LoggerFactory
			.getLogger(ProviderSharedResResourceTest.class);

	private Dispatcher dispatcher;

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
				ProviderSharedResResource.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);
	}

	@Test
	public void testProviderServers() throws URISyntaxException {
		// list all servers (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/provider-shared-resources");
		request.accept(MediaType.APPLICATION_XML);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST,
				response.getStatus());

		// list all servers by provider (expect none)
		request = MockHttpRequest
				.get("/v1/provider-shared-resources?csa_provider_id=ff808081428eb41d0142fa6cfbb0591f");
		request.accept(MediaType.APPLICATION_XML);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("provider_shared_resource"));

		// add server
		request = MockHttpRequest.post("/v1/provider-shared-resources");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream("/add_shared.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all keys by provider (expect one recorder)
		request = MockHttpRequest
				.get("/v1/provider-shared-resources?csa_provider_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_shared_resource"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex+6, endindex);
		
		 // get single server
		 request = MockHttpRequest.get("/v1/provider-shared-resources/"+id);
		 request.accept(MediaType.APPLICATION_JSON);
		 request.contentType(MediaType.MULTIPART_FORM_DATA
		 + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		 response = new MockHttpResponse();
		 dispatcher.invoke(request, response);
		 logger.info(response.toString());
		 Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// update server
		request = MockHttpRequest.put("/v1/provider-shared-resources/"+id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		is = this.getClass().getResourceAsStream("/update_shared.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all keys by provider (expect one recorder)
		request = MockHttpRequest
				.get("/v1/provider-shared-resources?csa_provider_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_shared_resource"));

		 // delete server
		 request = MockHttpRequest.delete("/v1/provider-shared-resources/"+id);
		 request.accept(MediaType.APPLICATION_XML);
		 response = new MockHttpResponse();
		 dispatcher.invoke(request, response);
		 logger.info(response.toString());
		 Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
}
