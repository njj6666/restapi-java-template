package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;
import com.hp.es.cto.sp.rest.form.ContextNodeForm;
import com.hp.es.cto.sp.rest.form.ContextNodeLdapMappingForm;
import com.hp.es.cto.sp.rest.form.ContextNodeProviderMappingForm;
import com.hp.es.cto.sp.service.context.ContextNodeLdapMappingService;
import com.hp.es.cto.sp.service.context.ContextNodeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing context Node Mapping.
 * 
 * @author Victor
 */
@Named
@Path(ContextNodeLdapMappingResource.URI)
@Api(value = ContextNodeLdapMappingResource.URI, description = "Context Node and Ldap Dn Mappings Management API")
public class ContextNodeLdapMappingResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ContextNodeLdapMappingResource.class);

	public static final String URI = "/v1/context-ldaps";

	@Inject
	private ContextNodeLdapMappingService contextNodeLdapMappingService;

	@Inject
	private ContextNodeService contextNodeService;

	@POST
	@ApiOperation(value = "Create Context Node and Ldap Dn Mapping", notes = "Create Context Node and Ldap Dn Mapping", response = ContextNodeLdapMappingForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeLdapMappingForm create(@ApiParam(value = "The body of the context node and Ldap Dn mapping form", required = true) @MultipartForm ContextNodeLdapMappingForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String contextNodeId = form.getContextNodeId();
		logger.info("Adding new Context Node Ldap Mapping for MetaData: {}", contextNodeId);
		if (isNullOrEmpty(contextNodeId)) {
			throwBadRequest("Context Node ID is required");
		}
		ContextNode node = contextNodeService.findById(contextNodeId);
		if (node == null) {
			throwBadRequest("context Node is not exist");
		}
		if (isNullOrEmpty(form.getLdapDn())) {
			throwBadRequest("LdapDn is required");
		}

		// decode ldap dn
		String decodeLdapDn = StringUtils.newStringUtf8(Base64.decodeBase64(form.getLdapDn()));

		// set mapping
		ContextNodeLdapMapping contextLdapMapping = new ContextNodeLdapMapping();
		contextLdapMapping.setLdapDn(decodeLdapDn);
		contextLdapMapping.setNode(node);
		contextLdapMapping.setUuid(UUID.randomUUID().toString());
		contextLdapMapping.setCreateDate(new Date());

		contextLdapMapping = contextNodeLdapMappingService.create(contextLdapMapping);

		// set return form
		ContextNodeLdapMappingForm cnf = new ContextNodeLdapMappingForm();
		cnf.setContextNodeId(contextLdapMapping.getNode().getUuid());
		BeanUtils.copyProperties(contextLdapMapping, cnf);
		logger.info("New Context Node  {} is added for MetaData {}", cnf.getUuid(), contextNodeId);
		return cnf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "Update Context Node and ldap dn mapping", notes = "Update Context Node and ldap dn mapping with form", response = ContextNodeLdapMappingForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeLdapMappingForm update(@ApiParam(value = "uuid of the existing context ldap dn mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the context node and ldap dn mapping form", required = true) @MultipartForm ContextNodeLdapMappingForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating context node mapping by uuid: {}", uuid);
		String contextNodeId = form.getContextNodeId();
		if (isNullOrEmpty(contextNodeId)) {
			throwBadRequest("Context Node ID is required");
		}
		ContextNode node = contextNodeService.findById(contextNodeId);
		if (node == null) {
			throwBadRequest("context Node is not exist");
		}
		if (isNullOrEmpty(form.getLdapDn())) {
			throwBadRequest("Ldap DN is required");
		}

		// decode ldap dn
		String decodeLdapDn = StringUtils.newStringUtf8(Base64.decodeBase64(form.getLdapDn()));
		form.setLdapDn(decodeLdapDn);

		ContextNodeLdapMapping contextNodeLdapMapping = contextNodeLdapMappingService.findById(uuid);
		if (contextNodeLdapMapping == null) {
			throwBadRequest("Context Node Ldap Mapping not found by uuid: " + uuid);
		}

		if (!contextNodeId.equals(contextNodeLdapMapping.getNode().getUuid())) {
			throwBadRequest("Context Node ID can't be changed!");
		}

		contextNodeLdapMapping.setLdapDn(form.getLdapDn());
		contextNodeLdapMapping = contextNodeLdapMappingService.update(contextNodeLdapMapping);

		ContextNodeLdapMappingForm cnf = new ContextNodeLdapMappingForm();
		cnf.setContextNodeId(contextNodeLdapMapping.getNode().getUuid());
		BeanUtils.copyProperties(contextNodeLdapMapping, cnf);
		logger.info("Context Node provider MappingInfo {} is updated.", uuid);
		return cnf;
	}

	@DELETE
	@ApiOperation(value = "Delete the context node and ldap dn mapping", notes = "Delete the context node and ldap dn mapping by uuid")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "uuid of the existing context node provider mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting Context Ldap Mapping by uuid: {}", uuid);

		ContextNodeLdapMapping contextLdapMapping = contextNodeLdapMappingService.findById(uuid);
		if (contextLdapMapping == null) {
			throwBadRequest("Context provider Mapping not found by uuid: " + uuid);
		}
		contextNodeLdapMappingService.delete(contextLdapMapping);
		logger.info("Context Node Ldap Mapping {} is deleted.", uuid);
		return ok();
	}

	@GET
	@ApiOperation(value = "Get context node and ldap dn mapping", notes = "Get context node and ldap dn mapping by uuid", response = ContextNodeProviderMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeLdapMappingForm findByUuid(@ApiParam(value = "uuid of the existing context node provider mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Context Node Ldap Mapping by uuid: {}", uuid);

		ContextNodeLdapMapping contextNodeLdapMapping = contextNodeLdapMappingService.findById(uuid);
		if (contextNodeLdapMapping == null) {
			throwBadRequest("Context Node mapping not found by uuid: " + uuid);
		}
		ContextNodeLdapMappingForm cnf = new ContextNodeLdapMappingForm();
		cnf.setContextNodeId(contextNodeLdapMapping.getNode().getUuid());
		BeanUtils.copyProperties(contextNodeLdapMapping, cnf);
		return cnf;
	}

	@GET
	@ApiOperation(value = "Get context node and ldap mappings", notes = "Get context node and ldap mapping by conditions", response = ContextNodeForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ContextNodeLdapMappingForm> findAll(
			@ApiParam(value = "the id of context node contains the ldap mappings, this parameter will be considered first") @QueryParam("context_node_id") String contextNodeId,
			@ApiParam(value = "the ldap dn contained in the mappings") @QueryParam("ldap_dn") String ldapDn,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		List<ContextNodeLdapMapping> contextNodeLdapMappings = new ArrayList<ContextNodeLdapMapping>();
		if (!isNullOrEmpty(contextNodeId)) {
			logger.info("Geting a list of context Node Ldap Mapping by Context Node Id: {}", contextNodeId);
			ContextNode node = contextNodeService.findById(contextNodeId);
			if (node == null) {
				throwBadRequest("context Node is not exist");
			}
			contextNodeLdapMappings = contextNodeLdapMappingService.findByNode(node);
			return copyContextNodeList(contextNodeLdapMappings);
		}

		if (!isNullOrEmpty(ldapDn)) {
			logger.info("Geting a list of context Node Ldap Mapping by ldap Dn: {}", ldapDn);
			// decode ldap dn
			String decodeLdapDn = StringUtils.newStringUtf8(Base64.decodeBase64(ldapDn));
			contextNodeLdapMappings = contextNodeLdapMappingService.findByLdapGroupDn(decodeLdapDn);
			return copyContextNodeList(contextNodeLdapMappings);
		}
		contextNodeLdapMappings = contextNodeLdapMappingService.findAll();
		return copyContextNodeList(contextNodeLdapMappings);
	}

	protected static List<ContextNodeLdapMappingForm> copyContextNodeList(List<ContextNodeLdapMapping> contextNodeLdapMappings) {
		List<ContextNodeLdapMappingForm> forms = new ArrayList<ContextNodeLdapMappingForm>(contextNodeLdapMappings.size());
		for (ContextNodeLdapMapping contextNodeLdapMapping : contextNodeLdapMappings) {
			ContextNodeLdapMappingForm form = new ContextNodeLdapMappingForm();
			form.setContextNodeId(contextNodeLdapMapping.getNode().getUuid());
			BeanUtils.copyProperties(contextNodeLdapMapping, form);
			forms.add(form);
		}
		return forms;
	}

}
