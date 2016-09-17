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

import com.hp.es.cto.sp.persistence.entity.provider.OrgProviderTypeMapping;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.OrgProviderTypeCollectionForm;
import com.hp.es.cto.sp.rest.form.OrgProviderTypeMappingForm;
import com.hp.es.cto.sp.rest.form.ProviderTypeForm;
import com.hp.es.cto.sp.service.provider.OrgProviderTypeMappingService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing org provider type mapping.
 * 
 * @author Victor
 */
@Named
@Path(OrgProviderTypeMappingResource.URI)
@Api(value = OrgProviderTypeMappingResource.URI, description = "Organization ProviderType Mapping Management API")
public class OrgProviderTypeMappingResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(OrgProviderTypeMappingResource.class);
	@Inject
	private OrgProviderTypeMappingService orgProviderTypeMappingService;

	public static final String URI = "/v1/org-provider-types";

	@GET
	@ApiOperation(value = "Get Org ProviderTypes Mapping", notes = "Get Org ProviderTypes Mapping by organization", response = OrgProviderTypeMappingForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<OrgProviderTypeMappingForm> findAll(@ApiParam(value = "The id of organization which mapping belongs to", required = true) @QueryParam("org_id") String orgId,
			@ApiParam(value = "The id of provider Type which mapping belongs to") @QueryParam("provider_type") String type,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of org provider type mapping: {} with specific provider type id: {}", orgId, type);

		List<OrgProviderTypeMapping> providerTypes = new ArrayList<OrgProviderTypeMapping>();
		if (!isNullOrEmpty(orgId) && !isNullOrEmpty(type)) {
			providerTypes = orgProviderTypeMappingService.findByOrgIdAndType(orgId, type);
		}
		else if (!isNullOrEmpty(orgId)) {
			providerTypes = orgProviderTypeMappingService.findByOrgId(orgId);
		}

		return copyProviderKeyList(providerTypes);
	}

	@GET
	@ApiOperation(value = "Get Org ProviderTypes Mapping", notes = "Get Org ProviderTypes Mapping by uuid", response = OrgProviderTypeMappingForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public OrgProviderTypeMappingForm findByUuid(@ApiParam(value = "The id of the mapping", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Type Type by uuid: {}", uuid);

		OrgProviderTypeMapping providerType = orgProviderTypeMappingService.findById(uuid);
		if (providerType == null) {
			throwNotFound("Provider Type Type not found by uuid: " + uuid);
		}
		OrgProviderTypeMappingForm form = new OrgProviderTypeMappingForm();
		BeanUtils.copyProperties(providerType, form);
		form.setProviderTypeId(providerType.getProviderType().getUuid());
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the Org ProviderType Type Mapping", notes = "Update the Org ProviderType Type Mapping", response = ProviderTypeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(@ApiParam(value = "The body of the mapping form", required = true) OrgProviderTypeCollectionForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getOrgId())) {
			throwBadRequest("Org Id is required");
		}
		logger.debug("Updating Update the Org ProviderType Type Mapping by orgId: {}", form.getOrgId());

		if (!isNullOrEmpty(form.getProviderType())) {
			try {
				orgProviderTypeMappingService.deleteByOrgIdAndProviderType(form.getOrgId(), form.getProviderType());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't clean the type mapping with org and provider type");
			}
		}
		else {
			try {
				orgProviderTypeMappingService.deleteByOrgId(form.getOrgId());
			}
			catch (DataIntegrityViolationException e) {
				throwBadRequest("Can't clean the type mapping with org");
			}
		}

		try {
			orgProviderTypeMappingService.createByOrgId(form.getOrgId(), form.getSubTypes());
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Can't create the type mapping");
		}

		logger.info("Update the Org ProviderType Type Mapping is updated.");
		return ok();
	}

	private List<OrgProviderTypeMappingForm> copyProviderKeyList(List<OrgProviderTypeMapping> providerTypes) {
		List<OrgProviderTypeMappingForm> forms = new ArrayList<OrgProviderTypeMappingForm>(providerTypes.size());
		for (OrgProviderTypeMapping providerType : providerTypes) {
			OrgProviderTypeMappingForm form = new OrgProviderTypeMappingForm();
			BeanUtils.copyProperties(providerType, form);
			form.setProviderTypeId(providerType.getProviderType().getUuid());
			forms.add(form);
		}
		return forms;
	}
}
