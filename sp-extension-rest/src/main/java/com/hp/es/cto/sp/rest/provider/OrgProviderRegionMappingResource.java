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

import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderRegionMapping;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.OrgProviderRegionCollectionForm;
import com.hp.es.cto.sp.rest.form.OrgProviderRegionMappingForm;
import com.hp.es.cto.sp.rest.form.ProviderRegionForm;
import com.hp.es.cto.sp.service.provider.OrgProviderRegionMappingService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing org provider region mapping.
 * 
 * @author Victor
 */
@Named
@Path(OrgProviderRegionMappingResource.URI)
@Api(value = OrgProviderRegionMappingResource.URI, description = "Organization ProviderRegion Mapping Management API")
public class OrgProviderRegionMappingResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(OrgProviderRegionMappingResource.class);
	@Inject
	private OrgProviderRegionMappingService orgProviderRegionMappingService;

	public static final String URI = "/v1/org-provider-regions";

	@GET
	@ApiOperation(value = "Get Org ProviderRegions Mapping", notes = "Get Org ProviderRegions Mapping by conditions", response = OrgProviderRegionMappingForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<OrgProviderRegionMappingForm> findAll(@ApiParam(value = "The id of organization which mapping belongs to", required = true) @QueryParam("org_id") String orgId,
			@ApiParam(value = "The id of provider Type which mapping belongs to") @QueryParam("provider_type_id") String typeId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of org provider region mapping: {} with specific provider type id: {}", orgId, typeId);

		List<OrgProviderRegionMapping> providerRegions = new ArrayList<OrgProviderRegionMapping>();
		if (!isNullOrEmpty(orgId) && !isNullOrEmpty(typeId)) {
			providerRegions = orgProviderRegionMappingService.findByOrgIdAndTypeId(orgId, typeId);
		}
		else if (!isNullOrEmpty(orgId)) {
			providerRegions = orgProviderRegionMappingService.findByOrgId(orgId);
		}

		return copyProviderKeyList(providerRegions);
	}

	@GET
	@ApiOperation(value = "Get Org ProviderRegions Mapping", notes = "Get Org ProviderRegions Mapping by uuid", response = OrgProviderRegionMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public OrgProviderRegionMappingForm findByUuid(@ApiParam(value = "The id of the mapping", required =true)  @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Region by uuid: {}", uuid);

		OrgProviderRegionMapping providerRegion = orgProviderRegionMappingService.findById(uuid);
		if (providerRegion == null) {
			throwNotFound("Provider Type Region not found by uuid: " + uuid);
		}
		OrgProviderRegionMappingForm form = new OrgProviderRegionMappingForm();
		BeanUtils.copyProperties(providerRegion, form);
		form.setProviderRegionId(providerRegion.getProviderRegion().getUuid());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the Org ProviderType Region Mapping", notes = "Update the Org ProviderType Region Mapping", response = ProviderRegionForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(@ApiParam(value = "The body of mapping form", required =true) OrgProviderRegionCollectionForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getOrgId())) {
			throwBadRequest("Org Id is required");
		}
		logger.debug("Updating Update the Org ProviderType Region Mapping by orgId: {}", form.getOrgId());

		if (!isNullOrEmpty(form.getProviderTypeId())) {
			try {
				orgProviderRegionMappingService.deleteByOrgIdAndProviderId(form.getOrgId(), form.getProviderTypeId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't delete the region mapping");
			}
		}
		else {
			try {
				orgProviderRegionMappingService.deleteByOrgId(form.getOrgId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't delete the region mapping");
			}
		}

		try {
			orgProviderRegionMappingService.createByOrgId(form.getOrgId(), form.getRegions());
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Can't create the region mapping");
		}

		logger.info("Update the Org ProviderType Region Mapping is updated.");
		return ok();
	}

	private List<OrgProviderRegionMappingForm> copyProviderKeyList(List<OrgProviderRegionMapping> providerRegions) {
		List<OrgProviderRegionMappingForm> forms = new ArrayList<OrgProviderRegionMappingForm>(providerRegions.size());
		for (OrgProviderRegionMapping providerRegion : providerRegions) {
			OrgProviderRegionMappingForm form = new OrgProviderRegionMappingForm();
			BeanUtils.copyProperties(providerRegion, form);
			form.setProviderRegionId(providerRegion.getProviderRegion().getUuid());
			forms.add(form);
		}
		return forms;
	}
}
