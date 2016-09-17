package com.hp.es.cto.sp.rest;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.service.context.ContextMetaDataService;
import com.hp.es.cto.sp.service.context.ContextNodeService;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

/**
 * @author Victor
 */
public class ContextNodeLdapMappingResourceTest {

	private static final Logger logger = LoggerFactory
			.getLogger(ContextNodeLdapMappingResourceTest.class);

	private Dispatcher dispatcher;
	
	private ContextMetaDataService contextMetaDataService;
	private ContextNodeService contextNodeService;

	// CSA ORG ID
	private static final String CSA_ORG_ID = "8a80828f470f283901470fb467720003";
	
	// META DATA Level
	private static final String CONTEXT_META_DATA_NAME_ORG = "SP_RS";

	// Node Name
	private static final String CONTEXT_NODE_NAME_ORG = "SP_RS";

	private static final int LEVEL_ZERO = 0;
	
	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
				ContextNodeLdapMappingResource.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);
		// Initialize IoC
		SpringUtil springUtil = new SpringUtil("testbean.xml");
		contextMetaDataService = springUtil
				.getBean(BeanNames.CONTEXT_METE_DATA_SERVICE);
		contextNodeService = springUtil.getBean(BeanNames.CONTEXT_NODE_SERVICE);

		prepareData();
	}
	
	@After
	public void after() {
		clearData();
	}
	
	@Test
	public void testContextNodeProviderMapping() throws URISyntaxException {
		// list all mapping (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/context-ldaps");
		request.accept(MediaType.APPLICATION_XML);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("context_ldap"));

		// list all mapping by node (expect none)
		request = MockHttpRequest
				.get("/v1/context-ldaps?context_node_id=ff808081428eb41d0142fa6cfbb0591n");
		request.accept(MediaType.APPLICATION_XML);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("context_ldap"));

		// add mapping
		request = MockHttpRequest.post("/v1/context-ldaps");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream(
				"/add_ldap_mapping.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all mapping by nodes (expect one recorder)
		request = MockHttpRequest
				.get("/v1/context-ldaps?context_node_id=ff808081428eb41d0142fa6cfbb0591n");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("context_ldap"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		// get single mapping
		request = MockHttpRequest.get("/v1/context-ldaps/" + id);
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// update mapping
		request = MockHttpRequest.put("/v1/context-ldaps/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		is = this.getClass().getResourceAsStream("/update_ldap_mapping.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// delete mapping
		request = MockHttpRequest.delete("/v1/context-ldaps/" + id);
		request.accept(MediaType.APPLICATION_XML);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void clearData() {
		contextMetaDataService.deleteByOrgId(CSA_ORG_ID);
	}
	
	private void prepareData() {
		// Add org
		ContextMetaData orgContextMataData = new ContextMetaData();
		orgContextMataData.setUuid(UUID.randomUUID().toString());
		orgContextMataData.setCsaOrgId(CSA_ORG_ID);
		orgContextMataData.setLevel(LEVEL_ZERO);
		orgContextMataData.setName(CONTEXT_META_DATA_NAME_ORG);
		orgContextMataData = contextMetaDataService.create(orgContextMataData);

		// add org node
		ContextNode orgContextNode = new ContextNode();
		orgContextNode.setUuid("ff808081428eb41d0142fa6cfbb0591n");
		orgContextNode.setName(CONTEXT_NODE_NAME_ORG);
		orgContextNode.setMetaData(orgContextMataData);
		orgContextNode = contextNodeService.create(orgContextNode);
	}

}
