package com.hp.es.cto.sp.service.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static com.hp.es.cto.sp.service.TestConstants.APP_CTX_LOCATION;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.es.cto.sp.persistence.entity.group.Project;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceImplTest {

	@Autowired
    private ProjectService projectService;

    private Project project;

    private static final String TEST_PROJECT_NAME1 = "testproject1";
    private static final String TEST_PROJECT_LIST1 = "shi.xin@hp.com;esit@hp.com;sp@hp.com";
    private static final String TEST_PROJECT_NAME2 = "testproject2";
    private static final String TEST_CSA_ORG_ID = "ff808081428eb41d0142fa6cfbb0591f";

    @Before
    public void before() {
        project = new Project();
        project.setUuid(UUID.randomUUID().toString());
        project.setName(TEST_PROJECT_NAME1);
        project.setCsaOrganizationId(TEST_CSA_ORG_ID);
        project.setNotificationList(TEST_PROJECT_LIST1);
    }

    @After
    public void after() {

    }

    @Test
    public void testCRUD() {
        project = projectService.create(project);
        assertEquals(TEST_PROJECT_NAME1, project.getName());
        assertEquals(TEST_CSA_ORG_ID, project.getCsaOrganizationId());
        assertEquals(TEST_PROJECT_LIST1, project.getNotificationList());

        String uuid = project.getUuid();
        System.out.println("New created project uuid -- " + uuid);

        project = projectService.findById(uuid);
        assertEquals(TEST_PROJECT_NAME1, project.getName());
        assertEquals(TEST_CSA_ORG_ID, project.getCsaOrganizationId());
        assertEquals(TEST_PROJECT_LIST1, project.getNotificationList());

        List<Project> projects = projectService.findNonLdapProject(TEST_CSA_ORG_ID);
        System.out.println("result of searching non ldap projects: " + projects);
        assertEquals(1, projects.size());

        project.setName(TEST_PROJECT_NAME2);
        projectService.update(project);
        assertEquals(TEST_PROJECT_NAME2, projectService.findById(uuid).getName());

        projectService.delete(project);
        assertNull(projectService.findById(uuid));
    }
}
