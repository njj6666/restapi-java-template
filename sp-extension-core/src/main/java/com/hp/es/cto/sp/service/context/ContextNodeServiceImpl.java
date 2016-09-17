package com.hp.es.cto.sp.service.context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.dao.context.ContextNodeDao;
import com.hp.es.cto.sp.persistence.entity.context.ContextMetaData;
import com.hp.es.cto.sp.persistence.entity.context.ContextNode;
import com.hp.es.cto.sp.service.GenericServiceImpl;
import com.hp.es.cto.sp.spring.AppContext;
import com.hp.es.cto.sp.spring.BeanNames;

@SuppressWarnings("deprecation")
@Named
public class ContextNodeServiceImpl extends GenericServiceImpl<ContextNode> implements ContextNodeService {
	private ContextNodeDao contextNodeDao;
	private final Logger logger = LoggerFactory.getLogger(ContextNodeServiceImpl.class);
	
	public ContextNodeServiceImpl(){
		try{
			ApplicationContext ac = AppContext.getApplicationContext();
			String driverClass =  AppContext.getDriverManagerDataSourceClassUrl();
			logger.info("driver class url:" + driverClass );
			
			if(driverClass.contains("postgres")){
				logger.info("init the context node dao with postgres" );
				this.setContextNodeDao((ContextNodeDao)ac.getBean(BeanNames.CONTEXT_NODE_DAO_POSTGRES));
				this.setGenericDao((ContextNodeDao)ac.getBean(BeanNames.CONTEXT_NODE_DAO_POSTGRES));
			}else if(driverClass.contains("sqlserver")){
				logger.info("init the context node dao with ms sql" );
				this.contextNodeDao = (ContextNodeDao)ac.getBean(BeanNames.CONTEXT_NODE_DAO_MSSQL);
				this.setGenericDao((ContextNodeDao)ac.getBean(BeanNames.CONTEXT_NODE_DAO_MSSQL));
			}else{
				this.contextNodeDao = (ContextNodeDao)ac.getBean(BeanNames.CONTEXT_NODE_DAO_MSSQL);
				this.setGenericDao((ContextNodeDao)ac.getBean(BeanNames.CONTEXT_NODE_DAO_MSSQL));
			}
		}catch(Exception e) {
			logger.error("Not able to find the context node type use default",e);
		}
	}

	public void setContextNodeDao(ContextNodeDao contextNodeDao) {
		this.contextNodeDao = contextNodeDao;
	}

	@Override
	public List<ContextNode> findByParent(ContextNode parentNode) {
		return contextNodeDao.findByParent(parentNode);
	}

	@Override
	public List<ContextNode> findByMetaData(ContextMetaData metaData) {
		return contextNodeDao.findByMetaData(metaData);
	}

	@Override
	public List<ContextNode> findByCsaOrgId(String csaOrgId) {
		String sql = "SELECT * from CONTEXT_NODE where CONTEXT_META_DATA_ID in ( select META.UUID from CONTEXT_META_DATA META WHERE META.CSA_ORG_ID = ? ) order by CREATE_DATE";
		List<ContextNode> orgContextNode = contextNodeDao.queryEntityBySql(sql, ContextNode.class, csaOrgId);
		return orgContextNode;
	}

	@Override
	public List<ContextNode> findAllByOrgidAndLdapDns(String OrgId, List<String> ldapDnList) {
		String sql = "SELECT UUID from CONTEXT_NODE where CONTEXT_META_DATA_ID in ( select META.UUID from CONTEXT_META_DATA META WHERE META.LEVEL=0 AND META.CSA_ORG_ID = ? ) and PARENT_NODE_ID is null";
		List<String> orgContextNode = contextNodeDao.queryStringPropertyBySql(sql, "UUID", OrgId);
		if (orgContextNode.size() == 1) {
			String orgnodeid = orgContextNode.get(0);
			return findAllByParentIdAndLdapDns(orgnodeid, ldapDnList);
		}
		return new ArrayList<ContextNode>();
	}

	@Override
	public List<ContextNode> findAllByParentIdAndLdapDns(String parentNodeId, List<String> ldapDnList) {
		return contextNodeDao.findAllByParentIdAndLdapDns(parentNodeId, ldapDnList);
	}

	@Override
	public List<String> findProvidersByNodeIdAndLdapDns(String nodeId, List<String> ldapDnList) {
		return contextNodeDao.findProvidersByNodeIdAndLdapDns(nodeId, ldapDnList);
	}

	@Override
	public List<String> findParentProvidersByNodeId(String nodeId) {
		return contextNodeDao.findParentProvidersByNodeId(nodeId);
	}

	@Override
	public void removeAllNodesAndMappingByNodeId(String nodeId) {
		ContextNode node = contextNodeDao.findById(nodeId);
		if (node != null) {
			contextNodeDao.delete(node);
		}
	}

	@Override
	public String findContextPathByNodeId(String nodeId) {
		try {
			List<String> paths = contextNodeDao.findContextPathByNodeId(nodeId);
			if (paths != null && paths.size() > 0) {
				return paths.get(0);
			}
		}
		catch (Exception e) {
			logger.warn("Not able to get the context path with node id:<" + nodeId + ">" + e.getMessage(), e);
			return "";
		}
		return "";
	}
}
