package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
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

import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.rest.form.ProductVersionForm;
import com.hp.es.cto.sp.rest.form.SubscriptionServerProductForm;
import com.hp.es.cto.sp.service.subscription.ProductVersionService;
import com.hp.es.cto.sp.service.subscription.SubscriptionServerProductService;
import com.hp.es.cto.sp.service.subscription.SubscriptionServerService;
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
@Path(SubscriptionServerProductResource.URI)
@Api(value = SubscriptionServerProductResource.URI, description = "Subscription Server Product API")
public class SubscriptionServerProductResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(SubscriptionServerProductResource.class);

	public static final String URI = "/v1/subscription-server-products";

	@Inject
	private SubscriptionServerProductService subscriptionServerProductService;

	@Inject
	private SubscriptionServerService subscriptionServerService;
	
	@Inject
	private ProductVersionService productVersionService;

	@POST
	@ApiOperation(value = "create Subscription Server Product", notes = "create Subscription Server Product", response = SubscriptionServerProductForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionServerProductForm create(@ApiParam(value = "The body of the subscription server product form", required = true) @MultipartForm SubscriptionServerProductForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String serverId = form.getServerId();
		logger.info("Adding new product for subscription server: {}", serverId);
		if (isNullOrEmpty(serverId)) {
			throwBadRequest("Server ID is required");
		}
		SubscriptionServer server = subscriptionServerService.findById(serverId);
		if (server == null) {
			throwBadRequest("server is not exist");
		}
		if (isNullOrEmpty(form.getProductName())) {
			throwBadRequest("Product Name is required");
		}
		
		if (isNullOrEmpty(form.getProductVersion())) {
			throwBadRequest("Product Version is required");
		}else{
			ProductVersion productVersion = new ProductVersion();
			productVersion.setProductName(form.getProductName());
			String version = form.getProductVersion();
			if("default".equalsIgnoreCase(version)){
				productVersion = productVersionService.findDefaultVersion(productVersion);
				if(productVersion != null){
					form.setProductVersion(productVersion.getFullVersion());
				}else{
					form.setProductVersion("default");
				}
			}else{
				productVersion.setPrimaryVersion(form.getProductVersion());
				productVersion = productVersionService.findVersionByPrimaryVersion(productVersion);
				if(productVersion != null)
					form.setProductVersion(productVersion.getFullVersion());
			}
		}

		SubscriptionServerProduct subscriptionServerProduct = new SubscriptionServerProduct();
		BeanUtils.copyProperties(form, subscriptionServerProduct);
		subscriptionServerProduct.setUuid(UUID.randomUUID().toString());
		subscriptionServerProduct.setSubscriptionServer(server);

		subscriptionServerProduct = subscriptionServerProductService.create(subscriptionServerProduct);

		SubscriptionServerProductForm psf = new SubscriptionServerProductForm();
		BeanUtils.copyProperties(subscriptionServerProduct, psf);
		psf.setServerId(server.getUuid());
		logger.info("New product {} is added for subscription server {}", psf.getUuid(), serverId);
		return psf;
	}

	@GET
	@ApiOperation(value = "get Subscription Server Product", notes = "get Subscription Server Product by conditions", response = SubscriptionServerProductForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<SubscriptionServerProductForm> findAll(@ApiParam(value = "The server Id") @QueryParam("server_id") String serverId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<SubscriptionServerProduct> subscriptionServerProducts = new ArrayList<SubscriptionServerProduct>();
		if (!isNullOrEmpty(serverId)) {
			logger.info("Geting a list of subscription Server Products by server Id: {}", serverId);
			SubscriptionServer server = subscriptionServerService.findById(serverId);
			if (server == null) {
				throwBadRequest("server is not exist");
			}

			subscriptionServerProducts = subscriptionServerProductService.findProductByServer(server);
			return copySubscriptionProductList(subscriptionServerProducts);
		}

		subscriptionServerProducts = subscriptionServerProductService.findAll();
		return copySubscriptionProductList(subscriptionServerProducts);
	}
	
	@PUT
	@Path("{uuid}/{version}")
	@ApiOperation(value = "update product version", notes = "update product version information", response = SubscriptionServerProduct.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SubscriptionServerProductForm update(@ApiParam(value = "The uuid of the specific product information record", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "target version", required = true) @PathParam("version") String version,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		SubscriptionServerProduct serverProduct = subscriptionServerProductService.findById(uuid);

		serverProduct.setProductVersion(version);

		serverProduct = subscriptionServerProductService.update(serverProduct);

		SubscriptionServerProductForm psf = new SubscriptionServerProductForm();
		BeanUtils.copyProperties(serverProduct, psf);
		psf.setServerId(serverProduct.getSubscriptionServer().getUuid());
		return psf;
	}

	@DELETE
	@ApiOperation(value = "delete Server Product by server id and product name", notes = "delete Server Product by server id and product name")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "The server Id", required = true) @QueryParam("server_id") String serverId,
			@ApiParam(value = "The name of product on the server") @QueryParam("product_name") String productName,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting provider Server product by serverId: {} and product Name: {}", serverId, productName);
		if (isNullOrEmpty(serverId)) {
			throwBadRequest("Server ID is required");
		}

		if (!isNullOrEmpty(productName)) {
			subscriptionServerProductService.deleteByServerIdAndProduct(serverId, productName);
		}
		else {
			subscriptionServerProductService.deleteByServerId(serverId);
		}

		logger.info("product record {} is deleted by serverId: {} and product Name: {}", serverId, productName);
		return ok();
	}
	

	private List<SubscriptionServerProductForm> copySubscriptionProductList(List<SubscriptionServerProduct> subscriptionServerProducts) {
		List<SubscriptionServerProductForm> forms = new ArrayList<SubscriptionServerProductForm>(subscriptionServerProducts.size());
		for (SubscriptionServerProduct subscriptionServerProduct : subscriptionServerProducts) {
			SubscriptionServerProductForm form = new SubscriptionServerProductForm();
			if (subscriptionServerProduct.getSubscriptionServer() != null) {
				form.setServerId(subscriptionServerProduct.getSubscriptionServer().getUuid());
			}
			BeanUtils.copyProperties(subscriptionServerProduct, form);
			forms.add(form);
		}
		return forms;
	}
	
	@GET
	@Path("/versions")
	@ApiOperation(value = "Get all available product", notes = "Get all available product", response = ProductVersionForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProductVersionForm> findAllProduct(
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<ProductVersion> productVersions = new ArrayList<ProductVersion>();
		productVersions = productVersionService.findAll();
		return copyProductVersionList(productVersions);
	}
	
	@GET
	@Path("/versions/{productName}")
	@ApiOperation(value = "Get all available product", notes = "Get all available product", response = ProductVersionForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProductVersionForm> findVersionByProudct(@ApiParam(value = "The uuid of the specific product information record", required = true) @PathParam("productName") String productName,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<ProductVersion> productVersions = new ArrayList<ProductVersion>();
		productVersions = productVersionService.findVersionByProduct(productName);
		return copyProductVersionList(productVersions);
	}
	

	protected static List<ProductVersionForm> copyProductVersionList(Collection<ProductVersion> productVersions) {
		List<ProductVersionForm> forms = new ArrayList<ProductVersionForm>(productVersions.size());
		for (ProductVersion productVersion : productVersions) {
			ProductVersionForm form = new ProductVersionForm();
			BeanUtils.copyProperties(productVersion, form);
			forms.add(form);
		}
		return forms;
	}

}
