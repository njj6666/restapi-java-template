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

import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.rest.provider.ProviderRegionResource;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;

/**
 * 
 * @author Dream
 * 
 */
public class ProviderRegionResourceTest {

	private Dispatcher dispatcher;
	private ProviderTypeService providerTypeService;

	private static final String TEST_IAAS_PROVIDER1 = "TEST_IAAS_PROVIDER_01";

	private static final Logger logger = LoggerFactory.getLogger(ProviderRegionResourceTest.class);

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(ProviderRegionResource.class);
		providerTypeService = noDefaults.getAppContext().getBean(ProviderTypeService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		// Initialize some test data.
		ProviderType pt = new ProviderType();
		pt.setUuid("test_provider_id");
		pt.setSubType(TEST_IAAS_PROVIDER1);
		pt.setType("default");
		providerTypeService.create(pt);

	}

	@Test
	public void testFindAll() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.get("/v1/provider-regions");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_region"));

		// add provider region
		request = MockHttpRequest.post("/v1/provider-regions");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		InputStream is = this.getClass().getResourceAsStream("/provider/add_provider_region.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		request = MockHttpRequest.get("/v1/provider-regions");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_region"));

		request = MockHttpRequest.get("/v1/provider-regions?provider_type_id=test_provider_id");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_region"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		request = MockHttpRequest.get("/v1/provider-regions?provider_type_id=test_provider_wrong_id");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_region"));

		// update provider region
		request = MockHttpRequest.put("/v1/provider-regions/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		is = this.getClass().getResourceAsStream("/provider/update_provider_region.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// get by id
		request = MockHttpRequest.get("/v1/provider-regions/" + id);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_region"));

		// delete by id
		request = MockHttpRequest.delete("/v1/provider-regions/" + id);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		request = MockHttpRequest.get("/v1/provider-regions");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_region"));

	}
}
