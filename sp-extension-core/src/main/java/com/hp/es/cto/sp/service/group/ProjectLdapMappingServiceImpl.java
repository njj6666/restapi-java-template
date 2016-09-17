package com.hp.es.cto.sp.service.group;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.group.ProjectLdapMappingDao;
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProjectLdapMappingServiceImpl extends GenericServiceImpl<ProjectLdapMapping> implements
        ProjectLdapMappingService {
    private ProjectLdapMappingDao projectLdapDao;

    public void setProjectLdapMappingDao(ProjectLdapMappingDao projectLdapDao) {
        this.projectLdapDao = projectLdapDao;
    }

    @Override
    public List<ProjectLdapMapping> findByCriteria(String csaOrgId, String projectId, String type, String[] ldapDns) {
        return projectLdapDao.findByCriteria(csaOrgId, projectId, type, ldapDns);
    }

    @Override
    public int deleteByProjectId(String projectId) {
        return projectLdapDao.deleteByProjectId(projectId);
    }

}
