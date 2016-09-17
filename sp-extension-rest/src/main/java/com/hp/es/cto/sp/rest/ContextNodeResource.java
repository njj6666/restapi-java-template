package com.hp.es.cto.sp.rest;

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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.hibernate.LazyInitializationException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;
import com.hp.es.cto.sp.rest.form.ContextNodeForm;
import com.hp.es.cto.sp.rest.form.ContextNodeLdapMappingForm;
import com.hp.es.cto.sp.rest.form.ContextNodeProviderMappingForm;
import com.hp.es.cto.sp.service.context.ContextMetaDataService;
import com.hp.es.cto.sp.service.context.ContextNodeService;
import com.hp.es.cto.sp.service.ldap.util.LdapUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST resource class for managing context Node.
 * 
 * @author Victor
 */
@Named
@Path(ContextNodeResource.URI)
@Api(value = ContextNodeResource.URI, description = "Context Node Management API")
public class ContextNodeResource extends SPResource {
	private final Logger logger = LoggerFactory.getLogger(ContextNodeResource.class);

	public static final String URI = "/v1/context-nodes";
	@Inject
	private ContextNodeService contextNodeService;

	@Inject
	private ContextMetaDataService contextMetaDataService;

	@POST
	@ApiOperation(value = "Create Context Node", notes = "Create new Context Node with a context node form", response = ContextNodeForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeForm create(@ApiParam(value = "The body of the context node form", required = true) @MultipartForm ContextNodeForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		String metaDataId = form.getContextMetaDataId();
		ContextNode contextNode = new ContextNode();
		logger.info("Adding new Context Node for MetaData: {}", metaDataId);
		if (isNullOrEmpty(metaDataId)) {
			throwBadRequest("Context Meta Data ID is required");
		}
		ContextMetaData metaData = contextMetaDataService.findById(metaDataId);
		if (metaData == null) {
			throwBadRequest("meta Data is not exist");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}
		if (!isNullOrEmpty(form.getParentNodeId())) {
			ContextNode parentNode = contextNodeService.findById(form.getParentNodeId());
			contextNode.setParentNode(parentNode);
		}

		contextNode.setMetaData(metaData);
		BeanUtils.copyProperties(form, contextNode);
		contextNode.setUuid(UUID.randomUUID().toString());
		contextNode.setCreateDate(new Date());

		try {
			contextNode = contextNodeService.create(contextNode);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Context node with name or diplay name under this node is exist! Can't Create");
		}

		ContextNodeForm cnf = new ContextNodeForm();
		if (contextNode.getParentNode() != null) {
			cnf.setParentNodeId(contextNode.getParentNode().getUuid());
		}
		cnf.setContextMetaDataId(contextNode.getMetaData().getUuid());
		cnf.setChildNodeIds(makeChildNodesList(contextNode));
		cnf.setLdapMappings(makeLdapMappingsList(contextNode));
		cnf.setProviderMappings(makeProviderMappingsList(contextNode));
		BeanUtils.copyProperties(contextNode, cnf);
		logger.info("New Context Node  {} is added for MetaData {}", cnf.getUuid(), metaDataId);
		return cnf;
	}

	@PUT
	@Path("{uuid}")
	@ApiOperation(value = "Update Context Node", notes = "Update Context Node with context node form", response = ContextNodeForm.class, consumes = MediaType.MULTIPART_FORM_DATA)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest"), @ApiResponse(code = 404, message = "NotFound") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeForm update(@ApiParam(value = "uuid of the existing context node", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "The body of the context node form", required = true) @MultipartForm ContextNodeForm form,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Updating context meta data by uuid: {}", uuid);
		String metaDataId = form.getContextMetaDataId();
		if (isNullOrEmpty(metaDataId)) {
			throwBadRequest("Context Meta Data ID is required");
		}
		if (isNullOrEmpty(form.getName())) {
			throwBadRequest("Name is required");
		}

		ContextNode contextNode = contextNodeService.findById(uuid);
		if (contextNode == null) {
			throwBadRequest("Context Node not found by uuid: " + uuid);
		}

		if (!metaDataId.equals(contextNode.getMetaData().getUuid())) {
			throwBadRequest("Context Meta Data ID can't be changed!");
		}

		if (contextNode.getParentNode() != null) {
			if (!form.getParentNodeId().equals(contextNode.getParentNode().getUuid())) {
				throwBadRequest("Context ParentID can't be changed!");
			}
		}
		else {
			if (form.getParentNodeId() != null) {
				throwBadRequest("Context ParentID can't be changed!");
			}
		}

		contextNode.setName(form.getName());
		contextNode.setDisplayName(form.getDisplayName());
		try {
			contextNode = contextNodeService.update(contextNode);
		}
		catch (DataIntegrityViolationException e) {
			throwBadRequest("Context node with name or diplay name under this node is exist! Can't Update");
		}

