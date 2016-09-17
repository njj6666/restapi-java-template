package com.hp.es.cto.sp.persistence.dao.group;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;

public interface ProjectLdapMappingDao extends GenericDao<ProjectLdapMapping> {

/**
 * Database access interface for project ldap mapping table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Nick
 */
    List<ProjectLdapMapping> findByCriteria(String csaOrgId, String projectId, String type, String[] ldapDns);
    
    int deleteByProjectId(String projectId);
}
