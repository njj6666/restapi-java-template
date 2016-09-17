package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionLog;
import com.hp.es.cto.sp.persistence.jaxb.subscription.log.Slog;
import com.hp.es.cto.sp.persistence.jaxb.subscription.log.SubscriptionLogs;
import com.hp.es.cto.sp.rest.form.SubscriptionLogForm;
import com.hp.es.cto.sp.service.context.util.JaxbUtil;
import com.hp.es.cto.sp.service.subscription.SubscriptionLogService;
import com.hp.es.cto.sp.service.subscription.SubscriptionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing subscription Log
 * 
 * @author Victor
 */
@Named
@Path(SubscriptionLogResource.URI)
@Api(value = SubscriptionLogResource.URI, description = "Subscription Log API")
public class SubscriptionLogResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(SubscriptionLogResource.class);

	private static final String XML_SUF = ".xml";

	public static final String URI = "/v1/subscription-logs";

	@Inject
	private SubscriptionService subscriptionService;

	@Inject
	private SubscriptionLogService subscriptionLogService;

	@POST
	@ApiOperation(value = "create Subscription Logs", notes = "create Subscription Logs with subscription log form", response = SubscriptionLogForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionLogForm create(@ApiParam(value = "The body of the subscription log form", required = true) @MultipartForm SubscriptionLogForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String subscriptionId = form.getSubscriptionId();
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}
		logger.info("Adding new Log for subscription : {}", subscriptionId);

		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throwBadRequest("subscription is not exist");
		}

		SubscriptionLog subscriptionLog = new SubscriptionLog();
		BeanUtils.copyProperties(form, subscriptionLog);
		subscriptionLog.setUuid(UUID.randomUUID().toString());
		subscriptionLog.setSubscription(subscription);
		subscriptionLog.setLogTime(new Date());

		subscriptionLog = subscriptionLogService.create(subscriptionLog);

		SubscriptionLogForm psf = new SubscriptionLogForm();
		BeanUtils.copyProperties(subscriptionLog, psf);
		psf.setSubscriptionId(subscription.getUuid());
		logger.info("New Log {} is added for subscription {}", psf.getUuid(), subscriptionId);
		return psf;
	}

	@GET
	@ApiOperation(value = "get Subscription Logs", notes = "get Subscription Logs by conditions", response = SubscriptionLogForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<SubscriptionLogForm> findAll(@ApiParam(value = "The id of the subscription to get all logs", required = true) @QueryParam("subscription_id") String subscriptionId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<SubscriptionLog> subscriptionLogs = new ArrayList<SubscriptionLog>();

		if (!isNullOrEmpty(subscriptionId)) {
			logger.info("Geting a list of Subscription Logs by subscription Id: {}", subscriptionId);
			Subscription subscription = subscriptionService.findById(subscriptionId);
			if (subscription == null) {
				throwBadRequest("subscription is not exist");
			}
			subscriptionLogs = subscriptionLogService.findLogBySubscription(subscription);
			return copySubscriptionLogsList(subscriptionLogs);
		}

		subscriptionLogs = subscriptionLogService.findAll();
		return copySubscriptionLogsList(subscriptionLogs);
	}

	@GET
	@Path("/export/{fileName}")
	@ApiOperation(value = "export Subscription log", notes = "export Subscription log to xml file by conditions")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response exportContext(@ApiParam(value = "The file name to save the logs", required = true) @PathParam("fileName") String fileName,
			@ApiParam(value = "The id of the subscription to get all logs", required = true) @QueryParam("subscription_id") String subscriptionId,
			@ApiParam(value = "The http response to download file", required = true) @Context HttpServletResponse response,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		InputStream is = null;
		List<SubscriptionLog> subscriptionLogs = new ArrayList<SubscriptionLog>();
		try {
			logger.info("Geting a list of Subscription Logs by subscription Id: {}", subscriptionId);
			Subscription subscription = subscriptionService.findById(subscriptionId);
			if (subscription == null) {
				throwBadRequest("subscription is not exist");
			}
			subscriptionLogs = subscriptionLogService.findLogBySubscription(subscription);

			SubscriptionLogs slogs = new SubscriptionLogs();
			for (SubscriptionLog subscriptionLog : subscriptionLogs) {
				Slog slog = new Slog();
				slog.setSubscriptionId(subscriptionId);
				slog.setComponent(subscriptionLog.getComponent());
				slog.setEvent(subscriptionLog.getEvent());
				slog.setLogLevel(subscriptionLog.getLogLevel());
				slog.setLogTime(subscriptionLog.getLogTime().toString());
				slog.setMessage(subscriptionLog.getMessage());
				slog.setDetail(subscriptionLog.getDetail());
				slog.setPath(subscriptionLog.getPath());
				slog.setTag(subscriptionLog.getTag());
				slogs.getSubscriptionLog().add(slog);
			}

			// Add Empty Log for Excel
			if (subscriptionLogs.size() > 0) {
				Slog slog = new Slog();
				slog.setSubscriptionId("");
				slog.setComponent("");
				slog.setEvent("");
				slog.setLogLevel("");
				slog.setLogTime("");
				slog.setMessage("");
				slogs.getSubscriptionLog().add(slog);
			}

			// marshall to string
			String ciStr = JaxbUtil.mashall(slogs, "com.hp.es.cto.sp.persistence.jaxb.subscription.log");
			is = IOUtils.toInputStream(ciStr);

			// Bytes to InputSteam
			if (!fileName.endsWith(XML_SUF)) {
				fileName = fileName + XML_SUF;
			}

			response.reset();
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\"");
			return Response.ok().entity(is).type(MediaType.APPLICATION_OCTET_STREAM_TYPE).build();
		}
		catch (Exception e) {
			logger.warn(e.getMessage(), e);
			throwBadRequest("Can't export file!");
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			}
			catch (IOException e) {
				logger.warn(e.getMessage());
			}
		}
		return ok();
	}

	private List<SubscriptionLogForm> copySubscriptionLogsList(List<SubscriptionLog> subscriptionLogs) {
		List<SubscriptionLogForm> forms = new ArrayList<SubscriptionLogForm>(subscriptionLogs.size());
		for (SubscriptionLog subscriptionLog : subscriptionLogs) {
			SubscriptionLogForm form = new SubscriptionLogForm();
			if (subscriptionLog.getSubscription() != null) {
				form.setSubscriptionId(subscriptionLog.getSubscription().getUuid());
			}
			BeanUtils.copyProperties(subscriptionLog, form);
			forms.add(form);
		}
		return forms;
	}

}
