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

import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegion;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderRegionForm;
import com.hp.es.cto.sp.service.provider.ProviderRegionService;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing provider region.
 * 
 * @author Dream
 */
@Named
@Path(ProviderRegionResource.URI)
@Api(value = ProviderRegionResource.URI, description = "ProviderRegion Management API")
public class ProviderRegionResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ProviderRegionResource.class);
	@Inject
	private ProviderRegionService providerRegionService;

	@Inject
	private ProviderTypeService providerTypeService;

	public static final String URI = "/v1/provider-regions";

	@GET
	@ApiOperation(value = "Get ProviderRegions", notes = "Get ProviderRegions with condition, if no condition return all", response = ProviderRegionForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderRegionForm> findAll(@ApiParam(value = "the id of provider sub type") @QueryParam("provider_type_id") String iaasProvider,
			@ApiParam(value = "the provider type") @QueryParam("provider_type") String providerType, @ApiParam(value = "the provider sub type") @QueryParam("sub_type") String subType,
			@ApiParam(value = "the id of the csa orgnization which has constraint with the provider size") @QueryParam("org_id") String orgId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of provider regions: {}", iaasProvider);

		List<ProviderRegion> providerRegions;
		if (!isNullOrEmpty(iaasProvider)) {
			providerRegions = providerRegionService.findByProviderTypeId(iaasProvider);
		}
		else if (!isNullOrEmpty(providerType) && !isNullOrEmpty(subType)) {
			if (!isNullOrEmpty(orgId)) {
				providerRegions = providerRegionService.findByProviderTypeWithConstraint(providerType, subType, orgId);
			}
			else {
				providerRegions = providerRegionService.findByProviderType(providerType, subType);
			}
		}
		else if (!isNullOrEmpty(providerType)) {
			providerRegions = providerRegionService.findByProviderType(providerType);
		}
		else {
			providerRegions = providerRegionService.findAll();
		}

		return copyProviderKeyList(providerRegions);
	}

	@GET
	@ApiOperation(value = "Get Provider Type Region", notes = "Get Provider Type Region by uuid", response = ProviderRegionForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderRegionForm findByUuid(@ApiParam(value = "the id of provider type region", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Region by uuid: {}", uuid);

		ProviderRegion providerRegion = providerRegionService.findById(uuid);
		if (providerRegion == null) {
			throwNotFound("Provider Region not found by uuid: " + uuid);
		}
		ProviderRegionForm form = new ProviderRegionForm();
		form.setProviderTypeId(providerRegion.getProviderType().getUuid());
		BeanUtils.copyProperties(providerRegion, form);
		return form;
	}

	@POST
	@ApiOperation(value = "Create new ProviderType Region ", notes = "Create new ProviderType Region", response = ProviderRegionForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderRegionForm create(@ApiParam(value = "The body of the provider region form", required = true) ProviderRegionForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getRegionName())) {
			throwBadRequest("RegionName is required");
		}
		if (isNullOrEmpty(form.getRegionDisplayName())) {
			throwBadRequest("Region Display Name is required");
		}
		if (isNullOrEmpty(form.getProviderTypeId())) {
			throwBadRequest("Provider Type ID is required");
		}

		ProviderType providerType = providerTypeService.findById(form.getProviderTypeId());
		if (providerType == null) {
			throwBadRequest("Provider Type can't find! ");
		}

		ProviderRegion providerRegion = new ProviderRegion();
		BeanUtils.copyProperties(form, providerRegion);
		providerRegion.setProviderType(providerType);
		providerRegion.setUuid(UUID.randomUUID().toString());

		try {
			providerRegion = providerRegionService.create(providerRegion);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Region Name is duplicated to other ProviderRegion!");
		}

		form = new ProviderRegionForm();
		form.setProviderTypeId(providerRegion.getProviderType().getUuid());
		BeanUtils.copyProperties(providerRegion, form);
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the ProviderType Region", notes = "Update the ProviderType Region", response = ProviderRegionForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderRegionForm update(@ApiParam(value = "the id of provider type region", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the provider region form", required = true) ProviderRegionForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Updating ProviderRegion by uuid: {}", uuid);
		ProviderRegion providerRegion = providerRegionService.findById(uuid);
		if (providerRegion == null) {
			throwNotFound("Provider Region not found by uuid " + uuid);
		}

		if (!isNullOrEmpty(form.getRegionName())) {
			providerRegion.setRegionName(form.getRegionName());
		}

		if (!isNullOrEmpty(form.getRegionDisplayName())) {
			providerRegion.setRegionDisplayName(form.getRegionDisplayName());
		}

		try {
			providerRegion = providerRegionService.update(providerRegion);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Region Name is duplicated to other ProviderRegion!");
		}

		ProviderRegionForm result = new ProviderRegionForm();
		result.setProviderTypeId(providerRegion.getProviderType().getUuid());
		BeanUtils.copyProperties(providerRegion, result);
		logger.info("ProviderSubRegion {} is updated.", uuid);
		return result;
	}

	@DELETE
	@ApiOperation(value = "Delete the ProviderType Region ", notes = "Delete the ProviderType Region")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteRegion(@ApiParam(value = "the id of provider type region", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Deleting Provider Sub Region by uuid: {}", uuid);
		ProviderRegion providerRegion = providerRegionService.findById(uuid);
		if (providerRegion == null) {
			throwNotFound("ProviderType Region not found by uuid " + uuid);
		}
		providerRegionService.delete(providerRegion);
		logger.debug("ProviderType Region {} is deleted.", uuid);
		return ok();
	}

	private List<ProviderRegionForm> copyProviderKeyList(List<ProviderRegion> providerRegions) {
		List<ProviderRegionForm> forms = new ArrayList<ProviderRegionForm>(providerRegions.size());
		for (ProviderRegion providerRegion : providerRegions) {
			ProviderRegionForm form = new ProviderRegionForm();
			form.setProviderTypeId(providerRegion.getProviderType().getUuid());
			BeanUtils.copyProperties(providerRegion, form);
			forms.add(form);
		}
		return forms;
	}
}
