package com.hp.es.cto.sp.persistence.dao.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.group.Project;

public class ProjectDaoImpl extends GenericDaoImpl<Project> implements ProjectDao {

    @Override
    public List<Project> findNonLdapProject(String csaOrgId) {
        String sql = "select * from PROJECT where CSA_ORGANIZATION_ID=:csaOrgId and UUID not in (select distinct PROJECT_ID from PROJECT_LDAP_MAPPING where type='csa');";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("csaOrgId", csaOrgId);
        return queryEntityBySql(sql, params, Project.class);
    }

}
