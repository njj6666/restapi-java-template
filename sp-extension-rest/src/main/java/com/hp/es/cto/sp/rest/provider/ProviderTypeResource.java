package com.hp.es.cto.sp.rest.provider;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderSubTypeForm;
import com.hp.es.cto.sp.rest.form.ProviderTypeForm;
import com.hp.es.cto.sp.service.provider.ProviderTypeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for ProviderType management
 * 
 * @author Nick
 * 
 */
@Named
@Path(ProviderTypeResource.URI)
@Api(value = ProviderTypeResource.URI, description = "API to manage the provider type and sub types")
public class ProviderTypeResource extends SPResource {
	private static final Logger logger = LoggerFactory.getLogger(ProviderTypeResource.class);

	public static final String URI = "/v1/provider-types";
	@Inject
	private ProviderTypeService providerTypeService;

	@GET
	@ApiOperation(value = "Get ProviderTypes", notes = "Get ProviderTypes with condition, if no condition return all", response = ProviderTypeForm.class, responseContainer = "List")
	@ApiResponses(value = {})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderTypeForm> findAll(@ApiParam(value = "the provider type") @QueryParam("type") String type, @ApiParam(value = "the provider sub type") @QueryParam("sub_type") String subType,
			@ApiParam(value = "the id of the csa orgnization which has constraint with the provider type") @QueryParam("org_id") String orgId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		List<ProviderType> list = new ArrayList<ProviderType>();
		if (isNullOrEmpty(subType) && isNullOrEmpty(type)) {
			list = providerTypeService.findAll();
			return makeProviderTypeList(list);
		}

		if (!isNullOrEmpty(type)) {
			if (!isNullOrEmpty(subType)) {
				ProviderType poviderType = providerTypeService.findByType(type, subType);
				if (poviderType != null) {
					list.add(poviderType);
				}
			}
			else {
				if (!isNullOrEmpty(orgId)) {
					list = providerTypeService.findByTypeWithConstraint(type, orgId);
				}
				else {
					list = providerTypeService.findByType(type);
				}
			}
		}
		return makeProviderTypeList(list);
	}

	protected static List<ProviderTypeForm> makeProviderTypeList(Collection<ProviderType> providerTypes) {
		Map<String, ProviderTypeForm> providerTypeMap = new HashMap<String, ProviderTypeForm>();
		for (ProviderType providerType : providerTypes) {
			// make form for the provider subtype
			ProviderSubTypeForm subForm = new ProviderSubTypeForm();
			subForm.setType(providerType.getType());
			subForm.setSubType(providerType.getSubType());
			subForm.setUuid(providerType.getUuid());
			// add to mapping
			if (providerTypeMap.containsKey(providerType.getType())) {
				providerTypeMap.get(providerType.getType()).getSubTypes().add(subForm);
			}
			else {
				ProviderTypeForm form = new ProviderTypeForm();
				form.setType(providerType.getType());
				form.getSubTypes().add(subForm);
				providerTypeMap.put(providerType.getType(), form);
			}
		}
		// add the form to the collection
		List<ProviderTypeForm> returnList = new ArrayList<ProviderTypeForm>();
		Iterator<String> it = providerTypeMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			returnList.add(providerTypeMap.get(key));
		}
		return returnList;
	}

	@GET
	@ApiOperation(value = "Get Provider Sub Type", notes = "Get Provider Sub Type by uuid", response = ProviderTypeForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSubTypeForm findByUuid(@ApiParam(value = "the id of provider sub type", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Provider Sub Type by uuid: {}", uuid);

		ProviderType providerSubType = providerTypeService.findById(uuid);
		if (providerSubType == null) {
			throwNotFound("Provider Sub Type not found by uuid: " + uuid);
		}
		ProviderSubTypeForm form = new ProviderSubTypeForm();
		BeanUtils.copyProperties(providerSubType, form);

		return form;
	}

	@POST
	@ApiOperation(value = "Create new ProviderType with SubType", notes = "Create new ProviderType with SubType", response = ProviderSubTypeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSubTypeForm create(@ApiParam(value = "The body of the provider type form", required = true) ProviderSubTypeForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (isNullOrEmpty(form.getType())) {
			throwBadRequest("Type is required");
		}
		if (isNullOrEmpty(form.getSubType())) {
			throwBadRequest("Sub Type is required");
		}

		ProviderType ProviderType = new ProviderType();
		BeanUtils.copyProperties(form, ProviderType);
		ProviderType.setUuid(UUID.randomUUID().toString());

		try {
			ProviderType = providerTypeService.create(ProviderType);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Type or SubType is duplicated to other ProviderType or Subtype!");
		}

		form = new ProviderSubTypeForm();
		BeanUtils.copyProperties(ProviderType, form);
		return form;
	}

	@PUT
	@ApiOperation(value = "Update the Provider Sub Type", notes = "Update the Provider Sub Type", response = ProviderTypeForm.class, consumes = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSubTypeForm update(@ApiParam(value = "the id of provider sub type", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the provider type form", required = true) ProviderSubTypeForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Updating ProviderType by uuid: {}", uuid);
		ProviderType ProviderType = providerTypeService.findById(uuid);
		if (ProviderType == null) {
			throwNotFound("Provider Sub Type not found by uuid " + uuid);
		}

		if (!isNullOrEmpty(form.getSubType())) {
			ProviderType.setSubType(form.getSubType());
		}

		try {
			ProviderType = providerTypeService.update(ProviderType);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Name is duplicated to other ProviderType!");
		}

		ProviderSubTypeForm result = new ProviderSubTypeForm();
		BeanUtils.copyProperties(ProviderType, result);
		logger.info("ProviderSubType {} is updated.", uuid);
		return result;
	}

	@DELETE
	@ApiOperation(value = "Delete the Provider Sub Type ", notes = "Delete the Provider Sub Type")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteSubType(@ApiParam(value = "the id of provider sub type", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.debug("Deleting Provider Sub Type by uuid: {}", uuid);
		ProviderType ProviderType = providerTypeService.findById(uuid);
		if (ProviderType == null) {
			throwNotFound("Provider Sub Type not found by uuid " + uuid);
		}
		providerTypeService.delete(ProviderType);
		logger.debug("Provider Sub Type {} is deleted.", uuid);
		return ok();
	}

	@DELETE
	@ApiOperation(value = "Delete the Provider Type", notes = "Delete the Provider Type")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteType(@ApiParam(value = "the provider type") @QueryParam("type") String type, @ApiParam(value = "the provider sub type") @QueryParam("sub_type") String subType,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		if (!isNullOrEmpty(type)) {
			throwBadRequest("Type is required");
		}

		if (!isNullOrEmpty(subType)) {
			logger.debug("Deleting ProviderType by type: {}", type);
			providerTypeService.deleteByType(type);
		}
		else {
			ProviderType ProviderType = providerTypeService.findByType(type, subType);
			if (ProviderType == null) {
				throwNotFound("Provider Sub Type not found by type: " + type + " and subType: " + subType);
			}
			providerTypeService.delete(ProviderType);
			logger.debug("Provider Sub Type is deleted.");
		}
		return ok();
	}
}
