package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.util.CSAClient;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for getting CSA data
 * 
 * @author Pinghua
 */
@Named
@Path(CSAProxyResource.URI)
public class CSAProxyResource extends SPResource{
	private final Logger logger = LoggerFactory.getLogger(CSAProxyResource.class);
	
	public static final String URI = "/v1/csa-proxy";
	
	@GET
	@Path("/design/{id}")
	@ApiOperation(value = "get CSA design model", notes = "get CSA design model")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Not sure"), @ApiResponse(code = 404, message = "bad") })
	@Produces({ MediaType.APPLICATION_JSON})
	public Response getCSADesign(@PathParam("id") String designId) {
		if (isNullOrEmpty(designId)) {
			throwBadRequest("Design id is required");
		}
		logger.info("Geting CSA design: {}", designId);
		try{
			CSAClient client = new CSAClient();
			String path = "/api/service/design/" + designId;
			String design = client.invoke(path,MediaType.APPLICATION_JSON);
			logger.debug(design);
			return Response.ok().entity(design).type(MediaType.APPLICATION_JSON).build();
		}catch(Exception e){
			throw new ApplicationException(e);
		}
	}
	
}
