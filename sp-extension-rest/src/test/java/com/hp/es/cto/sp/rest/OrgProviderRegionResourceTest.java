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
import com.hp.es.cto.sp.rest.provider.OrgProviderRegionMappingResource;
import com.hp.es.cto.sp.service.provider.ProviderRegionService;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;

/**
 * 
 * @author Victor
 * 
 */
public class OrgProviderRegionResourceTest {

	private Dispatcher dispatcher;
	private ProviderTypeService providerTypeService;
	private ProviderRegionService providerRegionService;

	private static final String TEST_IAAS_PROVIDER1 = "TEST_IAAS_PROVIDER_01";

	private static final Logger logger = LoggerFactory.getLogger(OrgProviderRegionResourceTest.class);

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(OrgProviderRegionMappingResource.class);
		providerTypeService = noDefaults.getAppContext().getBean(ProviderTypeService.class);
		providerRegionService = noDefaults.getAppContext().getBean(ProviderRegionService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		// Initialize some test data.
		ProviderType pt = new ProviderType();
		pt.setUuid("test_provider_id");
		pt.setSubType(TEST_IAAS_PROVIDER1);
		pt.setType("default");
		providerTypeService.create(pt);

		// Initialize some test data.
		ProviderRegion ps = new ProviderRegion();
		ps.setUuid("test_provider_region_id1");
		ps.setRegionName("small");
		ps.setProviderType(pt);
		providerRegionService.create(ps);
		
		ProviderRegion ps2 = new ProviderRegion();
		ps2.setUuid("test_provider_region_id2");
		ps2.setRegionName("large");
		ps2.setProviderType(pt);
		providerRegionService.create(ps2);

	}

	@Test
	public void testFindAll() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.get("/v1/org-provider-regions");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("org_provider_region"));

		// update provider region
		request = MockHttpRequest.put("/v1/org-provider-regions");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		InputStream is = this.getClass().getResourceAsStream("/provider/update_provider_region_mapping.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
}
