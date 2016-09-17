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

import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderSizeMapping;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.OrgProviderSizeCollectionForm;
import com.hp.es.cto.sp.rest.form.OrgProviderSizeMappingForm;
import com.hp.es.cto.sp.rest.form.ProviderSizeForm;
import com.hp.es.cto.sp.service.provider.OrgProviderSizeMappingService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing org provider size mapping.
 * 
 * @author Victor
 */
@Named
@Path(OrgProviderSizeMappingResource.URI)
@Api(value = OrgProviderSizeMappingResource.URI, description = "Organization ProviderSize Mapping Management API")
public class OrgProviderSizeMappingResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(OrgProviderSizeMappingResource.class);
	@Inject
	private OrgProviderSizeMappingService orgProviderSizeMappingService;

	public static final String URI = "/v1/org-provider-sizes";

	@GET
	@ApiOperation(value = "Get Org ProviderSizes Mapping", notes = "Get Org ProviderSizes Mapping by organization", response = OrgProviderSizeMappingForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<OrgProviderSizeMappingForm> findAll(@ApiParam(value = "The id of organization which mapping belongs to", required = true) @QueryParam("org_id") String orgId,
			@ApiParam(value = "The id of provider Type which mapping belongs to") @QueryParam("provider_type_id") String typeId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of org provider size mapping: {} with specific provider type id: {}", orgId, typeId);

		List<OrgProviderSizeMapping> providerSizes = new ArrayList<OrgProviderSizeMapping>();
		if (!isNullOrEmpty(orgId) && !isNullOrEmpty(typeId)) {
			providerSizes = orgProviderSizeMappingService.findByOrgIdAndTypeId(orgId, typeId);
		}
		else if (!isNullOrEmpty(orgId)) {
			providerSizes = orgProviderSizeMappingService.findByOrgId(orgId);
		}

		return copyProviderKeyList(providerSizes);
	}

	@GET
	@ApiOperation(value = "Get Org ProviderSizes Mapping", notes = "Get Org ProviderSizes Mapping by uuid", response = OrgProviderSizeMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public OrgProviderSizeMappingForm findByUuid(@ApiParam(value = "The id of the mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Size by uuid: {}", uuid);

		OrgProviderSizeMapping providerSize = orgProviderSizeMappingService.findById(uuid);
		if (providerSize == null) {
			throwNotFound("Provider Type Size not found by uuid: " + uuid);
		}
		OrgProviderSizeMappingForm form = new OrgProviderSizeMappingForm();
		BeanUtils.copyProperties(providerSize, form);
		form.setProviderSizeId(providerSize.getProviderSize().getUuid());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the Org ProviderType Size Mapping", notes = "Update the Org ProviderType Size Mapping", response = ProviderSizeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(@ApiParam(value = "The body of the mapping form", required = true) OrgProviderSizeCollectionForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getOrgId())) {
			throwBadRequest("Org Id is required");
		}
		logger.debug("Updating Update the Org ProviderType Size Mapping by orgId: {}", form.getOrgId());

		if (!isNullOrEmpty(form.getProviderTypeId())) {
			try {
				orgProviderSizeMappingService.deleteByOrgIdAndProviderId(form.getOrgId(), form.getProviderTypeId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't delete the size mapping");
			}
		}
		else {
			try {
				orgProviderSizeMappingService.deleteByOrgId(form.getOrgId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't delete the size mapping");
			}
		}

		try {
			orgProviderSizeMappingService.createByOrgId(form.getOrgId(), form.getSizes());
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Can't create the size mapping");
		}

		logger.info("Update the Org ProviderType Size Mapping is updated.");
		return ok();
	}

	private List<OrgProviderSizeMappingForm> copyProviderKeyList(List<OrgProviderSizeMapping> providerSizes) {
		List<OrgProviderSizeMappingForm> forms = new ArrayList<OrgProviderSizeMappingForm>(providerSizes.size());
		for (OrgProviderSizeMapping providerSize : providerSizes) {
			OrgProviderSizeMappingForm form = new OrgProviderSizeMappingForm();
			BeanUtils.copyProperties(providerSize, form);
			form.setProviderSizeId(providerSize.getProviderSize().getUuid());
			forms.add(form);
		}
		return forms;
	}
}
