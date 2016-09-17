package com.hp.es.cto.sp.rest.provider;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import com.hp.es.cto.sp.persistence.entity.provider.DesignProviderSizeMapping;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.DesignProviderSizeCollectionForm;
import com.hp.es.cto.sp.rest.form.DesignProviderSizeMappingForm;
import com.hp.es.cto.sp.rest.form.ProviderSizeForm;
import com.hp.es.cto.sp.service.provider.DesignProviderSizeMappingService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing Design provider size mapping.
 * 
 * @author Victor
 */
@Named
@Path(DesignProviderSizeMappingResource.URI)
@Api(value = DesignProviderSizeMappingResource.URI, description = "Service Design ProviderSize Mapping Management API")
public class DesignProviderSizeMappingResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(DesignProviderSizeMappingResource.class);
	@Inject
	private DesignProviderSizeMappingService designProviderSizeMappingService;

	public static final String URI = "/v1/design-provider-sizes";

	@GET
	@ApiOperation(value = "Get Design ProviderSizes Mapping", notes = "Get Design ProviderSizes Mapping by organization", response = DesignProviderSizeMappingForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<DesignProviderSizeMappingForm> findAll(@ApiParam(value = "The id of service designs which mapping belongs to", required = true) @QueryParam("design_id") String designId,
			@ApiParam(value = "The id of provider Type which mapping belongs to") @QueryParam("provider_type_id") String typeId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of Design provider size mapping: {} with specific provider type id: {}", designId, typeId);
		List<DesignProviderSizeMapping> providerSizes = new ArrayList<DesignProviderSizeMapping>();

		if (!isNullOrEmpty(designId) && !isNullOrEmpty(typeId)) {
			providerSizes = designProviderSizeMappingService.findByDesignIdAndTypeId(designId, typeId);
		}

		if (!isNullOrEmpty(designId)) {
			providerSizes = designProviderSizeMappingService.findByDesignId(designId);
		}

		return copyProviderKeyList(providerSizes);
	}

	@GET
	@ApiOperation(value = "Get Design ProviderSizes Mapping", notes = "Get Design ProviderSizes Mapping by uuid", response = DesignProviderSizeMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public DesignProviderSizeMappingForm findByUuid(@ApiParam(value = "The id of the mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Size by uuid: {}", uuid);

		DesignProviderSizeMapping providerSize = designProviderSizeMappingService.findById(uuid);
		if (providerSize == null) {
			throwNotFound("Provider Type Size not found by uuid: " + uuid);
		}
		DesignProviderSizeMappingForm form = new DesignProviderSizeMappingForm();
		BeanUtils.copyProperties(providerSize, form);
		form.setProviderSizeId(providerSize.getProviderSize().getUuid());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the Design ProviderType Size Mapping", notes = "Update the Design ProviderType Size Mapping", response = ProviderSizeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(@ApiParam(value = "The body of the mapping form", required = true) DesignProviderSizeCollectionForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getDesignId())) {
			throwBadRequest("Design Id is required");
		}
		logger.debug("Updating Update the Design ProviderType Size Mapping by DesignId: {}", form.getDesignId());

		if (!isNullOrEmpty(form.getProviderTypeId())) {
			try {
				designProviderSizeMappingService.deleteByDesignIdAndProviderId(form.getDesignId(), form.getProviderTypeId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't delete the size mapping");
			}
		}
		else {
			try {
				designProviderSizeMappingService.deleteByDesignId(form.getDesignId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't delete the size mapping");
			}
		}

		try {
			designProviderSizeMappingService.createByDesignId(form.getDesignId(), form.getSizes());
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Can't create the size mapping");
		}

		logger.info("Update the Design ProviderType Size Mapping is updated.");
		return ok();
	}

	private List<DesignProviderSizeMappingForm> copyProviderKeyList(List<DesignProviderSizeMapping> providerSizes) {
		List<DesignProviderSizeMappingForm> forms = new ArrayList<DesignProviderSizeMappingForm>(providerSizes.size());
		for (DesignProviderSizeMapping providerSize : providerSizes) {
			DesignProviderSizeMappingForm form = new DesignProviderSizeMappingForm();
			BeanUtils.copyProperties(providerSize, form);
			form.setProviderSizeId(providerSize.getProviderSize().getUuid());
			forms.add(form);
		}
		return forms;
	}
}