		ContextNodeForm cnf = new ContextNodeForm();
		if (contextNode.getParentNode() != null) {
			cnf.setParentNodeId(contextNode.getParentNode().getUuid());
		}
		cnf.setContextMetaDataId(contextNode.getMetaData().getUuid());
		cnf.setChildNodeIds(makeChildNodesList(contextNode));
		cnf.setLdapMappings(makeLdapMappingsList(contextNode));
		cnf.setProviderMappings(makeProviderMappingsList(contextNode));
		BeanUtils.copyProperties(contextNode, cnf);
		logger.info("Context Node Info {} is updated.", uuid);
		return cnf;
	}

	@DELETE
	@ApiOperation(value = "Delete the context node", notes = "Delete the context node by uuid, all sub level nodes and mappings will be deleted")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@ApiParam(value = "uuid of the existing context node", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Deleting Context Node by uuid: {}", uuid);

		ContextNode contextNode = contextNodeService.findById(uuid);
		if (contextNode == null) {
			throwNotFound("Context Node not found by uuid: " + uuid);
		}
		contextNodeService.removeAllNodesAndMappingByNodeId(contextNode.getUuid());
		logger.info("Context Node {} is deleted with all sub level context nodes and mappings.", uuid);
		return ok();
	}

	@GET
	@ApiOperation(value = "Get context node", notes = "Get context node by uuid", response = ContextNodeForm.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NotFound") })
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContextNodeForm findByUuid(@ApiParam(value = "uuid of the existing context node", required = true) @PathParam("uuid") String uuid,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		logger.info("Getting Context Node by uuid: {}", uuid);

		ContextNode contextNode = contextNodeService.findById(uuid);
		if (contextNode == null) {
			throwNotFound("Context Node not found by uuid: " + uuid);
		}
		ContextNodeForm cnf = new ContextNodeForm();
		if (contextNode.getParentNode() != null) {
			cnf.setParentNodeId(contextNode.getParentNode().getUuid());
		}
		cnf.setContextMetaDataId(contextNode.getMetaData().getUuid());
		cnf.setChildNodeIds(makeChildNodesList(contextNode));
		cnf.setLdapMappings(makeLdapMappingsList(contextNode));
		cnf.setProviderMappings(makeProviderMappingsList(contextNode));
		BeanUtils.copyProperties(contextNode, cnf);
		return cnf;
	}

	@GET
	@ApiOperation(value = "Get context node", notes = "Get context nodes by conditions", response = ContextNodeForm.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ContextNodeForm> findAll(@ApiParam(value = "uuid of the context meta data which context nodes belong to") @QueryParam("context_metadata_id") String meteDataId,
			@ApiParam(value = "the id of context node with is the parent") @QueryParam("parent_node_id") String parentId,
			@ApiParam(value = "the id of csa organization which context nodes belong to") @QueryParam("csa_org_id") String csaOrgId,
			@ApiParam(value = "constraint ldaps which assoicated with the context nodes") @QueryParam("ldap_dns") String LdapDns,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		List<ContextNode> contextNodes = new ArrayList<ContextNode>();
		if (!isNullOrEmpty(meteDataId)) {
			logger.info("Geting a list of context Node by meta data id: {}", meteDataId);
			ContextMetaData metaData = contextMetaDataService.findById(meteDataId);
			if (metaData == null) {
				throwBadRequest("meta Data is not exist");
			}
			contextNodes = contextNodeService.findByMetaData(metaData);
			return copyContextNodeList(contextNodes);
		}

		if (!isNullOrEmpty(LdapDns)) {
			if (!isNullOrEmpty(parentId)) {
				logger.info("Geting a list of context Node by parent id: {} and constraint ldap dns: {}", parentId, LdapDns);
				List<String> groupDnList = makeGroupDnList(LdapDns);
				contextNodes = contextNodeService.findAllByParentIdAndLdapDns(parentId, groupDnList);
				return copyContextNodeList(contextNodes);
			}
			else if (!isNullOrEmpty(csaOrgId)) {
				logger.info("Geting a list of context Node by CSA orginization id: {} and constraint ldap dns: {}", csaOrgId, LdapDns);
				List<String> groupDnList = makeGroupDnList(LdapDns);
				contextNodes = contextNodeService.findAllByOrgidAndLdapDns(csaOrgId, groupDnList);
				return copyContextNodeList(contextNodes);

			}
			else {
				throwBadRequest("parentId or Org Id  is required for ldap related query");
			}
		}

		if (!isNullOrEmpty(csaOrgId)) {
			logger.info("Geting a list of context Node by csa org id: {}", csaOrgId);
			contextNodes = contextNodeService.findByCsaOrgId(csaOrgId);
			return copyContextNodeList(contextNodes);
		}

		if (!isNullOrEmpty(parentId)) {
			logger.info("Geting a list of context Node by parent id: {}", csaOrgId);
			ContextNode parentNode = contextNodeService.findById(parentId);
			if (parentNode == null) {
				throwBadRequest("parent Node is not exist");
			}
			contextNodes = contextNodeService.findByParent(parentNode);
			return copyContextNodeList(contextNodes);
		}
		contextNodes = contextNodeService.findAll();
		return copyContextNodeList(contextNodes);
	}

	@GET
	@ApiOperation(value = "Get ids of providers constraint by context node and ldap dns", notes = "Get providers constraint by context node and ldap dns", response = String.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BadRequest") })
	@Path("providers")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<String> findAllAvailableProvidersByNodeIdAndLdapDns(
			@ApiParam(value = "constraint the providers associated with this context node and sub level nodes", required = true) @QueryParam("context_node_id") String contextNodeId, 
			@ApiParam(value = "constraint the providers associated with the context node which bind with these ldap dns") @QueryParam("ldap_dns") String LdapDns,
			@ApiParam(value = "the flag of whether also need to find providers assoicated with this node's parent") @QueryParam("parentflag") boolean parentflag,
			@ApiParam(value = "auth token of csa user") @HeaderParam("X-Auth-Token") String authToken) {
		List<String> providers = new ArrayList<String>();
		if (isNullOrEmpty(contextNodeId)) {
			throwBadRequest("Context node is required");
		}

		if (parentflag) {
			logger.info("Geting a list of parent node providers by context Node id: {} ", contextNodeId);
			providers = contextNodeService.findParentProvidersByNodeId(contextNodeId);
		}
		else {
			logger.info("Geting a list of providers by context Node id: {} and  Ldap dns: {}", contextNodeId, LdapDns);
			List<String> groupDnList = makeGroupDnList(LdapDns);
			providers = contextNodeService.findProvidersByNodeIdAndLdapDns(contextNodeId, groupDnList);
		}
		return providers;
	}

	protected static List<String> makeGroupDnList(String ldap_dns) {
		String decodeLdapDn = StringUtils.newStringUtf8(Base64.decodeBase64(ldap_dns));
		List<String> groupDnList = new ArrayList<String>();
		if (decodeLdapDn != null && decodeLdapDn.length() > 0) {
			String[] ldap_dn = decodeLdapDn.split(";");
			for (int i = 0; i < ldap_dn.length; i++) {
				String dn = ldap_dn[i];
				List<String> list = LdapUtil.getParentsDn(dn);
				groupDnList.addAll(list);
				groupDnList.add(dn);
			}
		}
		return groupDnList;
	}

	protected List<ContextNodeForm> copyContextNodeList(List<ContextNode> ContextNodes) {
		List<ContextNodeForm> forms = new ArrayList<ContextNodeForm>(ContextNodes.size());
		for (ContextNode contextNode : ContextNodes) {
			ContextNodeForm form = new ContextNodeForm();
			if (contextNode.getParentNode() != null) {
				form.setParentNodeId(contextNode.getParentNode().getUuid());
			}
			form.setContextMetaDataId(contextNode.getMetaData().getUuid());
			form.setChildNodeIds(makeChildNodesList(contextNode));
			form.setLdapMappings(makeLdapMappingsList(contextNode));
			form.setProviderMappings(makeProviderMappingsList(contextNode));
			BeanUtils.copyProperties(contextNode, form);
			forms.add(form);
		}
		return forms;
	}

	private List<ContextNodeLdapMappingForm> makeLdapMappingsList(ContextNode contextNode) {
		List<ContextNodeLdapMappingForm> list = new ArrayList<ContextNodeLdapMappingForm>();
		if (contextNode.getNodeLdaps() != null) {
			try {
				for (ContextNodeLdapMapping node : contextNode.getNodeLdaps()) {
					ContextNodeLdapMappingForm form = new ContextNodeLdapMappingForm();
					BeanUtils.copyProperties(node, form);
					list.add(form);
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.ContextNode.ldapmapping," + " no session or session was closed");
			}
		}
		return list;
	}

	private List<ContextNodeProviderMappingForm> makeProviderMappingsList(ContextNode contextNode) {
		List<ContextNodeProviderMappingForm> list = new ArrayList<ContextNodeProviderMappingForm>();
		if (contextNode.getNodeProviders() != null) {
			try {
				for (ContextNodeProviderMapping node : contextNode.getNodeProviders()) {
					ContextNodeProviderMappingForm form = new ContextNodeProviderMappingForm();
					BeanUtils.copyProperties(node, form);
					list.add(form);
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.ContextNode.providermapping," + " no session or session was closed");
			}
		}
		return list;
	}

	private List<String> makeChildNodesList(ContextNode contextNode) {
		List<String> list = new ArrayList<String>();
		if (contextNode.getChildNodes() != null) {
			try {
				for (ContextNode node : contextNode.getChildNodes()) {
					list.add(node.getUuid());
				}
			}
			catch (LazyInitializationException e) {
				logger.warn("failed to lazily initialize a collection of role: " + "com.hp.es.cto.sp.persistence.entity.ContextNode.childNodes," + " no session or session was closed");
			}
		}

		return list;
	}
}
