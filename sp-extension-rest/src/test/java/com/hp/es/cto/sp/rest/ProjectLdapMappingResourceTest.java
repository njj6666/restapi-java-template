package com.hp.es.cto.sp.rest;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.rest.form.ProjectLdapMappingForm;
import com.hp.es.cto.sp.service.group.ProjectService;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

/**
 * 
 * @author tanxu
 */
public class ProjectLdapMappingResourceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProjectLdapMappingResourceTest.class);

    private Dispatcher dispatcher;
    private ProjectService projectService;

    @Before
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(ProjectLdapMappingResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);

        SpringUtil springUtil = new SpringUtil("testbean.xml");
        projectService = springUtil.getBean(BeanNames.PROJECT_SERVICE);
    }

    @Test
    public void testProjectLdap() throws Exception {
        String csaOrgId = "ff808081428eb41d0142fa6cfbb0591f";
        String ldapDn = "cn=spadmin,ou=Groups,o=hp.com";

        // list all project ldaps (expect none)
        MockHttpRequest request = MockHttpRequest.get("/v1/project-ldaps");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        String respXml = new String(response.getOutput());
        System.out.println(respXml);
        Assert.assertFalse(respXml.contains("project-ldap"));

        // add project ldap
        request = MockHttpRequest.post("/v1/project-ldaps");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.APPLICATION_JSON);
        Project project = new Project();
        project.setUuid(UUID.randomUUID().toString());
        project.setName("testproject");
        project.setCsaOrganizationId(csaOrgId);
        project = projectService.create(project);

        ProjectLdapMappingForm projectLdap = new ProjectLdapMappingForm();
        projectLdap.setLdapDn(ldapDn);
        projectLdap.setType("csa");
        projectLdap.setProjectId(project.getUuid());
        Map<String, ProjectLdapMappingForm> requestMap = new HashMap<String, ProjectLdapMappingForm>();
        requestMap.put("project-ldap", projectLdap);
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayInputStream bais = new ByteArrayInputStream(mapper.writeValueAsBytes(requestMap));
        request.content(bais);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all project ldaps (expect one recorder)
        request = MockHttpRequest.get("/v1/project-ldaps");
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        System.out.println(respXml);
        Assert.assertTrue(respXml.contains(ldapDn));

        // list project ldap by csa org id
        request = MockHttpRequest.get("/v1/project-ldaps?csa_org_id=" + csaOrgId);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        System.out.println(respXml);
        Assert.assertTrue(respXml.contains(ldapDn));

        // list project ldap by csa org id and ldap dn
        String uri = "/v1/project-ldaps?csa_org_id=" + csaOrgId + "&ldap_dns="
                + URLEncoder.encode(Base64.encodeBase64String(ldapDn.getBytes("UTF-8")), "UTF-8");
        System.out.println("====uri: " + uri);
        request = MockHttpRequest.get(uri);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        System.out.println(respXml);
        Assert.assertTrue(respXml.contains(ldapDn));
    }

}
