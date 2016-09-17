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
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;

@ContextConfiguration(APP_CTX_LOCATION)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectLdapMappingServiceImplTest {

	@Autowired
    private ProjectService projectService;
	@Autowired
    private ProjectLdapMappingService projectLdapService;

    private Project project;

    @Before
    public void before() {
        project = new Project();
        project.setUuid(UUID.randomUUID().toString());
        project.setName("testproject");
        project.setCsaOrganizationId("ff808081428eb41d0142fa6cfbb0591f");
    }

    @After
    public void after() {

    }

    @Test
    public void testCRUD() {
        project = projectService.create(project);

        ProjectLdapMapping projectLdap = new ProjectLdapMapping();
        String prjLdapUuid = UUID.randomUUID().toString();
        projectLdap.setUuid(prjLdapUuid);
        projectLdap.setProject(project);
        projectLdap.setType("csa");
        projectLdap.setLdapDn("ou=Groups,o=hp.com");
        projectLdap = projectLdapService.create(projectLdap);

        projectLdap = projectLdapService.findById(prjLdapUuid);
        assertEquals("csa", projectLdap.getType());
        assertEquals("ff808081428eb41d0142fa6cfbb0591f", projectLdap.getProject().getCsaOrganizationId());

        List<ProjectLdapMapping> projectLdaps = projectLdapService.findByCriteria(null, null, null, null);
        System.out.println(">>>> result of searching all: \n" + projectLdaps);
        assertEquals(1, projectLdaps.size());
        assertEquals("ou=Groups,o=hp.com", projectLdaps.get(0).getLdapDn());

        projectLdaps = projectLdapService.findByCriteria("ff808081428eb41d0142fa6cfbb0591f", null, null, null);
        System.out.println(">>>> result of searching by csaOrgId: \n" + projectLdaps);
        assertEquals(1, projectLdaps.size());
        assertEquals("ou=Groups,o=hp.com", projectLdaps.get(0).getLdapDn());

        projectLdaps = projectLdapService.findByCriteria(null, null, "csa", null);
        System.out.println(">>>> result of searching by type: \n" + projectLdaps);
        assertEquals(1, projectLdaps.size());
        assertEquals("ou=Groups,o=hp.com", projectLdaps.get(0).getLdapDn());

        projectLdaps = projectLdapService.findByCriteria(null, project.getUuid(), "csa", null);
        System.out.println(">>>> result of searching by project id and type: \n" + projectLdaps);
        assertEquals(1, projectLdaps.size());
        assertEquals("ou=Groups,o=hp.com", projectLdaps.get(0).getLdapDn());

        projectLdaps = projectLdapService.findByCriteria("ff808081428eb41d0142fa6cfbb0591f", null, null, new String[] {
            "ou=Groups,o=hp.com"
        });
        System.out.println(">>>> result of searching by csaOrgId, and ldap: \n" + projectLdaps);
        assertEquals(1, projectLdaps.size());
        assertEquals("ou=Groups,o=hp.com", projectLdaps.get(0).getLdapDn());

        projectLdaps = projectLdapService.findByCriteria("ff808081428eb41d0142fa6cfbb0591f", null, "csa", new String[] {
            "ou=Groups,o=hp.com"
        });
        System.out.println(">>>> result of searching by csaOrgId, type, and ldap: \n" + projectLdaps);
        assertEquals(1, projectLdaps.size());
        assertEquals("ou=Groups,o=hp.com", projectLdaps.get(0).getLdapDn());

        projectLdap.setLdapDn("cn=spadmin,ou=Groups,o=hp.com");
        projectLdapService.update(projectLdap);
        assertEquals("cn=spadmin,ou=Groups,o=hp.com", projectLdapService.findById(prjLdapUuid).getLdapDn());

        assertEquals(1, projectLdapService.deleteByProjectId(projectLdap.getProject().getUuid()));
        assertNull(projectLdapService.findById(prjLdapUuid));
    }
}
