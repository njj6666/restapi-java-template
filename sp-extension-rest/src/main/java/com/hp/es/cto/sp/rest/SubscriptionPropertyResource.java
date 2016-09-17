package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;
import com.hp.es.cto.sp.rest.form.SubscriptionPropertyForm;
import com.hp.es.cto.sp.service.subscription.SubscriptionPropertyService;
import com.hp.es.cto.sp.service.subscription.SubscriptionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing subscription property
 * 
 * @author Victor
 */
@Named
@Path(SubscriptionPropertyResource.URI)
@Api(value = SubscriptionPropertyResource.URI, description = "Subscription Property API")
public class SubscriptionPropertyResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(SubscriptionPropertyResource.class);

	/**
	 * URI of subscription
	 */
	public static final String URI = "/v1/subscription-properties";

	@Inject
	private SubscriptionPropertyService subscriptionPropertyService;

	@Inject
	private SubscriptionService subscriptionService;

	@POST
	@ApiOperation(value = "create Subscription Property", notes = "create Subscription Property", response = SubscriptionPropertyForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionPropertyForm create(@ApiParam(value = "The body of the subscription property form", required = true) @MultipartForm SubscriptionPropertyForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String subscriptionId = form.getSubscriptionId();
		logger.info("Adding new property for subscription : {}", subscriptionId);
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}
		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throwBadRequest("subscription is not exist");
		}
		if (isNullOrEmpty(form.getPropertyName())) {
			throwBadRequest("Property Name is required");
		}

		SubscriptionProperty subscriptionProperty = new SubscriptionProperty();
		BeanUtils.copyProperties(form, subscriptionProperty);
		subscriptionProperty.setUuid(UUID.randomUUID().toString());
		subscriptionProperty.setSubscription(subscription);

		subscriptionProperty = subscriptionPropertyService.create(subscriptionProperty);

		SubscriptionPropertyForm psf = new SubscriptionPropertyForm();
		BeanUtils.copyProperties(subscriptionProperty, psf);
		psf.setSubscriptionId(subscription.getUuid());
		logger.info("New Property {} is added for subscription {}", psf.getUuid(), subscriptionId);
		return psf;
	}

	@GET
	@ApiOperation(value = "get Subscription Property", notes = "get Subscription Property", response = SubscriptionPropertyForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<SubscriptionPropertyForm> findAll(@ApiParam(value = "The id of the subscription", required = true) @QueryParam("subscription_id") String subscriptionId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<SubscriptionProperty> SubscriptionProperties = new ArrayList<SubscriptionProperty>();
		if (!isNullOrEmpty(subscriptionId)) {
			logger.info("Geting a list of SubscriptionProperties by subscription Id: {}", subscriptionId);
			Subscription subscription = subscriptionService.findById(subscriptionId);
			if (subscription == null) {
				throwBadRequest("subscription is not exist");
			}
			SubscriptionProperties = subscriptionPropertyService.findPropertyBySubscription(subscription);
			return copySubscriptionPropertyList(SubscriptionProperties);
		}

		SubscriptionProperties = subscriptionPropertyService.findAll();
		return copySubscriptionPropertyList(SubscriptionProperties);
	}

	private List<SubscriptionPropertyForm> copySubscriptionPropertyList(List<SubscriptionProperty> subscriptionProperties) {
		List<SubscriptionPropertyForm> forms = new ArrayList<SubscriptionPropertyForm>(subscriptionProperties.size());
		for (SubscriptionProperty subscriptionProperty : subscriptionProperties) {
			SubscriptionPropertyForm form = new SubscriptionPropertyForm();
			if (subscriptionProperty.getSubscription() != null) {
				form.setSubscriptionId(subscriptionProperty.getSubscription().getUuid());
			}
			BeanUtils.copyProperties(subscriptionProperty, form);
			forms.add(form);
		}
		return forms;
	}

}
