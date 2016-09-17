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

import com.hp.es.cto.sp.persistence.entity.provider.ProviderSharedResource;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderSharedResourceForm;
import com.hp.es.cto.sp.service.provider.ProviderSharedResourceService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing provider shared resources.
 * 
 * @author Victor
 */
@Named
@Path(ProviderSharedResResource.URI)
public class ProviderSharedResResource extends SPResource {
	private final Logger logger = LoggerFactory
			.getLogger(ProviderSharedResResource.class);
	@Inject
	private ProviderSharedResourceService ProviderSharedResourceService;

	/**
	 * URI of subscription
	 */
	public static final String URI = "/v1/provider-shared-resources";

	@POST
	@ApiOperation(value = "create provider server", notes = "create provider server", response = ProviderSharedResourceForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"),
			@ApiResponse(code = 404, message = "bad") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSharedResourceForm create(
			@MultipartForm ProviderSharedResourceForm form) {
		String csaproviderId = form.getCsaProviderId();
		logger.info("Adding new provider shared resource for CSA provider: {}",
				csaproviderId);
		if (isNullOrEmpty(csaproviderId)) {
			throwBadRequest("CSA Provider ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (form.getMaxConnection() == null) {
			throwBadRequest("max connection is required");
		}
		if (form.getCurrentConnection() == null) {
			throwBadRequest("current connection is required");
		}

		ProviderSharedResource ProviderSharedResource = new ProviderSharedResource();
		BeanUtils.copyProperties(form, ProviderSharedResource);
		ProviderSharedResource.setUuid(UUID.randomUUID().toString());
		ProviderSharedResource.setCreateDate(new Date());

		ProviderSharedResource = ProviderSharedResourceService
				.create(ProviderSharedResource);

		ProviderSharedResourceForm psf = new ProviderSharedResourceForm();
		BeanUtils.copyProperties(ProviderSharedResource, psf);
		logger.info(
				"New provider Shared Resource {} is added for CSA provider {}",
				psf.getUuid(), csaproviderId);
		return psf;
	}

	@PUT
	@ApiOperation(value = "update provider shared resource current connection", notes = "update provider shared resource current connection", response = ProviderSharedResourceForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"),
			@ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSharedResourceForm updateCurrentConnection(
			@QueryParam("resourceId") String resourceId,
			@QueryParam("increment") Integer increment) {
		logger.info(
				"Updating provider Shared Resource CurrentConnection by uuid: {}",
				resourceId);

		ProviderSharedResource ProviderSharedResource = ProviderSharedResourceService
				.findById(resourceId);
		if (ProviderSharedResource == null) {
			throwBadRequest("Provider Shared Resource not found by uuid: "
					+ resourceId);
		}

		Integer newConnection = ProviderSharedResource.getCurrentConnection()
				+ increment;
		ProviderSharedResource.setCurrentConnection(newConnection);

		ProviderSharedResource = ProviderSharedResourceService
				.update(ProviderSharedResource);

		ProviderSharedResourceForm psf = new ProviderSharedResourceForm();
		BeanUtils.copyProperties(ProviderSharedResource, psf);
		logger.info(
				"Provider Resource {} Connection is updated with increment: {}.",
				resourceId, increment);
		return psf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "update provider shared resource", notes = "update provider shared resource", response = ProviderSharedResourceForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"),
			@ApiResponse(code = 404, message = "bad") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSharedResourceForm update(@PathParam("uuid") String uuid,
			@MultipartForm ProviderSharedResourceForm form) {
		logger.info("Updating provider Server by uuid: {}", uuid);
		String csaproviderId = form.getCsaProviderId();
		if (isNullOrEmpty(csaproviderId)) {
			throwBadRequest("CSA Provider ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (form.getMaxConnection() == null) {
			throwBadRequest("max connection is required");
		}
		if (form.getCurrentConnection() == null) {
			throwBadRequest("current connection is required");
		}

		ProviderSharedResource ProviderSharedResource = ProviderSharedResourceService
				.findById(uuid);
		if (ProviderSharedResource == null) {
			throwBadRequest("Provider Server not found by uuid: " + uuid);
		}

		ProviderSharedResource.setCsaProviderId(csaproviderId);
		ProviderSharedResource.setName(form.getName());
		ProviderSharedResource
				.setCurrentConnection(form.getCurrentConnection());
		ProviderSharedResource.setMaxConnection(form.getMaxConnection());

		ProviderSharedResource = ProviderSharedResourceService
				.update(ProviderSharedResource);

		ProviderSharedResourceForm psf = new ProviderSharedResourceForm();
		BeanUtils.copyProperties(ProviderSharedResource, psf);
		logger.info("Provider Server {} is updated.", uuid);
		return psf;
	}

	@DELETE
	@Path("{uuid}")
	@ApiOperation(value = "delete provider server by uuid", notes = "delete provider server by uuid")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"),
			@ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@PathParam("uuid") String uuid) {
		logger.info("Deleting shared resource by uuid: {}", uuid);

		ProviderSharedResource ProviderSharedResource = ProviderSharedResourceService
				.findById(uuid);
		if (ProviderSharedResource == null) {
			throwNotFound("Provider shared resource not found by uuid: " + uuid);
		}
		ProviderSharedResourceService.delete(ProviderSharedResource);
		logger.info("Shared resource {} is deleted.", uuid);
		return ok();
	}

	@GET
	@Path("{uuid}")
	@ApiOperation(value = "get provider shared resource by uuid", notes = "get provider shared resource by uuid", response = ProviderSharedResourceForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"),
			@ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderSharedResourceForm findByUuid(@PathParam("uuid") String uuid) {
		logger.info("Getting provider key by uuid: {}", uuid);

		ProviderSharedResource ProviderSharedResource = ProviderSharedResourceService
				.findById(uuid);
		if (ProviderSharedResource == null) {
			throwNotFound("Provider Shared resource not found by uuid: " + uuid);
		}
		ProviderSharedResourceForm psf = new ProviderSharedResourceForm();
		BeanUtils.copyProperties(ProviderSharedResource, psf);
		return psf;
	}

	@GET
	@ApiOperation(value = "get provider shared resource", notes = "get provider shared resource", response = ProviderSharedResourceForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"),
			@ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderSharedResourceForm> findAllByProviderId(
			@QueryParam("csa_provider_id") String providerId,
			@QueryParam("isAvailable") String isAvailable) {
		logger.info("Geting a list of provider Server: {}", providerId);
		if (isNullOrEmpty(providerId)) {
			throwBadRequest("CSA Provider ID is required");
		}
		List<ProviderSharedResource> ProviderSharedResources = new ArrayList<ProviderSharedResource>();

		if (isNullOrEmpty(isAvailable)) {
			ProviderSharedResources = ProviderSharedResourceService
					.findByProviderId(providerId);
		} else if (isAvailable.equals("true")) {
			ProviderSharedResources = ProviderSharedResourceService
					.findByProviderIdAndAvailable(providerId);
		}else if (isAvailable.equals("false")) {
			ProviderSharedResources = ProviderSharedResourceService
					.findByProviderIdAndUnvailable(providerId);
		}
		return copyProviderSharedResourceList(ProviderSharedResources);
	}

	protected static List<ProviderSharedResourceForm> copyProviderSharedResourceList(
			List<ProviderSharedResource> ProviderSharedResources) {
		List<ProviderSharedResourceForm> forms = new ArrayList<ProviderSharedResourceForm>(
				ProviderSharedResources.size());
		for (ProviderSharedResource ProviderSharedResource : ProviderSharedResources) {
			ProviderSharedResourceForm form = new ProviderSharedResourceForm();
			BeanUtils.copyProperties(ProviderSharedResource, form);
			forms.add(form);
		}
		return forms;
	}

}
