package com.hp.es.cto.sp.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.hp.es.cto.sp.cache.Cache;
import com.hp.es.cto.sp.security.authsource.CSAUser;

/**
 * This class is responsible for authentication
 * 
 * @author Pinghua
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
	private Cache adminCache;
	private Cache userCache;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!(authentication instanceof SPAuthenticationToken)) {
			throw new AuthenticationServiceException("Only SPAuthenticationToken is supported");
		}

		SPAuthenticationToken spAuthToken = (SPAuthenticationToken) authentication;
		String name = (String) spAuthToken.getPrincipal();
		String authToken = (String) spAuthToken.getCredentials();
		String organization = spAuthToken.getOrganization();
		String email = spAuthToken.getEmail();

		if (StringUtils.isEmpty(authToken)) {
			logger.error("No auth token found");
			throw new AuthenticationServiceException("No auth token found");
		}

		if (hasAdminPrivilege(authToken)) {
			// Set authority to admin
			logger.info("user has admin privilege");
			List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
			auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			auths.add(new SimpleGrantedAuthority("ROLE_USER"));
			return new SPAuthenticationToken("admin", authToken, null, null, auths);
		}

		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(organization)) {
			logger.error("No admin privilege. Please provide organization and username for non-admin user");
			throw new AuthenticationServiceException(
					"No admin privilege. Please provide organization and username for non-admin user");
		}
		return authenticateUser(name, organization, authToken, email);
	}

	private Authentication authenticateUser(String name, String organization, String authToken, String email) {

		try {
			CSAUser user = (CSAUser) userCache.get(organization + "::" + name);
			if (!authToken.equals(user.getId())) {
				throw new AuthenticationServiceException("Auth token " + authToken + " is not valid for user " + name);
			}

			if (!StringUtils.isEmpty(email)) {
				// Authorize the access to the secure object based on user's email
				logger.debug("email: " + email);
				if (!email.equals(user.getEmail())) {
					throw new AuthenticationServiceException("User " + name + " cannot access resources for " + email);
				}
			}

			// Set authority to user
			List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
			auths.add(new SimpleGrantedAuthority("ROLE_USER"));
			return new SPAuthenticationToken(name, authToken, organization, email, auths);
		}
		catch (Exception e) {
			logger.error("authenticateUser failed", e);
			throw new AuthenticationServiceException(e.getMessage());
		}
	}

	private boolean hasAdminPrivilege(String authToken) {
		try {
			return (boolean) adminCache.get(authToken);
		}
		catch (Exception e) {
			logger.error("hasAdminPrivilege failed", e);
			throw new AuthenticationServiceException(e.getMessage());
		}
	}
	
	public void setAdminCache(Cache adminCache) {
		this.adminCache = adminCache;
	}
	
	public void setUserCache(Cache userCache) {
		this.userCache = userCache;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SPAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
