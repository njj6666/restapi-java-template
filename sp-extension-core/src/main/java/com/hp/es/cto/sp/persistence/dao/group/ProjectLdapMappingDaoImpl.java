package com.hp.es.cto.sp.persistence.dao.group;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.group.ProjectLdapMapping;

/**
 * Database access implementation class for project ldap mapping table.
 * 
 * @author Nick
 */
public class ProjectLdapMappingDaoImpl extends GenericDaoImpl<ProjectLdapMapping> implements ProjectLdapMappingDao {

    @Override
    public List<ProjectLdapMapping> findByCriteria(final String csaOrgId, final String projectId, final String type,
            final String[] ldapDns) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(ProjectLdapMapping.class, "plm");
                criteria.createAlias("plm.project", "p", Criteria.LEFT_JOIN);
                if (!isNullOrEmpty(csaOrgId)) {
                    criteria.add(Restrictions.eq("p.csaOrganizationId", csaOrgId));
                }
                if (!isNullOrEmpty(projectId)) {
                    criteria.add(Restrictions.eq("p.uuid", projectId));
                }
                if (!isNullOrEmpty(type)) {
                    criteria.add(Restrictions.eq("plm.type", type));
                }
                if (ldapDns != null && ldapDns.length != 0) {
                    criteria.add(Restrictions.in("plm.ldapDn", ldapDns));
                }
                return (List<ProjectLdapMapping>) criteria.list();
            }
        });
    }

    @Override
    public int deleteByProjectId(final String projectId) {
        return excuteBySql("delete from PROJECT_LDAP_MAPPING where PROJECT_ID=?", projectId);
    }
}
