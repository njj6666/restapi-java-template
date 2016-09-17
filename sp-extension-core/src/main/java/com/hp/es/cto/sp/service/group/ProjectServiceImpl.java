package com.hp.es.cto.sp.service.group;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.group.ProjectDao;
import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProjectServiceImpl extends GenericServiceImpl<Project> implements ProjectService {

    private ProjectDao projectDao;

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public List<Project> findNonLdapProject(String csaOrgId) {
        return projectDao.findNonLdapProject(csaOrgId);
    }

}
