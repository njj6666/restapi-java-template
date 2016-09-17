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

import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;
import com.hp.es.cto.sp.rest.form.UserKeyForm;
import com.hp.es.cto.sp.service.keymgmt.UserKeyService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing user public keys.
 * 
 * @author Nick
 */
@Named
@Path(UserKeyResource.URI)
@Api(value = UserKeyResource.URI, description = "User Public Key Management API")
public class UserKeyResource extends SPResource {

	private final Logger logger = LoggerFactory.getLogger(UserKeyResource.class);

	public static final String URI = "/v1/user-keys";

	@Inject
	private UserKeyService userKeyService;

	@POST
	@ApiOperation(value = "Create User Public Key", notes = "Create new user public with user key form", response = UserKeyForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public UserKeyForm create(@ApiParam(value = "The body of the user key form", required = true) @MultipartForm UserKeyForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		String email = form.getLdapEmail();
		logger.info("Creating a user key {} for: {}", form.getName(), email);
		if (isNullOrEmpty(email)) {
			throwBadRequest("user ldap email is required.");
		}
		if (form.getKeyFile() == null || form.getKeyFile().length == 0) {
			throwBadRequest("user public key file is empty.");
		}

		UserKey userKey = new UserKey();
		BeanUtils.copyProperties(form, userKey);
		userKey.setUuid(UUID.randomUUID().toString());
		userKey.setCreateDate(new Date());

		userKey = userKeyService.create(userKey);

		UserKeyForm result = new UserKeyForm();
		BeanUtils.copyProperties(userKey, result);
		logger.info("User key {} is created for: {}", form.getName(), email);
		return result;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "Update User Public Key", notes = "Update user public key with user key form", response = UserKeyForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public UserKeyForm update(@ApiParam(value = "uuid of the existing user key", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the user key form", required = true) @MultipartForm UserKeyForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		logger.info("Updating user key by uuid: {}", uuid);
		UserKey userKey = userKeyService.findById(uuid);
		if (userKey == null) {
			throwNotFound("User key not found by uuid: " + uuid);
		}

		userKey.setName(form.getName());
		userKey.setLdapEmail(form.getLdapEmail());
		if (form.getKeyFile() != null && form.getKeyFile().length > 0) {
			// Update key file only if user choose a new key file.
			userKey.setKeyFile(form.getKeyFile());
		}
		userKey = userKeyService.update(userKey);

		UserKeyForm result = new UserKeyForm();
		BeanUtils.copyProperties(userKey, result);
		logger.info("User key {} is updated.", uuid);
		return result;
	}

	@DELETE
	@Path("{uuid}")
	@ApiOperation(value = "Delete the user key", notes = "Delete the user key by uuid")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "uuid of the existing user key", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		logger.info("Deleting user key by uuid: {}", uuid);

		UserKey userKey = userKeyService.findById(uuid);
		if (userKey == null) {
			throwNotFound("User key not found by uuid: " + uuid);
		}
		userKeyService.delete(userKey);
		logger.info("User key {} is deleted.", uuid);
		return ok();
	}

	@GET
	@Path("{uuid}")
	@ApiOperation(value = "Get user key", notes = "Get user key by uuid", response = UserKeyForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public UserKeyForm findByUuid(@ApiParam(value = "uuid of the existing user key", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		logger.info("Getting user key by uuid: {}", uuid);

		UserKey userKey = userKeyService.findById(uuid);
		if (userKey == null) {
			throwNotFound("User key not found by uuid: " + uuid);
		}

		UserKeyForm result = new UserKeyForm();
		BeanUtils.copyProperties(userKey, result);
		return result;
	}

	@GET
	@ApiOperation(value = "Get user keys", notes = "Get user keys by email", response = UserKeyForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<UserKeyForm> findAll(@ApiParam(value = "email of the public key owner", required = true) @QueryParam("email") String email,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		logger.info("Getting a list of user keys by (optional) email: {}", email);

		if (isNullOrEmpty(email)) {
			throwBadRequest("user email is required.");
		}

		List<UserKey> userKeys = userKeyService.findByEmail(email);
		return copyUserKeyList(userKeys);
	}

	private List<UserKeyForm> copyUserKeyList(List<UserKey> userKeys) {
		List<UserKeyForm> result = new ArrayList<UserKeyForm>(userKeys.size());
		for (UserKey userKey : userKeys) {
			UserKeyForm pKey = new UserKeyForm();
			BeanUtils.copyProperties(userKey, pKey);
			result.add(pKey);
		}
		return result;
	}
}
