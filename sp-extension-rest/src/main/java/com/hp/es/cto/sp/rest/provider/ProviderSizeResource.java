package com.hp.es.cto.sp.rest.provider;

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
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderSize;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderSizeForm;
import com.hp.es.cto.sp.service.provider.ProviderSizeService;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing provider size.
 * 
 * @author Victor
 */
@Named
@Path(ProviderSizeResource.URI)
@Api(value = ProviderSizeResource.URI, description = "ProviderSize Management API")
public class ProviderSizeResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ProviderSizeResource.class);
	@Inject
	private ProviderSizeService providerSizeService;
	@Inject
	private ProviderTypeService providerTypeService;

	public static final String URI = "/v1/provider-sizes";

	@GET
	@ApiOperation(value = "Get ProviderSizes", notes = "Get ProviderSizes with condition, if no condition return all", response = ProviderSizeForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderSizeForm> findAll(@ApiParam(value = "the id of provider sub type") @QueryParam("provider_type_id") String iaasProvider,
			@ApiParam(value = "the provider type") @QueryParam("provider_type") String providerType, @ApiParam(value = "the provider sub type") @QueryParam("sub_type") String subType,
			@ApiParam(value = "the id of the csa orgnization which has constraint with the provider size") @QueryParam("org_id") String orgId,
			@ApiParam(value = "the id of the service design which has constraint with the provider size") @QueryParam("design_id") String designId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of provider size: {}", iaasProvider);

		List<ProviderSize> providerSizes;
		if (!isNullOrEmpty(iaasProvider)) {
			providerSizes = providerSizeService.findByProviderTypeId(iaasProvider);
		}
		else if (!isNullOrEmpty(orgId) && !isNullOrEmpty(designId) && !isNullOrEmpty(providerType) && !isNullOrEmpty(subType)) {
			providerSizes = providerSizeService.findByProviderTypeWithConstraint(providerType, subType, orgId, designId);
		}
		else if (!isNullOrEmpty(providerType) && !isNullOrEmpty(subType)) {
			providerSizes = providerSizeService.findByProviderType(providerType, subType);
		}
		else {
			providerSizes = providerSizeService.findAll();
		}

		return copyProviderKeyList(providerSizes);
	}

	@GET
	@ApiOperation(value = "Get Provider Type Size", notes = "Get Provider Type Size by uuid", response = ProviderSizeForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSizeForm findByUuid(@ApiParam(value = "the id of provider type size", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Size by uuid: {}", uuid);

		ProviderSize providerSize = providerSizeService.findById(uuid);
		if (providerSize == null) {
			throwNotFound("Provider Type Size not found by uuid: " + uuid);
		}
		ProviderSizeForm form = new ProviderSizeForm();
		BeanUtils.copyProperties(providerSize, form);
		form.setProviderTypeId(providerSize.getProviderType().getUuid());
		return form;
	}

	@POST
	@ApiOperation(value = "Create new ProviderType Size ", notes = "Create new ProviderType Size", response = ProviderSizeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSizeForm create(@ApiParam(value = "The body of the provider size form", required = true) ProviderSizeForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getSize())) {
			throwBadRequest("Size is required");
		}
		if (isNullOrEmpty(form.getProviderTypeId())) {
			throwBadRequest("Provider Type ID is required");
		}

		ProviderType providerType = providerTypeService.findById(form.getProviderTypeId());
		if (providerType == null) {
			throwBadRequest("Provider Type can't find! ");
		}

		ProviderSize providerSize = new ProviderSize();
		BeanUtils.copyProperties(form, providerSize);
		providerSize.setUuid(UUID.randomUUID().toString());
		providerSize.setProviderType(providerType);

		try {
			providerSize = providerSizeService.create(providerSize);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Size or display name is duplicated to other ProviderSize's!");
		}

		form = new ProviderSizeForm();
		BeanUtils.copyProperties(providerSize, form);
		form.setProviderTypeId(providerSize.getProviderType().getUuid());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the ProviderType Size", notes = "Update the ProviderType Size", response = ProviderSizeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSizeForm update(@ApiParam(value = "the id of provider type size", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the provider size form", required = true) ProviderSizeForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Updating ProviderSize by uuid: {}", uuid);
		ProviderSize providerSize = providerSizeService.findById(uuid);
		if (providerSize == null) {
			throwNotFound("Provider Size not found by uuid " + uuid);
		}

		providerSize.setDisplayName(form.getDisplayName());
		providerSize.setFlavor(form.getFlavor());
		providerSize.setSize(form.getSize());
		providerSize.setVcpu(form.getVcpu());
		providerSize.setVmemory(form.getVmemory());

		try {
			providerSize = providerSizeService.update(providerSize);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Size or display name is duplicated to other ProviderSize's!");
		}

		ProviderSizeForm result = new ProviderSizeForm();
		BeanUtils.copyProperties(providerSize, result);
		result.setProviderTypeId(providerSize.getProviderType().getUuid());
		logger.info("ProviderSize {} is updated.", uuid);
		return result;
	}

	@DELETE
	@ApiOperation(value = "Delete the ProviderType Size ", notes = "Delete the ProviderType Size")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteSize(@ApiParam(value = "the id of provider type size", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Deleting Provider Size by uuid: {}", uuid);
		ProviderSize providerSize = providerSizeService.findById(uuid);
		if (providerSize == null) {
			throwNotFound("ProviderType Size not found by uuid " + uuid);
		}
		providerSizeService.delete(providerSize);
		logger.debug("ProviderType Size {} is deleted.", uuid);
		return ok();
	}

	private List<ProviderSizeForm> copyProviderKeyList(List<ProviderSize> providerSizes) {
		List<ProviderSizeForm> forms = new ArrayList<ProviderSizeForm>(providerSizes.size());
		for (ProviderSize providerSize : providerSizes) {
			ProviderSizeForm form = new ProviderSizeForm();
			BeanUtils.copyProperties(providerSize, form);
			form.setProviderTypeId(providerSize.getProviderType().getUuid());
			forms.add(form);
		}
		return forms;
	}
}
