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
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderServer;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderServerForm;
import com.hp.es.cto.sp.service.provider.ProviderServerService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing provider server.
 * 
 * @author Victor
 */
@Named
@Path(ProviderServerResource.URI)
@Api(value = ProviderServerResource.URI, description = "API to manage server resources in provider")
public class ProviderServerResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ProviderServerResource.class);
	@Inject
	private ProviderServerService providerServerService;

	/**
	 * URI of subscription
	 */
	public static final String URI = "/v1/provider-servers";

	@POST
	@ApiOperation(value = "create provider server", notes = "create server record into provider", response = ProviderServerForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderServerForm create(@ApiParam(value = "The body of provider server form", required = true) @MultipartForm ProviderServerForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String csaproviderId = form.getCsaProviderId();
		logger.info("Adding new provider Server for CSA provider: {}", csaproviderId);
		if (isNullOrEmpty(csaproviderId)) {
			throwBadRequest("CSA Provider ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (isNullOrEmpty(form.getIpAddress())) {
			throwBadRequest("IpAddress is required");
		}
		if (isNullOrEmpty(form.getIsAllocated())) {
			throwBadRequest("IsAllocated is required");
		}
		if (isNullOrEmpty(form.getSize())) {
			throwBadRequest("Size is required");
		}
		if (isNullOrEmpty(form.getProvider())) {
			throwBadRequest("provider is required");
		}

		ProviderServer providerServer = new ProviderServer();
		BeanUtils.copyProperties(form, providerServer);
		providerServer.setUuid(UUID.randomUUID().toString());
		providerServer.setCreateDate(new Date());

		try {
			providerServer = providerServerService.create(providerServer);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Provider Server record with same ipaddress, privateIpaddress or public Ipaddress is exist! Can't Create");
		}

		ProviderServerForm psf = new ProviderServerForm();
		BeanUtils.copyProperties(providerServer, psf);
		logger.info("New provider Server {} is added for CSA provider {}", psf.getUuid(), csaproviderId);
		return psf;
	}

	@PUT
	@ApiOperation(value = "update provider server isAllocated", notes = "update provider server isAllocated attribute", response = ProviderServerForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderServerForm updateIsAllocated(@ApiParam(value = "The id of the server", required = true) @QueryParam("serverId") String serverId, @QueryParam("isAllocated") String isAllocated,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating provider Server is Allocated by uuid: {}", serverId);

		ProviderServer providerServer = providerServerService.findById(serverId);
		if (providerServer == null) {
			throwBadRequest("Provider Server not found by uuid: " + serverId);
		}

		providerServer.setIsAllocated(isAllocated);

		providerServer = providerServerService.update(providerServer);

		ProviderServerForm psf = new ProviderServerForm();
		BeanUtils.copyProperties(providerServer, psf);
		logger.info("Provider Server {} is updated.", serverId);
		return psf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "update provider server", notes = "update provider server", response = ProviderServerForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"), @ApiResponse(code = 404, message = "bad") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderServerForm update(@ApiParam(value = "The id of the server", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of provider server form", required = true) @MultipartForm ProviderServerForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating provider Server by uuid: {}", uuid);
		String csaproviderId = form.getCsaProviderId();
		if (isNullOrEmpty(csaproviderId)) {
			throwBadRequest("CSA Provider ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (isNullOrEmpty(form.getIpAddress())) {
			throwBadRequest("IpAddress is required");
		}
		if (isNullOrEmpty(form.getIsAllocated())) {
			throwBadRequest("IsAllocated is required");
		}
		if (isNullOrEmpty(form.getSize())) {
			throwBadRequest("Size is required");
		}
		if (isNullOrEmpty(form.getProvider())) {
			throwBadRequest("provider is required");
		}

		ProviderServer providerServer = providerServerService.findById(uuid);
		if (providerServer == null) {
			throwBadRequest("Provider Server not found by uuid: " + uuid);
		}

		providerServer.setCsaProviderId(csaproviderId);
		providerServer.setName(form.getName());
		providerServer.setIpAddress(form.getIpAddress());
		providerServer.setIsAllocated(form.getIsAllocated());
		providerServer.setName(form.getName());
		providerServer.setOsType(form.getOsType());
		providerServer.setPrivateIpAddress(form.getPrivateIpAddress());
		providerServer.setProvider(form.getProvider());
		providerServer.setPublicIpAddress(form.getPublicIpAddress());
		providerServer.setSize(form.getSize());

		try {
			providerServer = providerServerService.update(providerServer);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Provider Server record with same ipaddress, privateIpaddress or public Ipaddress is exist! Can't update! ");
		}

		ProviderServerForm psf = new ProviderServerForm();
		BeanUtils.copyProperties(providerServer, psf);
		logger.info("Provider Server {} is updated.", uuid);
		return psf;
	}

	@DELETE
	@ApiOperation(value = "delete provider server by uuid", notes = "delete provider server by uuid")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"), @ApiResponse(code = 404, message = "bad") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "The id of the server", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting provider Server by uuid: {}", uuid);

		ProviderServer providerServer = providerServerService.findById(uuid);
		if (providerServer == null) {
			throwNotFound("Provider Server not found by uuid: " + uuid);
		}
		providerServerService.delete(providerServer);
		logger.info("Provider key {} is deleted.", uuid);
		return ok();
	}

	@GET
	@Path("{uuid}")
	@ApiOperation(value = "get provider server by uuid", notes = "get provider server by uuid", response = ProviderServerForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ProviderServerForm findByUuid(@ApiParam(value = "The id of the server", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting provider key by uuid: {}", uuid);

		ProviderServer providerServer = providerServerService.findById(uuid);
		if (providerServer == null) {
			throwNotFound("Provider Server not found by uuid: " + uuid);
		}
		ProviderServerForm psf = new ProviderServerForm();
		BeanUtils.copyProperties(providerServer, psf);
		return psf;
	}

	@GET
	@ApiOperation(value = "get provider server by server Info", notes = "get provider server by server Info", response = ProviderServerForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "bad"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderServerForm> findAllByProviderId(@ApiParam(value = "The id of provider which server belongs to") @QueryParam("csa-provider-id") String providerId,
			@ApiParam(value = "The allocate status of server") @QueryParam("is-allocated") String isAllocated, @ApiParam(value = "The size of server") @QueryParam("size") String size,
			@ApiParam(value = "The name of server") @QueryParam("name") String name, @ApiParam(value = "The os type of server") @QueryParam("osType") String osType,
			@ApiParam(value = "The ip address of server") @QueryParam("ipAddress") String ipAddress,
			@ApiParam(value = "The public ip address of server") @QueryParam("publicIpAddress") String publicIpAddress,
			@ApiParam(value = "The private ip address of server") @QueryParam("privateIpAddress") String privateIpAddress,
			@ApiParam(value = "The iaas provider type of the server") @QueryParam("provider") String provider,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of provider Server: {}", providerId);
		List<ProviderServer> providerServers = new ArrayList<ProviderServer>();
		if (!isNullOrEmpty(providerId)) {
			if (isNullOrEmpty(isAllocated)) {
				providerServers = providerServerService.findByProviderId(providerId);
			}
			else if (isNullOrEmpty(size)) {
				providerServers = providerServerService.findByProviderIdAndAllocated(providerId, isAllocated);
			}
			else {
				providerServers = providerServerService.findByProviderIdAndAllocatedAndSize(providerId, isAllocated, size);
			}
		}
		else {
			providerServers = providerServerService.findByConditions(providerId, isAllocated, size, name, osType, ipAddress, publicIpAddress, privateIpAddress, provider);
		}

		return copyProviderServerList(providerServers);
	}

	protected static List<ProviderServerForm> copyProviderServerList(List<ProviderServer> providerServers) {
		List<ProviderServerForm> forms = new ArrayList<ProviderServerForm>(providerServers.size());
		for (ProviderServer providerServer : providerServers) {
			ProviderServerForm form = new ProviderServerForm();
			BeanUtils.copyProperties(providerServer, form);
			forms.add(form);
		}
		return forms;
	}

}
