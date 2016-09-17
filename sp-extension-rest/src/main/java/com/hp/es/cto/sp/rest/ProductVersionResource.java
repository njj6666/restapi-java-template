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

import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.rest.form.ProductVersionForm;
import com.hp.es.cto.sp.rest.form.ProjectForm;
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
@Path(ProductVersionResource.URI)
@Api(value = ProductVersionResource.URI, description = "Product Version API")
public class ProductVersionResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ProductVersionResource.class);

	public static final String URI = "/v1/product-versions";
	
	@Inject
	private ProductVersionService productVersionService;



	@GET
	@ApiOperation(value = "Get all available product", notes = "Get all available product", response = ProductVersionForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProductVersionForm> findAll(
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken, @ApiParam(value = "user name of csa user") @HeaderParam("X-Auth-User") String authUser,
			@ApiParam(value = "csa organization name of csa user") @HeaderParam("X-Auth-Org") String authOrg) {
		List<ProductVersion> productVersions = new ArrayList<ProductVersion>();
		productVersions = productVersionService.findAll();
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
