package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;
import com.hp.es.cto.sp.rest.form.ProjectForm;
import com.hp.es.cto.sp.rest.form.ProjectLdapMappingForm;
import com.hp.es.cto.sp.service.group.ProjectLdapMappingService;
import com.hp.es.cto.sp.service.group.ProjectService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for project management
 * 
 * @author Nick
 * 
 */
@Named
@Path(ProjectResource.URI)
@Api(value = ProjectResource.URI, description = "Project Management API")
public class ProjectResource extends SPResource {
	private static final Logger logger = LoggerFactory.getLogger(ProjectResource.class);

	public static final String URI = "/v1/projects";
	@Inject
	private ProjectService projectService;
	@Inject
	private ProjectLdapMappingService projectLdapService;

	@GET
	@ApiOperation(value = "Get projects", notes = "Get all projects by conditions, if no conditions will return all projects", response = ProjectForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProjectForm> findAll(@ApiParam(value = "Project ldap mapping type, can be 'csauser' or 'spuser' ") @QueryParam("type") String type,
			@ApiParam(value = "The id of organization which project belongs to") @QueryParam("csa_org_id") String csaOrgId,
			@ApiParam(value = "The ladp dns which project has assoicated") @QueryParam("ldap_dns") String ldapDnList,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(type) && isNullOrEmpty(csaOrgId) && isNullOrEmpty(ldapDnList)) {
			return copyProjectList(projectService.findAll());
		}

		String[] ldapDns = null;
		if (!isNullOrEmpty(ldapDnList)) {
			ldapDns = ContextNodeResource.makeGroupDnList(ldapDnList).toArray(new String[0]);
		}
		List<ProjectLdapMapping> projectLdaps = projectLdapService.findByCriteria(csaOrgId, null, type, ldapDns);
		Set<Project> projects = new HashSet<Project>(projectLdaps.size());
		for (ProjectLdapMapping projectLdap : projectLdaps) {
			projects.add(projectLdap.getProject());
		}
		// workaround to get the projects not yet have ldap configure, for CSA
		// query only
		if (!isNullOrEmpty(csaOrgId)) {
			projects.addAll(projectService.findNonLdapProject(csaOrgId));
		}

		return copyProjectList(projects);
	}

	protected static List<ProjectForm> copyProjectList(Collection<Project> projects) {
		List<ProjectForm> forms = new ArrayList<ProjectForm>(projects.size());
		for (Project project : projects) {
			ProjectForm form = new ProjectForm();
			BeanUtils.copyProperties(project, form);
			forms.add(form);
		}
		return forms;
	}

	@GET
	@Path("{uuid}/ldaps")
	@ApiOperation(value = "Get Project ldap dns", notes = "Get ldap dn mapping of the project by id and type", response = ProjectLdapMappingForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProjectLdapMappingForm> findLdapsByProjectId(@ApiParam(value = "The uuid of the project") @PathParam("uuid") String uuid,
			@ApiParam(value = "Project ldap mapping type, can be 'csauser' or 'spuser'") @QueryParam("type") String type,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting project ldap of project_id [{}] and type [{}]", uuid, type);
		List<ProjectLdapMapping> projectLdaps = projectLdapService.findByCriteria(null, uuid, type, null);
		return ProjectLdapMappingResource.copyProjectList(projectLdaps);
	}

	@GET
	@ApiOperation(value = "Get project", notes = "Get project by uuid", response = ProjectForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProjectForm findByUuid(@ApiParam(value = "The uuid of the project") @PathParam("uuid") String uuid) {
		logger.info("Getting project by uuid: {}", uuid);

		Project project = projectService.findById(uuid);
		if (project == null) {
			throwNotFound("project not found by uuid: " + uuid);
		}
		ProjectForm form = new ProjectForm();
		BeanUtils.copyProperties(project, form);
		return form;
	}

	@POST
	@ApiOperation(value = "Create project", notes = "Create new project with project form", response = ProjectForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProjectForm create(@ApiParam(value = "The body of the project form") ProjectForm form, @ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getCsaOrganizationId())) {
			throwBadRequest("CSA Organization ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Project name is required");
		}

		Project project = new Project();
		BeanUtils.copyProperties(form, project);
		project.setUuid(UUID.randomUUID().toString());

		try {
			project = projectService.create(project);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Name is duplicated to other project!");
		}

		form = new ProjectForm();
		BeanUtils.copyProperties(project, form);
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the project", notes = "Update the project by uuid and project form", response = ProjectForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProjectForm update(@ApiParam(value = "The uuid of the project for update") @PathParam("uuid") String uuid, @ApiParam(value = "The body of the project form") ProjectForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Updating project by uuid: {}", uuid);
		Project project = projectService.findById(uuid);
		if (project == null) {
			throwNotFound("project not found by uuid " + uuid);
		}

		if (!isNullOrEmpty(form.getName())) {
			project.setName(form.getName());
		}
		if (!isNullOrEmpty(form.getCsaOrganizationId())) {
			project.setCsaOrganizationId(form.getCsaOrganizationId());
		}

		if (form.getNotificationList() != null) {
			project.setNotificationList(form.getNotificationList());
		}

		try {
			project = projectService.update(project);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Name is duplicated to other project!");
		}

		ProjectForm result = new ProjectForm();
		BeanUtils.copyProperties(project, result);
		logger.info("Project {} is updated.", uuid);
		return result;
	}

	@DELETE
	@ApiOperation(value = "Delete the project", notes = "Delete the project by uuid")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "The uuid of the project to delete") @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Deleting project ldap mapping by project id {}", uuid);
		projectLdapService.deleteByProjectId(uuid);

		logger.debug("Deleting project by uuid: {}", uuid);
		Project project = projectService.findById(uuid);
		if (project == null) {
			throwNotFound("Project not found by uuid " + uuid);
		}
		projectService.delete(project);
		logger.debug("Project {} is deleted.", uuid);
		return ok();
	}
}
