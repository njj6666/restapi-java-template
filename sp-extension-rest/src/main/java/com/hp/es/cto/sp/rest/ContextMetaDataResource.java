package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.jaxb.ContextImport;
import com.hp.es.cto.sp.persistence.jaxb.ContextMetadataImport;
import com.hp.es.cto.sp.persistence.jaxb.ContextNodeImport;
import com.hp.es.cto.sp.rest.form.ContextImportForm;
import com.hp.es.cto.sp.rest.form.ContextMetaDataForm;
import com.hp.es.cto.sp.service.context.ContextMetaDataService;
import com.hp.es.cto.sp.service.context.ContextNodeService;
import com.hp.es.cto.sp.service.context.util.JaxbUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing context MetaData.
 * 
 * @author Victor
 */
@Named
@Path(ContextMetaDataResource.URI)
@Api(value = ContextMetaDataResource.URI, description = "Context Meta Data Management API")
public class ContextMetaDataResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ContextMetaDataResource.class);

	public static final String URI = "/v1/context-metadatas";

	@Inject
	private ContextMetaDataService contextMetaDataService;

	@Inject
	private ContextNodeService contextNodeService;

	private static final String XML_SUF = ".xml";

	@POST
	@ApiOperation(value = "Create Context Meta Data", notes = "Create new Context Meta Data with a meta data form", response = ContextMetaDataForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextMetaDataForm create(@ApiParam(value = "The body of the context meta data form", required = true) @MultipartForm ContextMetaDataForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String csaOrgId = form.getCsaOrgId();
		logger.info("Adding new Context Meta Data for CSA Orgnization: {}", csaOrgId);
		if (isNullOrEmpty(csaOrgId)) {
			throwBadRequest("CSA Org ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (form.getLevel() == null || form.getLevel() < 0) {
			throwBadRequest("Level is required");
		}

		ContextMetaData contextMetaData = new ContextMetaData();
		BeanUtils.copyProperties(form, contextMetaData);
		contextMetaData.setUuid(UUID.randomUUID().toString());
		contextMetaData.setCreateDate(new Date());
		try {
			contextMetaData = contextMetaDataService.create(contextMetaData);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Name is duplicated to ther level Meta Data!");
		}

		ContextMetaDataForm cmdf = new ContextMetaDataForm();
		BeanUtils.copyProperties(contextMetaData, cmdf);
		logger.info("New Context Meta Data Server {} is added for CSA Orgnization {}", cmdf.getUuid(), csaOrgId);
		return cmdf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "Update Context Meta Data", notes = "Update Context Meta Data with meta data form", response = ContextMetaDataForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextMetaDataForm update(@ApiParam(value = "uuid of the existing context meta data", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the context meta data form", required = true) @MultipartForm ContextMetaDataForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating context meta data by uuid: {}", uuid);
		String csaOrgId = form.getCsaOrgId();
		if (isNullOrEmpty(csaOrgId)) {
			throwBadRequest("CSA Org ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (form.getLevel() == null || form.getLevel() < 0) {
			throwBadRequest("Level is required");
		}

		ContextMetaData contextMetaData = contextMetaDataService.findById(uuid);
		if (contextMetaData == null) {
			throwBadRequest("MeteData not found by uuid: " + uuid);
		}

		if (!csaOrgId.equals(contextMetaData.getCsaOrgId())) {
			throwBadRequest("CSA orginization can't be changed!");
		}

		if (form.getLevel() != contextMetaData.getLevel()) {
			throwBadRequest("Level can't be changed!");
		}

		contextMetaData.setName(form.getName());

		try {
			contextMetaData = contextMetaDataService.update(contextMetaData);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Name is duplicated to ther level Meta Data!");
		}
		ContextMetaDataForm cmdf = new ContextMetaDataForm();
		BeanUtils.copyProperties(contextMetaData, cmdf);
		logger.info("Context MetaData {} is updated.", uuid);
		return cmdf;
	}

	@DELETE
	@ApiOperation(value = "Delete the context meta data", notes = "Delete the context meta data by uuid, all sub level meta datas and nodes will be deleted")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "uuid of the existing context meta data", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting Context MetaData by uuid: {}", uuid);

		ContextMetaData contextMetaData = contextMetaDataService.findById(uuid);
		if (contextMetaData == null) {
			throwNotFound("Context MetaData  not found by uuid: " + uuid);
		}
		contextMetaDataService.deleteAllRelatedContext(contextMetaData.getUuid());
		logger.info("Context MetaData {} is deleted with all sub level meta datas, related context nodes and mappings.", uuid);
		return ok();
	}

	@GET
	@Path("{uuid}")
	@ApiOperation(value = "Get context meta data", notes = "Get context meta data by uuid", response = ContextMetaDataForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextMetaDataForm findByUuid(@ApiParam(value = "uuid of the existing context meta data", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Context MetaData by uuid: {}", uuid);

		ContextMetaData contextMetaData = contextMetaDataService.findById(uuid);
		if (contextMetaData == null) {
			throwNotFound("Context MetaData  not found by uuid: " + uuid);
		}
		ContextMetaDataForm cmdf = new ContextMetaDataForm();
		BeanUtils.copyProperties(contextMetaData, cmdf);
		return cmdf;
	}

	@GET
	@ApiOperation(value = "Get context meta data", notes = "Get context meta data by conditions", response = ContextMetaDataForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ContextMetaDataForm> findAll(@ApiParam(value = "Id of CSA organization which context meta datas belong to", required = true) @QueryParam("csa_org_id") String csaOrgId,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Geting a list of context meta data: {}", csaOrgId);
		if (isNullOrEmpty(csaOrgId)) {
			throwBadRequest("CSA Org ID is required");
		}
		List<ContextMetaData> contextMetaDatas = contextMetaDataService.findByOrgId(csaOrgId);
		return copyContextMetaDataList(contextMetaDatas);
	}

	@POST
	@Path("fileload")
	@ApiOperation(value = "Import context meta data tree", notes = "Import context meta data tree with import form")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response importContext(
			@ApiParam(value = "Import form of context meta data tree, the ContextInfo will contain the Input Stream Bytes[]", required = true) @MultipartForm ContextImportForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		try {
			// Bytes to InputSteam
			byte[] input = form.getContextInfo();
			InputStream inputStream = new ByteArrayInputStream(input);
			String csaOrgId = form.getCsaOrgId();
			Object obj = JaxbUtil.unmashall(inputStream, "com.hp.es.cto.sp.persistence.jaxb");
			if (obj != null && obj instanceof ContextImport) {
				ContextImport ci = (ContextImport) obj;
				insertDb(ci, csaOrgId);
			}
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Name is duplicated to ther level Meta Data or Context node with name or diplay name under this node is exist! Can't Create!");
		}
		return ok();
	}

	@GET
	@Path("/fileload/{fileName}")
	@ApiOperation(value = "Export context meta data tree", notes = "Export context meta data tree with xml format")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Produces()
	public Response exportContext(@ApiParam(value = "Export file name", required = true) @PathParam("fileName") String fileName,
			@ApiParam(value = "Id of CSA organization which context meta datas belong to", required = true) @QueryParam("csa_org_id") String csaOrgId,
			@ApiParam(value = "the new http servlet response", required = true) @Context HttpServletResponse response,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		InputStream is = null;
		try {
			List<ContextMetaData> listInDb = contextMetaDataService.findByOrgId(csaOrgId);
			if (listInDb == null || listInDb.size() <= 0) {
				throwBadRequest("No MetaData Find");
			}

			ContextImport ci = new ContextImport();
			for (ContextMetaData md : listInDb) {
				ContextMetadataImport cmi = new ContextMetadataImport();
				cmi.setLevel(md.getLevel());
				cmi.setName(md.getName());
				ci.getContextMetadataImport().add(cmi);

				if (md.getLevel() == 0) {
					List<ContextNode> cnList = contextNodeService.findByMetaData(md);
					if (cnList == null || cnList.size() <= 0) {
						logger.info("Can't find the context node");
					}
					ContextNodeImport cni = new ContextNodeImport();
					ContextNode rootNode = cnList.get(0);
					cni.setContextMetadata(rootNode.getMetaData().getName());
					cni.setDisplayName(rootNode.getDisplayName());
					cni.setName(rootNode.getName());
					setChildNode(cni, rootNode);
					ci.setContextNodeImport(cni);
				}
			}

			// marshall to string
			String ciStr = JaxbUtil.mashall(ci, "com.hp.es.cto.sp.persistence.jaxb");
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

	private void setChildNode(ContextNodeImport parentImport, ContextNode parentNode) {
		List<ContextNode> cnList = contextNodeService.findByParent(parentNode);
		if (cnList == null || cnList.size() <= 0) {
			logger.info("Can't find the sub context node");
		}

		for (ContextNode nd : cnList) {
			ContextNodeImport cni = new ContextNodeImport();
			cni.setContextMetadata(nd.getMetaData().getName());
			cni.setName(nd.getName());
			cni.setDisplayName(nd.getDisplayName());
			setChildNode(cni, nd);
			parentImport.getChildNodeImport().add(cni);
		}
	}

	private boolean insertDb(ContextImport ci, String csaOrgId) {
		// check whether the org already has meta datas
		List<ContextMetaData> listInDb = contextMetaDataService.findByOrgId(csaOrgId);
		if (listInDb != null && listInDb.size() > 0) {
			return false;
		}

		// make the map to store mapping for metadata and uuid
		Map<String, ContextMetaData> metaDataIdMap = new HashMap<String, ContextMetaData>();

		// create the Meta Data
		List<ContextMetadataImport> metaDataList = ci.getContextMetadataImport();
		if (metaDataList != null && metaDataList.size() > 0) {
			for (ContextMetadataImport metadataImport : metaDataList) {
				ContextMetaData md = new ContextMetaData();
				md.setUuid(UUID.randomUUID().toString());
				md.setCsaOrgId(csaOrgId);
				md.setLevel(metadataImport.getLevel());
				md.setName(metadataImport.getName());
				md = contextMetaDataService.create(md);
				metaDataIdMap.put(md.getName(), md);
			}
		}

		// create the Node
		ContextNodeImport nodeImport = ci.getContextNodeImport();
		if (nodeImport != null) {
			ContextNode contextNode = new ContextNode();
			contextNode.setUuid(UUID.randomUUID().toString());
			contextNode.setName(nodeImport.getName());
			contextNode.setDisplayName(nodeImport.getDisplayName());
			contextNode.setMetaData(metaDataIdMap.get(nodeImport.getContextMetadata()));
			contextNode = contextNodeService.create(contextNode);
			addChildNode(nodeImport.getChildNodeImport(), contextNode, metaDataIdMap);
		}
		return true;
	}

	private void addChildNode(List<ContextNodeImport> childNodeImportList, ContextNode parentContextNode, Map<String, ContextMetaData> metaDataIdMap) {
		if (childNodeImportList != null && childNodeImportList.size() > 0) {
			for (ContextNodeImport nodeImport : childNodeImportList) {
				ContextNode contextNode = new ContextNode();
				contextNode.setUuid(UUID.randomUUID().toString());
				contextNode.setName(nodeImport.getName());
				contextNode.setDisplayName(nodeImport.getDisplayName());
				contextNode.setMetaData(metaDataIdMap.get(nodeImport.getContextMetadata()));
				contextNode.setParentNode(parentContextNode);
				contextNode = contextNodeService.create(contextNode);
				addChildNode(nodeImport.getChildNodeImport(), contextNode, metaDataIdMap);
			}
		}
	}

	protected static List<ContextMetaDataForm> copyContextMetaDataList(List<ContextMetaData> contextMetaDatas) {
		List<ContextMetaDataForm> forms = new ArrayList<ContextMetaDataForm>(contextMetaDatas.size());
		for (ContextMetaData contextMetaData : contextMetaDatas) {
			ContextMetaDataForm form = new ContextMetaDataForm();
			BeanUtils.copyProperties(contextMetaData, form);
			forms.add(form);
		}
		return forms;
	}

}
