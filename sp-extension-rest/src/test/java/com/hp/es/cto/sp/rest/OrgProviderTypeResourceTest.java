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
import com.hp.es.cto.sp.rest.provider.OrgProviderTypeMappingResource;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;

/**
 * 
 * @author Victor
 * 
 */
public class OrgProviderTypeResourceTest {

	private Dispatcher dispatcher;
	private ProviderTypeService providerTypeService;

	private static final String TEST_IAAS_PROVIDER1 = "TEST_IAAS_PROVIDER_01";

	private static final Logger logger = LoggerFactory.getLogger(OrgProviderTypeResourceTest.class);

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(OrgProviderTypeMappingResource.class);
		providerTypeService = noDefaults.getAppContext().getBean(ProviderTypeService.class);
		providerTypeService = noDefaults.getAppContext().getBean(ProviderTypeService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		// Initialize some test data.
		ProviderType pt = new ProviderType();
		pt.setUuid("test_provider_id");
		pt.setSubType(TEST_IAAS_PROVIDER1);
		pt.setType("default");
		providerTypeService.create(pt);

		// Initialize some test data.
		ProviderType ps = new ProviderType();
		ps.setUuid("test_provider_type_id1");
		ps.setType("small");
		ps.setSubType("test_provider_type_id1");
		providerTypeService.create(ps);
		
		ProviderType ps2 = new ProviderType();
		ps2.setUuid("test_provider_type_id2");
		ps.setType("small");
		ps.setSubType("test_provider_type_id2");
		providerTypeService.create(ps2);

	}

	@Test
	public void testFindAll() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.get("/v1/org-provider-types");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(!respXml.contains("org_provider_type"));

		// update provider type
		request = MockHttpRequest.put("/v1/org-provider-types");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.APPLICATION_JSON);
		InputStream is = this.getClass().getResourceAsStream("/provider/update_provider_type_mapping.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
}
