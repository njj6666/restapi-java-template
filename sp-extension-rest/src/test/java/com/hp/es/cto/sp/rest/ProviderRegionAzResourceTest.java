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

import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegion;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.rest.provider.ProviderRegionAzResource;
import com.hp.es.cto.sp.service.provider.ProviderRegionService;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;

/**
 * 
 * @author Victor
 * 
 */
public class ProviderRegionAzResourceTest {

	private Dispatcher dispatcher;
	private ProviderTypeService providerTypeService;
	private ProviderRegionService providerRegionService;
	
	private static final String TEST_IAAS_PROVIDER1 = "TEST_IAAS_PROVIDER_01";

	private static final Logger logger = LoggerFactory.getLogger(ProviderRegionAzResourceTest.class);

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(ProviderRegionAzResource.class);
		providerTypeService = noDefaults.getAppContext().getBean(ProviderTypeService.class);
		providerRegionService = noDefaults.getAppContext().getBean(ProviderRegionService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		// Initialize some test data.
		ProviderType pt = new ProviderType();
		pt.setUuid("test_provider_id");
		pt.setSubType(TEST_IAAS_PROVIDER1);
		pt.setType("default");
		providerTypeService.create(pt);
		
		ProviderRegion pr = new ProviderRegion();
		pr.setProviderType(pt);
		pr.setRegionName("test_provider_region_id");
		pr.setUuid("test_provider_region_id");
		providerRegionService.create(pr);

	}

	@Test
	public void testFindAll() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.get("/v1/provider-region-azs");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_region_az"));

		// add provider region
		request = MockHttpRequest.post("/v1/provider-region-azs");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		InputStream is = this.getClass().getResourceAsStream("/provider/add_provider_region_az.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		request = MockHttpRequest.get("/v1/provider-region-azs");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_region_az"));

		request = MockHttpRequest.get("/v1/provider-region-azs?region_id=test_provider_region_id");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_region_az"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		request = MockHttpRequest.get("/v1/provider-region-azs?region_id=test_provider_wrong_id");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_region_az"));

		// update provider region
		request = MockHttpRequest.put("/v1/provider-region-azs/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		is = this.getClass().getResourceAsStream("/provider/update_provider_region_az.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// get by id
		request = MockHttpRequest.get("/v1/provider-region-azs/" + id);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("provider_region_az"));

		// delete by id
		request = MockHttpRequest.delete("/v1/provider-region-azs/" + id);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		request = MockHttpRequest.get("/v1/provider-region-azs");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("provider_region_az"));

	}
}
