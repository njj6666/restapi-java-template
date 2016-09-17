package com.hp.es.cto.sp.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;
import com.hp.es.cto.sp.rest.form.ProjectForm;
import com.hp.es.cto.sp.service.group.ProjectLdapMappingService;
import com.hp.es.cto.sp.service.group.ProjectService;
import com.hp.es.cto.sp.spring.BeanNames;
import com.hp.es.cto.sp.spring.SpringUtil;

/**
 * 
 * @author tanxu
 */
public class ProjectResourceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProjectResourceTest.class);

    private Dispatcher dispatcher;
    private ProjectLdapMappingService projectLdapService;
    private ProjectService projectService;

    @Before
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        SpringPOJOResourceFactory noDefaults = new SpringPOJOResourceFactory(ProjectResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);

        SpringUtil springUtil = new SpringUtil("testbean.xml");
        projectLdapService = springUtil.getBean(BeanNames.PROJECT_LDAP_MAPPING_SERVICE);
        projectService = springUtil.getBean(BeanNames.PROJECT_SERVICE);
    }

    @Test
    public void testProject() throws Exception {
        // list all project (expect none)
        MockHttpRequest request = MockHttpRequest.get("/v1/projects");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        String respXml = new String(response.getOutput());
        assertFalse(respXml.contains("project"));

        // add project
        request = MockHttpRequest.post("/v1/projects");
        request.accept(MediaType.APPLICATION_XML);
        request.contentType(MediaType.APPLICATION_JSON);
        InputStream is = this.getClass().getResourceAsStream("/add_project.json");
        request.content(is);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        logger.info(response.toString());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // list all project (expect one recorder)
        request = MockHttpRequest.get("/v1/projects");
        request.accept(MediaType.APPLICATION_JSON);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ArrayList<HashMap<String, ProjectForm>>> typeRef = new TypeReference<ArrayList<HashMap<String, ProjectForm>>>() {
        };
        String res = new String(response.getOutput(), "UTF-8");
        System.out.println(">>>> find project body: \n" + res);
        ArrayList<HashMap<String, ProjectForm>> forms = mapper.readValue(res, typeRef);
        System.out.println("projects: " + forms);
        assertEquals(1, forms.size());
        String projectId = forms.get(0).get("project").getUuid();
        assertNotNull(projectId);

        // list all project ldaps (expect one recorder)
        Project prj = projectService.findById(projectId);
        assertNotNull(prj);
        ProjectLdapMapping prjLdap = new ProjectLdapMapping();
        prjLdap.setProject(prj);
        prjLdap.setUuid(UUID.randomUUID().toString());
        prjLdap.setType("csa");
        prjLdap.setLdapDn("cn=project-test-spuser,ou=Groups,o=hp.com");
        projectLdapService.create(prjLdap);

        request = MockHttpRequest.get("/v1/projects/" + projectId + "/ldaps?type=csa");
        request.accept(MediaType.APPLICATION_JSON);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        res = new String(response.getOutput(), "UTF-8");
        System.out.println(">>>> find project ldaps body: \n" + res);

        // delete project
        request = MockHttpRequest.delete("/v1/projects/" + projectId);
        response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        respXml = new String(response.getOutput());
        assertFalse(respXml.contains("project"));
    }

}
