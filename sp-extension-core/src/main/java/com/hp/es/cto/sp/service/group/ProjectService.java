package com.hp.es.cto.sp.service.group;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.group.Project;
import com.hp.es.cto.sp.service.GenericService;

public interface ProjectService extends GenericService<Project> {

    List<Project> findNonLdapProject(String csaOrgId);
}
