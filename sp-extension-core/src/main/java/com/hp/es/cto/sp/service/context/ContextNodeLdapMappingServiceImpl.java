package com.hp.es.cto.sp.service.context;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.context.ContextNodeLdapMappingDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeLdapMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ContextNodeLdapMappingServiceImpl extends GenericServiceImpl<ContextNodeLdapMapping> implements ContextNodeLdapMappingService {
	private ContextNodeLdapMappingDao contextNodeLdapMappingDao;

	public void setContextNodeLdapMappingDao(ContextNodeLdapMappingDao contextNodeLdapMappingDao) {
		this.contextNodeLdapMappingDao = contextNodeLdapMappingDao;
	}

	@Override
	public List<ContextNodeLdapMapping> findByLdapGroupDn(String ldapGroupDn) {
		return contextNodeLdapMappingDao.findByLdapGroupDn(ldapGroupDn);
	}

	@Override
	public void deleteByNodeId(String nodeId) {
		 contextNodeLdapMappingDao.deleteByNodeId(nodeId);
	}

	@Override
	public List<ContextNodeLdapMapping> findByNode(ContextNode node) {
		return contextNodeLdapMappingDao.findByNode(node);
	}

}
