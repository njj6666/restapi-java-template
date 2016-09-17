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

import com.hp.es.cto.sp.rest.provider.ProviderKeyResource;

/**
 * @author tanxu
 */
public class ProviderKeyResourceTest {

	private static final Logger logger = LoggerFactory.getLogger(ProviderKeyResourceTest.class);

	private Dispatcher dispatcher;

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(ProviderKeyResource.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);
	}

	@Test
	public void testProviderKeys() throws URISyntaxException {
		// list all keys (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/provider-keys");
		request.accept(MediaType.APPLICATION_XML);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("provider-key"));

		// add user key
		request = MockHttpRequest.post("/v1/provider-keys");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream("/add_provider_key.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all keys (expect one recorder)
		request = MockHttpRequest.get("/v1/provider-keys");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider-key"));
		// TODO parse the response xml with XPath or marshall to object
	}

	@Test
	public void testAddProviderKeyWithNoUsername() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post("/v1/provider-keys");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream("/add_provider_key_no_username.txt");
		request.content(is);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testAddProviderKeyWithNoKeyfile() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post("/v1/provider-keys");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream("/add_provider_key_no_keyfile.txt");
		request.content(is);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testAddProviderKeyWithNoPassword() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post("/v1/provider-keys");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream("/add_provider_key_no_password.txt");
		request.content(is);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
}
