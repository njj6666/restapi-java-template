package com.hp.es.cto.sp.rest;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.service.context.ContextMetaDataService;
import com.hp.es.cto.sp.service.context.ContextNodeLdapMappingService;
import com.hp.es.cto.sp.service.context.ContextNodeProviderMappingService;
import com.hp.es.cto.sp.service.context.ContextNodeService;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

/**
 * @author Victor
 */
public class ContextNodeResourceTest {

	private static final Logger logger = LoggerFactory
			.getLogger(ContextNodeResourceTest.class);

	private Dispatcher dispatcher;

	private ContextMetaDataService contextMetaDataService;
	
	private ContextNodeService contextNodeService;

	// CSA ORG ID
	private static final String CSA_ORG_ID = "8a80828f470f283901470fb467720003";

	// META DATA Level
	private static final String CONTEXT_META_DATA_NAME_ORG = "SP_RS";

	private static final int LEVEL_ZERO = 0;

	@Before
	public void setup() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(
				ContextNodeResource.class);
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
	public void testContextNode() throws URISyntaxException {
		// list all node (expect none)
		MockHttpRequest request = MockHttpRequest.get("/v1/context-nodes");
		request.accept(MediaType.APPLICATION_XML);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("context-node"));

		// list all node by metadata (expect none)
		request = MockHttpRequest
				.get("/v1/context-nodes?context_metadata_id=ff808081428eb41d0142fa6cfbb0591f");
		request.accept(MediaType.APPLICATION_XML);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertFalse(respXml.contains("context-node"));

		// add node
		request = MockHttpRequest.post("/v1/context-nodes");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream(
				"/add_contextnode.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all node by context_metadata_id (expect one recorder)
		request = MockHttpRequest
				.get("/v1/context-nodes?context_metadata_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("context_node"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		// get single node
		request = MockHttpRequest.get("/v1/context-nodes/" + id);
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// update metadata
		request = MockHttpRequest.put("/v1/context-nodes/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		is = this.getClass().getResourceAsStream("/update_contextnode.txt");
		request.content(is);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}

	@Test
	public void testUpdateNodeaWithMetaData() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post("/v1/context-nodes");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream(
				"/add_contextnode.txt");
		request.content(is);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all metadata by org (expect one recorder)
		request = MockHttpRequest
				.get("/v1/context-nodes?context_metadata_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("context_node"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		request = MockHttpRequest.put("/v1/context-nodes/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is2 = this.getClass().getResourceAsStream(
				"/update_nodes_metadataid.txt");
		request.content(is2);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST,
				response.getStatus());
	}

	@Test
	public void testUpdateNodeaWithParentID() throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post("/v1/context-nodes");
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is = this.getClass().getResourceAsStream(
				"/add_contextnode_parentid.txt");
		request.content(is);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// list all metadata by org (expect one recorder)
		request = MockHttpRequest
				.get("/v1/context-nodes?context_metadata_id=ff808081428eb41d0142fa6cfbb0591f");
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		String respXml = new String(response.getOutput());
		Assert.assertTrue(respXml.contains("context_node"));
		int startindex = respXml.indexOf("<uuid>");
		int endindex = respXml.indexOf("</uuid>");
		String id = respXml.substring(startindex + 6, endindex);

		request = MockHttpRequest.put("/v1/context-nodes/" + id);
		request.accept(MediaType.APPLICATION_XML);
		request.contentType(MediaType.MULTIPART_FORM_DATA
				+ "; boundary=----WebKitFormBoundaryxmsrVjZxadPVoUFZ");
		InputStream is2 = this.getClass().getResourceAsStream(
				"/update_nodes_parentid.txt");
		request.content(is2);
		response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logger.info(response.toString());
		Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST,
				response.getStatus());
	}

	@Test
	public void testDecodeLdapDn() throws URISyntaxException {
		String originLdapDn = "cn=test1,ou=groups,o=hp.com;cn=test2,ou=groups,o=hp.com";
		String encodeDn = StringUtils.newStringUtf8(Base64
				.encodeBase64(originLdapDn.getBytes()));
		System.out.print(encodeDn);
		List<String> groupDnList = ContextNodeResource
				.makeGroupDnList(encodeDn);
		Assert.assertEquals(6, groupDnList.size());
	}
	
	private void clearData() {
		contextMetaDataService.deleteByOrgId(CSA_ORG_ID);
	}

	private void prepareData() {
		// Add org
		ContextMetaData orgContextMataData = new ContextMetaData();
		orgContextMataData.setUuid("ff808081428eb41d0142fa6cfbb0591f");
		orgContextMataData.setCsaOrgId(CSA_ORG_ID);
		orgContextMataData.setLevel(LEVEL_ZERO);
		orgContextMataData.setName(CONTEXT_META_DATA_NAME_ORG);
		orgContextMataData = contextMetaDataService.create(orgContextMataData);
	}

}
