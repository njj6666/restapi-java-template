package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.LazyInitializationException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionProperty;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.persistence.jaxb.subscription.SubscriptionRecord;
import com.hp.es.cto.sp.persistence.jaxb.subscription.SubscriptionRecordServer;
import com.hp.es.cto.sp.persistence.jaxb.subscription.SubscriptionReport;
import com.hp.es.cto.sp.rest.form.SubscriptionCollectionForm;
import com.hp.es.cto.sp.rest.form.SubscriptionForm;
import com.hp.es.cto.sp.rest.form.SubscriptionPropertyForm;
import com.hp.es.cto.sp.rest.form.SubscriptionServerForm;
import com.hp.es.cto.sp.rest.mapping.IdNameMapping;
import com.hp.es.cto.sp.service.context.ContextNodeService;
import com.hp.es.cto.sp.service.context.util.JaxbUtil;
import com.hp.es.cto.sp.service.group.ProjectService;
import com.hp.es.cto.sp.service.subscription.SubscriptionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing subscription server products
 * 
 * @author Victor
 */
@Named
@Path(SubscriptionResource.URI)
@Api(value = SubscriptionResource.URI, description = "Subscription API")
public class SubscriptionResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(SubscriptionResource.class);
	@Inject
	private SubscriptionService subscriptionService;

	@Inject
	private ContextNodeService contextNodeService;

	@Inject
	private ProjectService projectService;

	private static final String XML_SUF = ".xml";
	/**
	 * URI of subscription
	 */
	public static final String URI = "/v1/subscriptions";

	@POST
	@ApiOperation(value = "create Subscription", notes = "create Subscription", response = SubscriptionForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionForm create(@ApiParam(value = "The subscription form", required = true) @MultipartForm SubscriptionForm form) {
		String subscriptionId = form.getUuid();
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}
		logger.info("Adding new subscription: {}", subscriptionId);

		Subscription subscription = new Subscription();
		BeanUtils.copyProperties(form, subscription);
		subscription.setStartTime(new Date());
		if (subscription.getContextNodeId() != null && subscription.getContextNodeId().length() == 0) {
			subscription.setContextNodeId(null);
		}
		subscription = subscriptionService.create(subscription);

		SubscriptionForm psf = new SubscriptionForm();
		psf.setServerIds(makeServersList(subscription));
		psf.setProperties(makePropertyList(subscription));
		BeanUtils.copyProperties(subscription, psf);
		logger.info("New subscription {} is added ", subscriptionId);
		return psf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "update Subscription", notes = "update Subscription", response = SubscriptionForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionForm update(@ApiParam(value = "The uuid of the subscription", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The subscription form", required = true) @MultipartForm SubscriptionForm form) {
		String subscriptionId = form.getUuid();
		if (isNullOrEmpty(subscriptionId)) {
			throwBadRequest("subscription ID is required");
		}

		Subscription subscription = subscriptionService.findById(uuid);
		if (subscription == null) {
			throwBadRequest("subscription not found by uuid: " + uuid);
		}

		if (!subscriptionId.equals(subscription.getUuid())) {
			throwBadRequest("subscription Id can't be changed!");
		}
		logger.info("Updating subscription uuid : {} , uuid");

		setUpdateValue(form, subscription);

		subscription = subscriptionService.update(subscription);

		SubscriptionForm psf = new SubscriptionForm();
		psf.setServerIds(makeServersList(subscription));
		psf.setProperties(makePropertyList(subscription));
		BeanUtils.copyProperties(subscription, psf);
		logger.info("Subscription {} is updated ", psf.getUuid());
		return psf;
	}

	private void setUpdateValue(SubscriptionForm form, Subscription subscription) {
		if (!isNullOrEmpty(form.getContextNodeId())) {
			subscription.setContextNodeId(form.getContextNodeId());
		}

		if (!isNullOrEmpty(form.getCsaOrgId())) {
			subscription.setCsaOrgId(form.getCsaOrgId());
		}

		if (!isNullOrEmpty(form.getName())) {
			subscription.setName(form.getName());
		}

		if (!isNullOrEmpty(form.getOwnerId())) {
			subscription.setOwnerId(form.getOwnerId());
		}

		if (!isNullOrEmpty(form.getProject())) {
			subscription.setProject(form.getProject());
		}

		if (!isNullOrEmpty(form.getStatus())) {
			subscription.setStatus(form.getStatus());
			if (form.getStatus().equalsIgnoreCase("CANCELLED")) {
				subscription.setTerminateTime(new Date());
			}
		}

		/*
		 * if (form.getStartTime() != null) { subscription.setStartTime(form.getStartTime()); }
		 */
		if (form.getTerminateTime() != null) {
			subscription.setTerminateTime(new Date());
		}
	}

	@GET
	@ApiOperation(value = "Get Subscription", notes = "get Subscription by id", response = SubscriptionForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionForm findByUuid(@ApiParam(value = "The id of subscription", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken,
			@ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg
			) {
		logger.info("Getting Server by uuid: {}", uuid);

		Subscription subscription = subscriptionService.findById(uuid);
		if (subscription == null) {
			throwBadRequest("subscription not found by uuid: " + uuid);
		}

		SubscriptionForm cnf = new SubscriptionForm();
		cnf.setServerIds(makeServersList(subscription));
		cnf.setProperties(makePropertyList(subscription));
		BeanUtils.copyProperties(subscription, cnf);
		return cnf;
	}

	@DELETE
	@Path("{uuid}")
	@ApiOperation(value = "delete Subscription by id", notes = "delete Subscription by id")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteById(@ApiParam(value = "The id of subscription", required = true) @PathParam("uuid") String uuid) {
		logger.info("Deleting Subscription Server by uuid: {}", uuid);

		Subscription subscription = subscriptionService.findById(uuid);
		if (subscription == null) {
			throwBadRequest("subscription not found by uuid: " + uuid);
		}
		subscriptionService.delete(subscription);
		logger.info("Subscription {} is deleted", uuid);
		return ok();
	}

	@GET
	@ApiOperation(value = "get Subscription", notes = "get Subscription by conditions", response = SubscriptionCollectionForm.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionCollectionForm findAll(@ApiParam(value = "The owner email of subscription") @QueryParam("owner_id") String ownerId,
			@ApiParam(value = "The orgnization id of subscription") @QueryParam("organization_id") String orgId,
			@ApiParam(value = "The context node id of subscription") @QueryParam("context_node_id") String contextNodeId,
			@ApiParam(value = "The project name of subscription") @QueryParam("project") String projectName, @ApiParam(value = "The status of subscription") @QueryParam("status") String status,
			@ApiParam(value = "return start position of the records") @QueryParam("marker") String marker, @ApiParam(value = "the max number of return records") @QueryParam("max") String max,
			@ApiParam(value = "whether need to use all conditions for query, value should be 'mix'") @QueryParam("query_type") String type,
			@ApiParam(value = "subscription start time query from this point") @QueryParam("start_from") String startFrom,
			@ApiParam(value = "subscription start time query until this point") @QueryParam("start_to") String startTo,
			@ApiParam(value = "subscription terminate time query from this point") @QueryParam("terminate_from") String terminateFrom,
			@ApiParam(value = "subscription terminate time query until this point") @QueryParam("terminate_to") String terminateTo,
			@ApiParam(value = "subscription product name") @QueryParam("product_filter") String product_filter,
			@ApiParam(value = "subscription product version") @QueryParam("version_filter") String version_filter,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken,
			@ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg
			) {

//		String startFromLocal = dateTransformBetweenGMTToLocal(startFrom);
//		String startToLocal = dateTransformBetweenGMTToLocal(startTo);
//		String terminateFromLocal = dateTransformBetweenGMTToLocal(terminateFrom);
//		String terminateToLocal = dateTransformBetweenGMTToLocal(terminateTo);
		
		String startFromLocal = startFrom;
		String startToLocal = startTo;
		String terminateFromLocal = terminateFrom;
		String terminateToLocal = terminateTo;


		SubscriptionCollectionForm collection = new SubscriptionCollectionForm();
		List<Subscription> subscriptions = new ArrayList<Subscription>();
		int limit = NumberUtils.toInt(max, DEFAULT_LIMIT);
		int offset = NumberUtils.toInt(marker, DEFAULT_OFFSET);
		if (!isNullOrEmpty(type) && type.equals("mix")) {
			logger.info("Geting a list of Subscription by mixed condition");
			Integer size = subscriptionService.countByCondition(ownerId, orgId, contextNodeId, projectName, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal,product_filter,version_filter);
			collection.setSize(size);
			subscriptions = subscriptionService.findByCondition(offset, limit, ownerId, orgId, contextNodeId, projectName, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal,product_filter,version_filter);
			collection.setSubscriptions(copySubscriptionList(subscriptions));
			return collection;
		}

		if (!isNullOrEmpty(ownerId)) {
			logger.info("Geting a list of Subscription by owner Id: {} and status: {}", ownerId, status);
			Integer size = subscriptionService.countByOwnerId(ownerId, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSize(size);
			subscriptions = subscriptionService.findByOwnerId(offset, limit, ownerId, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSubscriptions(copySubscriptionList(subscriptions));
			return collection;
		}

		if (!isNullOrEmpty(orgId)) {
			logger.info("Geting a list of Subscription by organization Id: {} and status: {}", orgId, status);
			Integer size = subscriptionService.countByOrgId(orgId, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSize(size);
			subscriptions = subscriptionService.findByOrgId(offset, limit, orgId, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSubscriptions(copySubscriptionList(subscriptions));
			return collection;
		}

		if (!isNullOrEmpty(contextNodeId)) {
			logger.info("Geting a list of Subscription by Context node Id: {} and status: {}", contextNodeId, status);
			Integer size = subscriptionService.countByContextNodeId(contextNodeId, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSize(size);
			subscriptions = subscriptionService.findByContextNodeId(offset, limit, contextNodeId, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSubscriptions(copySubscriptionList(subscriptions));
			return collection;
		}

		if (!isNullOrEmpty(projectName)) {
			logger.info("Geting a list of Subscription by Project Name: {} and status: {}", projectName, status);
			Integer size = subscriptionService.countByProject(projectName, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSize(size);
			subscriptions = subscriptionService.findByProject(offset, limit, projectName, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
			collection.setSubscriptions(copySubscriptionList(subscriptions));
			return collection;
		}
		Integer size = subscriptionService.countAllSubscription(status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
		collection.setSize(size);
		subscriptions = subscriptionService.findAllSubscription(offset, limit, status, startFromLocal, startToLocal, terminateFromLocal, terminateToLocal);
		collection.setSubscriptions(copySubscriptionList(subscriptions));
		return collection;
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/export/{fileName}")
	@ApiOperation(value = "export Subscription", notes = "export Subscription by conditions")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response exportContext(@ApiParam(value = "The file name of the export") @PathParam("fileName") String fileName,
			@ApiParam(value = "The owner email of subscription") @QueryParam("owner_id") String ownerId,
			@ApiParam(value = "The orgnization id of subscription") @QueryParam("organization_id") String orgId, @QueryParam("context_node_id") String contextNodeId,
			@ApiParam(value = "The project name of subscription") @QueryParam("project") String projectName, @ApiParam(value = "The status of subscription") @QueryParam("status") String status,
			@ApiParam(value = "return start position of the records") @QueryParam("marker") String marker, @ApiParam(value = "the max number of return records") @QueryParam("max") String max,
			@ApiParam(value = "subscription start time query from this point") @QueryParam("start_from") String startFrom,
			@ApiParam(value = "subscription start time query until this point") @QueryParam("start_to") String startTo,
			@ApiParam(value = "subscription terminate time query from this point") @QueryParam("terminate_from") String terminateFrom,
			@ApiParam(value = "subscription terminate time query until this point") @QueryParam("terminate_to") String terminateTo,
			@ApiParam(value = "subscription product name") @QueryParam("product_filter") String product_filter,
			@ApiParam(value = "subscription product version") @QueryParam("version_filter") String version_filter,
			@ApiParam(value = "a mapping of csa orgnization name and id") @HeaderParam("orgMapping") String orgMapping,
			@ApiParam(value = "a mapping of csa provider name and id") @HeaderParam("providerMapping") String providerMapping,
			@ApiParam(value = "a http servlet response to download") @Context HttpServletResponse response,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken,
			@ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg
			) {
		InputStream is = null;
		List<Subscription> subscriptions = new ArrayList<Subscription>();
		Map<String, String> paths = new HashMap<String, String>();
		Map<String, String> projects = new HashMap<String, String>();
		try {
			logger.info("Geting a list of Subscription by mixed condition");
			subscriptions = subscriptionService.findByCondition(0, 0, ownerId, orgId, contextNodeId, projectName, status, startFrom, startTo, terminateFrom, terminateTo,product_filter,version_filter);
			if (subscriptions == null || subscriptions.size() <= 0) {
				throwBadRequest("No Subscription Find");
			}

			SubscriptionReport sr = new SubscriptionReport();
			for (Subscription subscription : subscriptions) {
				SubscriptionRecord record = new SubscriptionRecord();
				if (!paths.containsKey(subscription.getContextNodeId())) {
					logger.info("Geting a context path by node id:" + subscription.getContextNodeId());
					String contextPath = contextNodeService.findContextPathByNodeId(subscription.getContextNodeId());
					paths.put(subscription.getContextNodeId(), contextPath);
					record.setContextNode(contextPath);
				}
				else {
					String pathName = paths.get(subscription.getContextNodeId());
					record.setContextNode(pathName);
				}

				String csaOrgName = findName(orgMapping, subscription.getCsaOrgId());
				record.setCsaOrg(csaOrgName);
				record.setName(subscription.getName());
				record.setOwnerEmail(subscription.getOwnerId());

				if (!projects.containsKey(subscription.getProject())) {
					Project project = projectService.findById(subscription.getProject());
					paths.put(subscription.getProject(), project.getName());
					record.setProject(project.getName());
				}
				else {
					String name = projects.get(subscription.getProject());
					record.setProject(name);
				}

				record.setStatus(subscription.getStatus());
				record.setStartTime(subscription.getStartTime().toGMTString());
				if (subscription.getTerminateTime() != null) {
					record.setTerminateTime(subscription.getTerminateTime().toGMTString());
				}
				else {
					record.setTerminateTime("");
				}
				record.setProperty(makeProperty(subscription));
				makeServerReport(record, subscription, providerMapping);
				sr.getSubscriptionRecord().add(record);
			}

			// marshall to string
			String ciStr = JaxbUtil.mashall(sr, "com.hp.es.cto.sp.persistence.jaxb.subscription");
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

	private String findName(String jsonString, String id) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			List<IdNameMapping> navigation = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, IdNameMapping.class));
			if (navigation != null && navigation.size() > 0) {
				for (IdNameMapping orgMap : navigation) {
					if (orgMap.getId().equals(id)) {
						return orgMap.getName();
					}
				}
			}
		}
		catch (JsonParseException e) {
			logger.warn(e.getMessage());
		}
		catch (JsonMappingException e) {
			logger.warn(e.getMessage());
		}
		catch (IOException e) {
			logger.warn(e.getMessage());
		}
		return id;
	}

	private void makeServerReport(SubscriptionRecord record, Subscription subscription, String providerMapping) {
		if (subscription.getSubscriptionServer() != null) {
			try {
				for (SubscriptionServer server : subscription.getSubscriptionServer()) {
					SubscriptionRecordServer serverRecord = new SubscriptionRecordServer();
					serverRecord.setActiveFlag(server.getActiveFlag());
					String providerName = findName(providerMapping, server.getCsaProviderId());
					serverRecord.setCsaProvider(providerName);
					serverRecord.setDns(server.getDns());
					serverRecord.setIaasProvider(server.getIaasProvider());
					serverRecord.setIpAddress(server.getIpAddress());
					serverRecord.setOsType(server.getOsType());
					serverRecord.setPrivateIpaddress(server.getPrivateIpAddress());
					serverRecord.setPublicIpAddress(server.getPublicIpAddress());
					serverRecord.setSize(server.getSize());
					serverRecord.setSimplifiedDns(server.getSimplifiedDns());
					serverRecord.setProducts(makeProducts(server));
					record.getSubscriptionRecordServer().add(serverRecord);
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.Subscription.SubscriptionServer," + " no session or session was closed");
			}
		}
	}

	private String makeProducts(SubscriptionServer subscriptionServer) {
		StringBuffer sb = new StringBuffer();
		if (subscriptionServer.getProduct() != null) {
			try {
				for (SubscriptionServerProduct product : subscriptionServer.getProduct()) {
					sb.append(product.getProductName()+(product.getProductVersion()==null?"":" "+product.getProductVersion()));
					sb.append(";");
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.SubscriptionServer.Product," + " no session or session was closed");
			}
		}
		return sb.toString();
	}

	private String makeProperty(Subscription subscription) {
		StringBuffer sb = new StringBuffer();
		if (subscription.getSubscriptionProperty() != null) {
			try {
				for (SubscriptionProperty node : subscription.getSubscriptionProperty()) {
					sb.append(node.getPropertyName());
					sb.append("=");
					sb.append(node.getPropertyValue());
					sb.append(";");
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.Subscription.SubscriptionProperty," + " no session or session was closed");
			}
		}
		return sb.toString();
	}

	private List<SubscriptionForm> copySubscriptionList(List<Subscription> subscriptions) {
		List<SubscriptionForm> forms = new ArrayList<SubscriptionForm>(subscriptions.size());
		for (Subscription subscription : subscriptions) {
			SubscriptionForm form = new SubscriptionForm();
			form.setServerIds(makeServersList(subscription));
			form.setProperties(makePropertyList(subscription));
			BeanUtils.copyProperties(subscription, form);
			forms.add(form);
		}
		return forms;
	}

	private List<SubscriptionServerForm> makeServersList(Subscription subscription) {
		List<SubscriptionServerForm> list = new ArrayList<SubscriptionServerForm>();
		if (subscription.getSubscriptionServer() != null) {
			try {
				for (SubscriptionServer server : subscription.getSubscriptionServer()) {
					SubscriptionServerForm form = new SubscriptionServerForm();
					BeanUtils.copyProperties(server, form);
					form.setProductNames(makeProductList(server));
					list.add(form);
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.Subscription.SubscriptionServer," + " no session or session was closed");
			}
		}
		return list;
	}

	private List<SubscriptionPropertyForm> makePropertyList(Subscription subscription) {
		List<SubscriptionPropertyForm> list = new ArrayList<SubscriptionPropertyForm>();
		if (subscription.getSubscriptionProperty() != null) {
			try {
				for (SubscriptionProperty node : subscription.getSubscriptionProperty()) {
					SubscriptionPropertyForm form = new SubscriptionPropertyForm();
					form.setUuid(node.getUuid());
					form.setPropertyName(node.getPropertyName());
					form.setPropertyValue(node.getPropertyValue());
					list.add(form);
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.Subscription.SubscriptionProperty," + " no session or session was closed");
			}
		}
		return list;
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

	private String dateTransformBetweenGMTToLocal(String sourceTime) {

		if (isNullOrEmpty(sourceTime)) {
			return "";
		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date sourceDate = null;
		try {
			sourceDate = formatter.parse(sourceTime);
		}
		catch (ParseException e) {
			logger.warn("failed to parse the time: " + sourceTime);
			return "";
		}

		TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT");
		TimeZone targetTimeZone = Calendar.getInstance().getTimeZone();

		Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
		return formatter.format(new Date(targetTime));
	}
	

}
