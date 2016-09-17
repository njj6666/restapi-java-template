package com.hp.es.cto.sp.service.context;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.context.ContextNodeProviderMappingDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.persistence.entity.context.ContextNodeProviderMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ContextNodeProviderMappingServiceImpl extends GenericServiceImpl<ContextNodeProviderMapping> implements ContextNodeProviderMappingService {
	private ContextNodeProviderMappingDao contextNodeProviderMappingDao;

	public void setContextNodeProviderMappingDao(ContextNodeProviderMappingDao contextNodeProviderMappingDao) {
		this.contextNodeProviderMappingDao = contextNodeProviderMappingDao;
	}

	@Override
	public List<ContextNodeProviderMapping> findProviderByNode(ContextNode node) {
		return contextNodeProviderMappingDao.findProviderByNode(node);
	}

	@Override
	public void deleteByNodeId(String nodeId) {
		contextNodeProviderMappingDao.deleteByNodeId(nodeId);
	}
}
