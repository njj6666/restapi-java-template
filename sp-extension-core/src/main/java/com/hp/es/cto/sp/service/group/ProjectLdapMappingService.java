package com.hp.es.cto.sp.service.group;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;
import com.hp.es.cto.sp.service.GenericService;

public interface ProjectLdapMappingService extends GenericService<ProjectLdapMapping> {

    List<ProjectLdapMapping> findByCriteria(String csaOrgId, String projectId, String type, String[] ldapDns);
    
    int deleteByProjectId(String projectId);
}
