package com.hp.es.cto.sp.rest;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.spi.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.rest.form.LdapGroupForm;
import com.hp.es.cto.sp.service.ldap.LdapQueryService;

/**
 * REST resource class for querying objects on LDAP server.
 * 
 * @author Dream
 * 
 */
@Named
@Path("/v1/ldap-api/groups")
public class LdapGroupResource {
    private final Logger logger = LoggerFactory.getLogger(LdapGroupResource.class);

    @Inject
    private LdapQueryService ldapQueryService;

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<LdapGroupForm> findAll(@QueryParam("ldap-url") String ldapUrl, @QueryParam("group-cn") String groupCn) {
        List<LdapGroupForm> result = new ArrayList<LdapGroupForm>();

        if (isNullOrEmpty(ldapUrl) || isNullOrEmpty(groupCn)) {
            throw new BadRequestException("Parameter ldap-url and group-cn can't be empty.");
        }
        else {
            logger.info("Get ldap groups: {}, {}", ldapUrl, groupCn);
            Properties groupProp = ldapQueryService.findGroupByName(ldapUrl, groupCn);
            LdapGroupForm groupForm = new LdapGroupForm();
            groupForm.setDn(groupProp.getProperty("dn"));
            groupForm.setCn(groupProp.getProperty("cn"));

            result.add(groupForm);
            return result;
        }
    }
}
