package com.hp.es.cto.sp.rest.provider;

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

import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderKeyForm;
import com.hp.es.cto.sp.service.provider.ProviderKeyService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing provider keys.
 * 
 * @author Nick
 */
@Named
@Path(ProviderKeyResource.URI)
@Api(value = ProviderKeyResource.URI, description = "Provider Private Key Management API")
public class ProviderKeyResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ProviderKeyResource.class);

	public static final String URI = "/v1/provider-keys";

	@Inject
	private ProviderKeyService providerKeyService;

	private static final String AUTH_TYPE_KEY = "key";
	private static final String AUTH_TYPE_PASSWORD = "password";

	@POST
	@ApiOperation(value = "Create Provider Private Key", notes = "Create new provider private key with provider key form", response = ProviderKeyForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderKeyForm create(@ApiParam(value = "The body of the provider key form", required = true) @MultipartForm ProviderKeyForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String providerId = form.getCsaProviderId();
		logger.info("Adding new provider key for CSA provider: {}", providerId);
		if (isNullOrEmpty(providerId)) {
			throwBadRequest("CSA Provider ID is required");
		}
		if (isNullOrEmpty(form.getUsername())) {
			throwBadRequest("User name is required");
		}

		ProviderKey providerKey = new ProviderKey();
		BeanUtils.copyProperties(form, providerKey);
		providerKey.setUuid(UUID.randomUUID().toString());
		providerKey.setCreateDate(new Date());

		// Set auth information according to auth-type param
		if (form.getAuthType().equals(AUTH_TYPE_PASSWORD)) {
			if (isNullOrEmpty(form.getPassword())) {
				throwBadRequest("Password is required for auth-type " + AUTH_TYPE_PASSWORD);
			}
			providerKey.setKeyFile(null);
		}
		else if (form.getAuthType().equals(AUTH_TYPE_KEY)) {
			if (form.getKeyFile() == null || form.getKeyFile().length == 0) {
				throwBadRequest("Key file is required for auth-type " + AUTH_TYPE_KEY);
			}
			providerKey.setPassword("");
		}
		else {
			throwBadRequest("Invalid parameter value of auth-type: " + form.getAuthType());
		}

		providerKey = providerKeyService.create(providerKey);

		ProviderKeyForm pKey = new ProviderKeyForm();
		BeanUtils.copyProperties(providerKey, pKey);
		logger.info("New provider key {} is added for CSA provider {}", providerKey.getUuid(), providerId);
		return pKey;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "Update Provider Private Key", notes = "Update Provider Private Key with key form", response = ProviderKeyForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderKeyForm update(@ApiParam(value = "uuid of the provider key", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the provider key form", required = true) @MultipartForm ProviderKeyForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating provider key by uuid: {}", uuid);
		if (isNullOrEmpty(form.getUsername())) {
			throwBadRequest("User name is required");
		}

		ProviderKey providerKey = providerKeyService.findById(uuid);
		if (providerKey == null) {
			throwBadRequest("Provider key not found by uuid: " + uuid);
		}

		providerKey.setCsaProviderId(form.getCsaProviderId());
		providerKey.setName(form.getName());
		providerKey.setUsername(form.getUsername());

		// Set auth information according to auth-type param
		if (form.getAuthType().equals(AUTH_TYPE_PASSWORD)) {
			providerKey.setPassword(form.getPassword());
			providerKey.setKeyFile(null);
		}
		else if (form.getAuthType().equals(AUTH_TYPE_KEY)) {
			if (form.getKeyFile() != null && form.getKeyFile().length > 0) {
				providerKey.setKeyFile(form.getKeyFile());
			}
			providerKey.setPassword("");
		}
		else {
			throwBadRequest("Invalid parameter value of auth-type: " + form.getAuthType());
		}

		providerKey = providerKeyService.update(providerKey);

		ProviderKeyForm pKey = new ProviderKeyForm();
		BeanUtils.copyProperties(providerKey, pKey);
		logger.info("Provider key {} is updated.", uuid);
		return pKey;
	}

	@DELETE
	@Path("{uuid}")
	@ApiOperation(value = "Delete the provider private key", notes = "Delete the provider private key by uuid")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "uuid of the provider key", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting provider key by uuid: {}", uuid);

		ProviderKey providerKey = providerKeyService.findById(uuid);
		if (providerKey == null) {
			throwNotFound("Provider key not found by uuid: " + uuid);
		}
		providerKeyService.delete(providerKey);
		logger.info("Provider key {} is deleted.", uuid);
		return ok();
	}

	@GET
	@Path("{uuid}")
	@ApiOperation(value = "Get provider key", notes = "Get provider key by uuid", response = ProviderKeyForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderKeyForm findByUuid(@ApiParam(value = "uuid of the provider key", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting provider key by uuid: {}", uuid);

		ProviderKey providerKey = providerKeyService.findById(uuid);
		if (providerKey == null) {
			logger.error("Provider key not found by uuid: " + uuid);
			throwNotFound("Provider key not found by uuid: " + uuid);
		}
		ProviderKeyForm pKey = new ProviderKeyForm();
		BeanUtils.copyProperties(providerKey, pKey);
		return pKey;
	}

	@GET
	@ApiOperation(value = "Get provider keys", notes = "Get provider keys by conditions", response = ProviderKeyForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderKeyForm> findAll(@ApiParam(value = "CSA provider resource Id") @QueryParam("csa-provider-id") String providerId,
			@ApiParam(value = "AZ of the provider") @QueryParam("availability-zone") String zone,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of provider keys: {}, {}", providerId, zone);

		List<ProviderKey> providerKeys;
		if (!isNullOrEmpty(providerId) && isNullOrEmpty(zone)) {
			providerKeys = providerKeyService.findByProviderId(providerId);
		}
		else if (!isNullOrEmpty(providerId) && !isNullOrEmpty(zone)) {
			providerKeys = providerKeyService.findByProviderIdAndRegion(providerId, zone);
		}
		else {
			providerKeys = providerKeyService.findAll();
		}

		return copyProviderKeyList(providerKeys);
	}

	protected static List<ProviderKeyForm> copyProviderKeyList(List<ProviderKey> providerKeys) {
		List<ProviderKeyForm> forms = new ArrayList<ProviderKeyForm>(providerKeys.size());
		for (ProviderKey providerKey : providerKeys) {
			ProviderKeyForm form = new ProviderKeyForm();
			BeanUtils.copyProperties(providerKey, form);
			forms.add(form);
		}
		return forms;
	}
}
