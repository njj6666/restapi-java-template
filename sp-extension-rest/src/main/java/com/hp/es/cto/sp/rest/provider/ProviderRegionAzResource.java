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
import com.hp.es.cto.sp.persistence.entity.provider.ProviderRegionAz;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderRegionAzForm;
import com.hp.es.cto.sp.service.provider.ProviderRegionAzService;
import com.hp.es.cto.sp.service.provider.ProviderRegionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing provider region.
 * 
 * @author Victor
 */
@Named
@Path(ProviderRegionAzResource.URI)
@Api(value = ProviderRegionAzResource.URI, description = "ProviderRegionAz Management API")
public class ProviderRegionAzResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ProviderRegionAzResource.class);
	@Inject
	private ProviderRegionAzService providerRegionAzService;

	@Inject
	private ProviderRegionService providerRegionService;

	public static final String URI = "/v1/provider-region-azs";

	@GET
	@ApiOperation(value = "Get ProviderRegionAzs", notes = "Get ProviderRegionAzs with condition, if no condition return all", response = ProviderRegionAzForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderRegionAzForm> findAll(@ApiParam(value = "the id of provider type region") @QueryParam("region_id") String regionId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of provider region azs: {}", regionId);

		List<ProviderRegionAz> providerRegionAzs;
		if (!isNullOrEmpty(regionId)) {
			providerRegionAzs = providerRegionAzService.findByRegionId(regionId);
		}
		else {
			providerRegionAzs = providerRegionAzService.findAll();
		}

		return copyProviderKeyList(providerRegionAzs);
	}

	@GET
	@ApiOperation(value = "Get Provider Type Region Az", notes = "Get Provider Type Region Az by uuid", response = ProviderRegionAzForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderRegionAzForm findByUuid(@ApiParam(value = "the id of provider type region az", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Region Az by uuid: {}", uuid);

		ProviderRegionAz providerRegionAz = providerRegionAzService.findById(uuid);
		if (providerRegionAz == null) {
			throwNotFound("Provider Type Region Az not found by uuid: " + uuid);
		}
		ProviderRegionAzForm form = new ProviderRegionAzForm();
		form.setAz(providerRegionAz.getAz());
		form.setProviderRegionId(providerRegionAz.getProviderRegion().getUuid());
		form.setUuid(providerRegionAz.getUuid());
		return form;
	}

	@POST
	@ApiOperation(value = "Create new ProviderType Region Az", notes = "Create new ProviderType Region Az", response = ProviderRegionAzForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderRegionAzForm create(@ApiParam(value = "The body of the provider region az form", required = true) ProviderRegionAzForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getProviderRegionId())) {
			throwBadRequest("Region is required");
		}
		if (isNullOrEmpty(form.getAz())) {
			throwBadRequest("az is required");
		}

		ProviderRegion providerRegion = providerRegionService.findById(form.getProviderRegionId());
		if (providerRegion == null) {
			throwNotFound("Provider Region not found by uuid " + form.getProviderRegionId());
		}

		ProviderRegionAz providerRegionAz = new ProviderRegionAz();
		providerRegionAz.setAz(form.getAz());
		providerRegionAz.setProviderRegion(providerRegion);
		providerRegionAz.setUuid(UUID.randomUUID().toString());

		try {
			providerRegionAz = providerRegionAzService.create(providerRegionAz);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("AZ name is duplicated to other ProviderRegion Az!");
		}

		form = new ProviderRegionAzForm();
		form.setAz(providerRegionAz.getAz());
		form.setProviderRegionId(providerRegionAz.getProviderRegion().getUuid());
		form.setUuid(providerRegionAz.getUuid());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the ProviderType Region Az", notes = "Update the ProviderType Region Az", response = ProviderRegionAzForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderRegionAzForm update(@ApiParam(value = "the id of provider type region az", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the provider region az form", required = true) ProviderRegionAzForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Updating ProviderRegionAz by uuid: {}", uuid);
		ProviderRegionAz providerRegionAz = providerRegionAzService.findById(uuid);
		if (providerRegionAz == null) {
			throwNotFound("Provider Region Az not found by uuid " + uuid);
		}

		if (!isNullOrEmpty(form.getAz())) {
			providerRegionAz.setAz(form.getAz());
		}

		try {
			providerRegionAz = providerRegionAzService.update(providerRegionAz);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Az Name is duplicated to other ProviderRegionAz!");
		}

		ProviderRegionAzForm result = new ProviderRegionAzForm();
		result.setAz(providerRegionAz.getAz());
		result.setProviderRegionId(providerRegionAz.getProviderRegion().getUuid());
		result.setUuid(providerRegionAz.getUuid());
		logger.info("ProviderSubRegion {} is updated.", uuid);
		return result;
	}

	@DELETE
	@ApiOperation(value = "Delete the ProviderType Region Az", notes = "Delete the ProviderType Region Az")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteRegionAz(@ApiParam(value = "the id of provider type region az", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Deleting Provider Region Az by uuid: {}", uuid);
		ProviderRegionAz providerRegionAz = providerRegionAzService.findById(uuid);
		if (providerRegionAz == null) {
			throwNotFound("ProviderType Region not found by uuid " + uuid);
		}
		providerRegionAzService.delete(providerRegionAz);
		logger.debug("ProviderType Region Az {} is deleted.", uuid);
		return ok();
	}

	private List<ProviderRegionAzForm> copyProviderKeyList(List<ProviderRegionAz> providerRegionAzs) {
		List<ProviderRegionAzForm> forms = new ArrayList<ProviderRegionAzForm>(providerRegionAzs.size());
		for (ProviderRegionAz providerRegionAz : providerRegionAzs) {
			ProviderRegionAzForm form = new ProviderRegionAzForm();
			form.setAz(providerRegionAz.getAz());
			form.setProviderRegionId(providerRegionAz.getProviderRegion().getUuid());
			form.setUuid(providerRegionAz.getUuid());
			BeanUtils.copyProperties(providerRegionAz, form);
			forms.add(form);
		}
		return forms;
	}
}
