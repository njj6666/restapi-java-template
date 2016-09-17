package com.hp.es.cto.sp.persistence.dao.group;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.group.Project;

public interface ProjectDao extends GenericDao<Project> {

    List<Project> findNonLdapProject(String csaOrgId);
}
