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
 * @author Victor
 */
public class ContextMetaDataResourceTest {

	private static final Logger logger = LoggerFactory
			.getLogger(ContextMetaDataResourceTest.class);

	private Dispatcher dispatcher;

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
				ContextMetaDataResource.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);
	}

	@Test
	public void testContextMetaData() throws URISyntaxException {
		// list all metadata (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/context-metadatas");
		request.accept(MediaType.APPLICATION_XML);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST,
				response.getStatus());

		// list all metadata by provider (expect none)
		request = MockHttpRequest
				.get("/v1/context-metadatas?csa_org_id=ff808081428eb41d0142fa6cfbb0591f");
		request.accept(MediaType.APPLICATION_XML);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("context_metadata"));

		// add metadata
		request = MockHttpRequest.post("/v1/context-metadatas");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream(
				"/add_metadata.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all metadata by org (expect one recorder)
		request = MockHttpRequest
				.get("/v1/context-metadatas?csa_org_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("context_metadata"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		// get single metadata
		request = MockHttpRequest.get("/v1/context-metadatas/" + id);
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// update metadata
		request = MockHttpRequest.put("/v1/context-metadatas/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		is = this.getClass().getResourceAsStream("/update_metadata.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}

	@Test
	public void testUpdateMetaDataWithLevelAndOrgId() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post("/v1/context-metadatas");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream(
				"/add_metadata.txt");
		request.content(is);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all metadata by org (expect one recorder)
		request = MockHttpRequest
				.get("/v1/context-metadatas?csa_org_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("context_metadata"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		request = MockHttpRequest.put("/v1/context-metadatas/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is2 = this.getClass().getResourceAsStream(
				"/update_metadata_level.txt");
		request.content(is2);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST,
				response.getStatus());

		request = MockHttpRequest.put("/v1/context-metadatas/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is3 = this.getClass().getResourceAsStream(
				"/update_metadata_org.txt");
		request.content(is3);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST,
				response.getStatus());
	}
	
	@Test
	public void testDelete() {
		//TODO Add test for Remove
	}

}
