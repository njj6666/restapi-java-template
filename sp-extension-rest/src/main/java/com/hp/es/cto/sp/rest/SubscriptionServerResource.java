package com.hp.es.cto.sp.rest;

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

import org.hibernate.LazyInitializationException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.rest.form.SubscriptionServerForm;
import com.hp.es.cto.sp.service.subscription.SubscriptionServerService;
import com.hp.es.cto.sp.service.subscription.SubscriptionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing subscription server
 * 
 * @author Victor
 */
@Named
@Path(SubscriptionServerResource.URI)
@Api(value = SubscriptionServerResource.URI, description = "Subscription Server API")
public class SubscriptionServerResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(SubscriptionServerResource.class);

	public static final String URI = "/v1/subscription-servers";

	@Inject
	private SubscriptionService subscriptionService;

	@Inject
	private SubscriptionServerService subscriptionServerService;

	@POST
	@ApiOperation(value = "create Subscription Server Info", notes = "create Server information of a Subscription by form", response = SubscriptionServerForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionServerForm create(@ApiParam(value = "The body of the subscription server form", required = true) @MultipartForm SubscriptionServerForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String subscriptionId = form.getSubscriptionId();
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}
		logger.info("Adding new server for subscription : {}", subscriptionId);

		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throwBadRequest("subscription is not exist");
		}

		SubscriptionServer subscriptionServer = new SubscriptionServer();
		BeanUtils.copyProperties(form, subscriptionServer);
		subscriptionServer.setUuid(UUID.randomUUID().toString());
		subscriptionServer.setSubscription(subscription);

		subscriptionServer = subscriptionServerService.create(subscriptionServer);

		SubscriptionServerForm psf = new SubscriptionServerForm();
		BeanUtils.copyProperties(subscriptionServer, psf);
		psf.setSubscriptionId(subscription.getUuid());
		psf.setProductNames(makeProductList(subscriptionServer));
		logger.info("New server {} is added for subscription {}", psf.getUuid(), subscriptionId);
		return psf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "update Subscription Server", notes = "update Subscription Server information", response = SubscriptionServerForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionServerForm update(@ApiParam(value = "The uuid of the specific subscription server information record", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the subscription server information form", required = true) @MultipartForm SubscriptionServerForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		SubscriptionServer server = subscriptionServerService.findById(uuid);
		if (server == null) {
			throwBadRequest("server not found by uuid: " + uuid);
		}

		String subscriptionId = form.getSubscriptionId();
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}

		if (!subscriptionId.equals(server.getSubscription().getUuid())) {
			throwBadRequest("subscription Id can't be changed!");
		}
		logger.info("Updating server uuid : {} for subscription : {}", uuid, subscriptionId);

		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throwBadRequest("subscription is not exist");
		}

		setUpdateValue(form, server);
		server.setSubscription(subscription);

		server = subscriptionServerService.update(server);

		SubscriptionServerForm psf = new SubscriptionServerForm();
		BeanUtils.copyProperties(server, psf);
		psf.setSubscriptionId(subscription.getUuid());
		psf.setProductNames(makeProductList(server));
		logger.info("server {} is updated for subscription {}", psf.getUuid(), subscriptionId);
		return psf;
	}

	private void setUpdateValue(SubscriptionServerForm form, SubscriptionServer subscriptionServer) {
		if (!isNullOrEmpty(form.getCsaProviderId())) {
			subscriptionServer.setCsaProviderId(form.getCsaProviderId());
		}

		if (!isNullOrEmpty(form.getDns())) {
			subscriptionServer.setDns(form.getDns());
		}

		if (!isNullOrEmpty(form.getIaasProvider())) {
			subscriptionServer.setIaasProvider(form.getIaasProvider());
		}

		if (!isNullOrEmpty(form.getIpAddress())) {
			subscriptionServer.setIpAddress(form.getIpAddress());
		}

		if (!isNullOrEmpty(form.getPrivateIpAddress())) {
			subscriptionServer.setPrivateIpAddress(form.getPrivateIpAddress());
		}

		if (!isNullOrEmpty(form.getPublicIpAddress())) {
			subscriptionServer.setPublicIpAddress(form.getPublicIpAddress());
		}

		if (!isNullOrEmpty(form.getSimplifiedDns())) {
			subscriptionServer.setSimplifiedDns(form.getSimplifiedDns());
		}

		if (!isNullOrEmpty(form.getSize())) {
			subscriptionServer.setSize(form.getSize());
		}
		if (!isNullOrEmpty(form.getActiveFlag())) {
			subscriptionServer.setActiveFlag(form.getActiveFlag());
		}
		if (!isNullOrEmpty(form.getOsType())) {
			subscriptionServer.setOsType(form.getOsType());
		}
	}

	@GET
	@ApiOperation(value = "get Subscription Server by id", notes = "get Subscription Server Record by record id", response = SubscriptionServerForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionServerForm findByUuid(@ApiParam(value = "The uuid of the specific subscription server information record", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		logger.info("Getting Server by uuid: {}", uuid);

		SubscriptionServer subscriptionServer = subscriptionServerService.findById(uuid);
		if (subscriptionServer == null) {
			throwBadRequest("subscription Server not found by uuid: " + uuid);
		}
		SubscriptionServerForm cnf = new SubscriptionServerForm();
		if (subscriptionServer.getSubscription() != null) {
			cnf.setSubscriptionId(subscriptionServer.getSubscription().getUuid());
		}
		cnf.setProductNames(makeProductList(subscriptionServer));
		BeanUtils.copyProperties(subscriptionServer, cnf);
		return cnf;
	}

	@GET
	@ApiOperation(value = "get all Subscription Servers information", notes = "get all Subscription Servers information by condition", response = SubscriptionServerForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<SubscriptionServerForm> findAll(@ApiParam(value = "The uuid of the specific subscription") @QueryParam("subscription_id") String subscriptionId,
			@ApiParam(value = "The ipaddress which server owns") @QueryParam("ip_address") String ipAddress, @ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken,
			@ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser, @ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<SubscriptionServer> subscriptionServers = new ArrayList<SubscriptionServer>();
		if (!isNullOrEmpty(ipAddress) && !isNullOrEmpty(subscriptionId)) {
			logger.info("Geting a list of Subscription Servers by subscription Id : {} and Ip Address {}", subscriptionId, ipAddress);
			subscriptionServers = subscriptionServerService.findBySubscriptionAndIp(subscriptionId, ipAddress);
		}

		if (!isNullOrEmpty(subscriptionId)) {
			logger.info("Geting a list of Subscription Servers by subscription Id: {}", subscriptionId);
			Subscription subscription = subscriptionService.findById(subscriptionId);
			if (subscription == null) {
				throwBadRequest("subscription is not exist");
			}
			subscriptionServers = subscriptionServerService.findServerBySubscription(subscription);
			return copySubscriptionServerList(subscriptionServers);
		}

		subscriptionServers = subscriptionServerService.findAll();
		return copySubscriptionServerList(subscriptionServers);
	}

	@DELETE
	@Path("{uuid}")
	@ApiOperation(value = "delete Subscription Server by id", notes = "delete Subscription Server by id")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteById(@ApiParam(value = "The uuid of the specific subscription server information record", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting Subscription Server by uuid: {}", uuid);

		SubscriptionServer subscriptionServer = subscriptionServerService.findById(uuid);
		if (subscriptionServer == null) {
			throwBadRequest("subscription Server not found by uuid: " + uuid);
		}
		subscriptionServerService.delete(subscriptionServer);
		logger.info("Subscription Server {} is deleted", uuid);
		return ok();
	}

	@DELETE
	@ApiOperation(value = "delete Subscription Servers by subscription id", notes = "delete all Subscription Server records by subscription id")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "The uuid of the specific subscription", required = true) @QueryParam("subscription_id") String subscriptionId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting subscription server by subscription: {}", subscriptionId);
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}

		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throwBadRequest("subscription is not exist");
		}

		subscriptionServerService.deleteBySubscriptionId(subscriptionId);

		logger.info("subscription servers is deleted by subscription: {} ", subscriptionId);
		return ok();
	}

	private List<SubscriptionServerForm> copySubscriptionServerList(List<SubscriptionServer> subscriptionServers) {
		List<SubscriptionServerForm> forms = new ArrayList<SubscriptionServerForm>(subscriptionServers.size());
		for (SubscriptionServer subscriptionServer : subscriptionServers) {
			SubscriptionServerForm form = new SubscriptionServerForm();
			if (subscriptionServer.getSubscription() != null) {
				form.setSubscriptionId(subscriptionServer.getSubscription().getUuid());
			}
			form.setProductNames(makeProductList(subscriptionServer));
			BeanUtils.copyProperties(subscriptionServer, form);
			forms.add(form);
		}
		return forms;
	}

	private List<String> makeProductList(SubscriptionServer subscriptionServer) {
		List<String> list = new ArrayList<String>();
		if (subscriptionServer.getProduct() != null) {
			try {
				for (SubscriptionServerProduct product : subscriptionServer.getProduct()) {
					list.add(product.getProductName());
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.SubscriptionServer.Product," + " no session or session was closed");
			}
		}
		return list;
	}

}
