package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
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

import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;
import com.hp.es.cto.sp.rest.form.ProjectLdapMappingForm;
import com.hp.es.cto.sp.service.group.ProjectLdapMappingService;
import com.hp.es.cto.sp.service.group.ProjectService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing project ldap mapping.
 * 
 * @author Nick
 */
@Named
@Path(ProjectLdapMappingResource.URI)
@Api(value = ProjectLdapMappingResource.URI, description = "Project ldap mapping API")
public class ProjectLdapMappingResource extends SPResource {
	private static final Logger logger = LoggerFactory.getLogger(ProjectLdapMappingResource.class);

	public static final String URI = "/v1/project-ldaps";
	@Inject
	private ProjectLdapMappingService projectLdapService;

	@Inject
	private ProjectService projectService;

	@GET
	@ApiOperation(value = "Get project ldap mappings", notes = "Get project ldap mappings of specific conditions", response = ProjectLdapMappingForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProjectLdapMappingForm> findAll(@ApiParam(value = "Project ldap mapping type, can be 'csauser' or 'spuser' ", required = true) @QueryParam("type") String type,
			@ApiParam(value = "The id of organization which project belongs to", required = true) @QueryParam("csa_org_id") String csaOrgId,
			@ApiParam(value = "The ladp dns which project has assoicated") @QueryParam("ldap_dns") String ldapDnList,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Query project ldap mapping by csa_org_id [{}] and ldap_dn [{}]", csaOrgId, ldapDnList);
		String[] ldapDns = null;
		if (!isNullOrEmpty(ldapDnList)) {
			ldapDns = ContextNodeResource.makeGroupDnList(ldapDnList).toArray(new String[0]);
		}
		List<ProjectLdapMapping> projectLdaps = projectLdapService.findByCriteria(csaOrgId, null, type, ldapDns);

		return copyProjectList(projectLdaps);
	}

	protected static List<ProjectLdapMappingForm> copyProjectList(List<ProjectLdapMapping> projectLdaps) {
		List<ProjectLdapMappingForm> forms = new ArrayList<ProjectLdapMappingForm>(projectLdaps.size());
		for (ProjectLdapMapping projectLdap : projectLdaps) {
			ProjectLdapMappingForm form = new ProjectLdapMappingForm();
			BeanUtils.copyProperties(projectLdap, form);
			form.setProjectId(projectLdap.getProject().getUuid());
			form.setProjectName(projectLdap.getProject().getName());
			forms.add(form);
		}
		return forms;
	}

	@GET
	@ApiOperation(value = "Get project ldap mapping", notes = "Get project ldap mapping by project uuid", response = ProjectLdapMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProjectLdapMappingForm findByUuid(@ApiParam(value = "The uuid of the project", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting project by uuid: {}", uuid);

		ProjectLdapMapping projectLdap = projectLdapService.findById(uuid);
		if (projectLdap == null) {
			throwNotFound("project ldap mapping not found by uuid: " + uuid);
		}
		ProjectLdapMappingForm form = new ProjectLdapMappingForm();
		BeanUtils.copyProperties(projectLdap, form);
		form.setProjectId(projectLdap.getProject().getUuid());
		return form;
	}

	@POST
	@ApiOperation(value = "Create project ldap mapping", notes = "Create project ldap mapping with form", response = ProjectLdapMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProjectLdapMappingForm create(@ApiParam(value = "The body of the project ldap mapping form") ProjectLdapMappingForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getLdapDn())) {
			throwBadRequest("LDAP DN is required");
		}
		if (isNullOrEmpty(form.getType())) {
			throwBadRequest("Project LDAP Mapping type is required");
		}
		if (isNullOrEmpty(form.getProjectId())) {
			throwBadRequest("Project ID is required");
		}

		Project project = projectService.findById(form.getProjectId());
		if (project == null) {
			throwBadRequest("Project not found by UUID " + form.getUuid());
		}

		ProjectLdapMapping projectLdap = new ProjectLdapMapping();
		BeanUtils.copyProperties(form, projectLdap);
		projectLdap.setUuid(UUID.randomUUID().toString());
		projectLdap.setProject(project);

		projectLdap = projectLdapService.create(projectLdap);

		form = new ProjectLdapMappingForm();
		BeanUtils.copyProperties(projectLdap, form);
		form.setProjectId(projectLdap.getProject().getUuid());
		form.setProjectName(projectLdap.getProject().getName());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update project ldap mapping", notes = "Update project ldap mapping with form", response = ProjectLdapMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProjectLdapMappingForm update(@ApiParam(value = "The uuid of the project ldap mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the project ldap mapping form") ProjectLdapMappingForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Updating project ldap mapping by uuid: {}", uuid);
		ProjectLdapMapping projectLdap = projectLdapService.findById(uuid);
		if (projectLdap == null) {
			throwNotFound("project ldap mapping not found by uuid " + uuid);
		}

		if (!isNullOrEmpty(form.getLdapDn())) {
			projectLdap.setLdapDn(form.getLdapDn());
		}
		if (!isNullOrEmpty(form.getType())) {
			projectLdap.setType(form.getType());
		}
		projectLdap = projectLdapService.update(projectLdap);

		ProjectLdapMappingForm result = new ProjectLdapMappingForm();
		BeanUtils.copyProperties(projectLdap, result);
		result.setProjectId(projectLdap.getProject().getUuid());
		result.setProjectName(projectLdap.getProject().getName());
		logger.info("Project {} is updated.", uuid);
		return result;
	}

	@DELETE
	@ApiOperation(value = "Delete project ldap mapping", notes = "Delete project ldap mapping by uuid")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "The uuid of the project ldap mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Deleting project ldap mapping by uuid: {}", uuid);
		ProjectLdapMapping projectLdap = projectLdapService.findById(uuid);
		if (projectLdap == null) {
			throwNotFound("Project ldap mapping not found by uuid " + uuid);
		}
		projectLdapService.delete(projectLdap);
		logger.debug("Project ldap mapping {} is deleted.", uuid);
		return ok();
	}

}
