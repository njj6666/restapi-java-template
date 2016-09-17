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

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;
import com.hp.es.cto.sp.rest.form.ContextNodeForm;
import com.hp.es.cto.sp.rest.form.ContextNodeProviderMappingForm;
import com.hp.es.cto.sp.service.context.ContextNodeProviderMappingService;
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
@Path(ContextNodeProviderMappingResource.URI)
@Api(value = ContextNodeProviderMappingResource.URI, description = "Context Node and CSA Resource Provider Mapping Management API")
public class ContextNodeProviderMappingResource extends SPResource {
	private final Logger logger = LoggerFactory
			.getLogger(ContextNodeProviderMappingResource.class);
	
	public static final String URI = "/v1/context-providers";

	@Inject
	private ContextNodeProviderMappingService contextNodeProviderMappingService;
	
	@Inject
	private ContextNodeService contextNodeService;

	@POST
	@ApiOperation(value = "Create Context Node and CSA Resource Provider Mapping", notes = "Create Context Node and CSA Resource Provider Mapping", response = ContextNodeProviderMappingForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeProviderMappingForm create(
			@ApiParam(value = "The body of the context node and resource provider mapping form", required = true)  @MultipartForm ContextNodeProviderMappingForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String contextNodeId = form.getContextNodeId();
		logger.info(
				"Adding new Context Node Provider Mapping for MetaData: {}",
				contextNodeId);
		if (isNullOrEmpty(contextNodeId)) {
			throwBadRequest("Context Node ID is required");
		}
		ContextNode node = contextNodeService.findById(contextNodeId);
		if (node==null) {
			throwBadRequest("context Node is not exist");
		}
		if (isNullOrEmpty(form.getCsaProviderId())) {
			throwBadRequest("ProviderId is required");
		}

		ContextNodeProviderMapping contextProviderMapping = new ContextNodeProviderMapping();
		contextProviderMapping.setCsaProviderId(form.getCsaProviderId());
		contextProviderMapping.setNode(node);
		contextProviderMapping.setUuid(UUID.randomUUID().toString());
		contextProviderMapping.setCreateDate(new Date());

		contextProviderMapping = contextNodeProviderMappingService
				.create(contextProviderMapping);

		ContextNodeProviderMappingForm cnf = new ContextNodeProviderMappingForm();
		cnf.setContextNodeId(contextProviderMapping.getNode().getUuid());
		BeanUtils.copyProperties(contextProviderMapping, cnf);
		logger.info("New Context Node  {} is added for MetaData {}",
				cnf.getUuid(), contextNodeId);
		return cnf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "Update Context Node and csa resource provider mapping", notes = "Update Context Node and csa resource provider mapping with form", response = ContextNodeProviderMappingForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeProviderMappingForm update(
			@ApiParam(value = "uuid of the existing context node provider mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the context node and resource provider mapping form", required = true) @MultipartForm ContextNodeProviderMappingForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating context node mapping by uuid: {}", uuid);
		String contextNodeId = form.getContextNodeId();
		if (isNullOrEmpty(contextNodeId)) {
			throwBadRequest("Context Node ID is required");
		}
		ContextNode node = contextNodeService.findById(contextNodeId);
		if (node==null) {
			throwBadRequest("context Node is not exist");
		}
		if (isNullOrEmpty(form.getCsaProviderId())) {
			throwBadRequest("ProviderId is required");
		}

		ContextNodeProviderMapping contextNodeProviderMapping = contextNodeProviderMappingService
				.findById(uuid);
		if (contextNodeProviderMapping == null) {
			throwBadRequest("Context Node Provider Mapping not found by uuid: "
					+ uuid);
		}

		if (!contextNodeId
				.equals(contextNodeProviderMapping.getNode().getUuid())) {
			throwBadRequest("Context Node ID can't be changed!");
		}

		contextNodeProviderMapping.setCsaProviderId(form.getCsaProviderId());
		contextNodeProviderMapping = contextNodeProviderMappingService
				.update(contextNodeProviderMapping);

		ContextNodeProviderMappingForm cnf = new ContextNodeProviderMappingForm();
		cnf.setContextNodeId(contextNodeProviderMapping.getNode().getUuid());
		BeanUtils.copyProperties(contextNodeProviderMapping, cnf);
		logger.info("Context Node provider MappingInfo {} is updated.", uuid);
		return cnf;
	}

	@DELETE
	@ApiOperation(value = "Delete the context node and resource provider mapping", notes = "Delete the context node and resource provider mapping by uuid")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "uuid of the existing context node provider mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting Context Provider Mapping by uuid: {}", uuid);

		ContextNodeProviderMapping contextProviderMapping = contextNodeProviderMappingService
				.findById(uuid);
		if (contextProviderMapping == null) {
			throwBadRequest("Context provider Mapping not found by uuid: "
					+ uuid);
		}
		contextNodeProviderMappingService.delete(contextProviderMapping);
		logger.info("Context Node Provider Mapping {} is deleted.", uuid);
		return ok();
	}

	@GET
	@ApiOperation(value = "Get context node and resource provider mapping", notes = "Get context node and resource provider mapping by uuid", response = ContextNodeProviderMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeProviderMappingForm findByUuid(
			@ApiParam(value = "uuid of the existing context node provider mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Context Node Provider Mapping by uuid: {}", uuid);

		ContextNodeProviderMapping contextNodeProviderMapping = contextNodeProviderMappingService
				.findById(uuid);
		if (contextNodeProviderMapping == null) {
			throwBadRequest("Context Node mapping not found by uuid: " + uuid);
		}
		ContextNodeProviderMappingForm cnf = new ContextNodeProviderMappingForm();
		cnf.setContextNodeId(contextNodeProviderMapping.getNode().getUuid());
		BeanUtils.copyProperties(contextNodeProviderMapping, cnf);
		return cnf;
	}

	@GET
	@ApiOperation(value = "Get context node and resource provider mappings", notes = "Get context node and resource provider mapping by conditions", response = ContextNodeForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ContextNodeProviderMappingForm> findAllByContextNodeId(
			@ApiParam(value = "the id of context node contains the resource provider mappings", required = true) @QueryParam("context_node_id") String contextNodeId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info(
				"Geting a list of context Node Provider Mapping by context Node id: {}",
				contextNodeId);
		if (isNullOrEmpty(contextNodeId)) {
			throwBadRequest("context Node Id is required");
		}
		
		ContextNode node = contextNodeService.findById(contextNodeId);
		if (node==null) {
			throwBadRequest("context Node is not exist");
		}
		
		List<ContextNodeProviderMapping> contextNodeProviderMappings = contextNodeProviderMappingService
				.findProviderByNode(node);
		return copyContextNodeList(contextNodeProviderMappings);
	}

	protected static List<ContextNodeProviderMappingForm> copyContextNodeList(
			List<ContextNodeProviderMapping> contextNodeProviderMappings) {
		List<ContextNodeProviderMappingForm> forms = new ArrayList<ContextNodeProviderMappingForm>(
				contextNodeProviderMappings.size());
		for (ContextNodeProviderMapping contextNodeProviderMapping : contextNodeProviderMappings) {
			ContextNodeProviderMappingForm form = new ContextNodeProviderMappingForm();
			form.setContextNodeId(contextNodeProviderMapping.getNode().getUuid());
			BeanUtils.copyProperties(contextNodeProviderMapping, form);
			forms.add(form);
		}
		return forms;
	}
}
