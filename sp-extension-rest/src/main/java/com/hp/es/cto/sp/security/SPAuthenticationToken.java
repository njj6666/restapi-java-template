package com.hp.es.cto.sp.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SPAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = -8832958505594834826L;
	private Object principal;
	private Object credentials;
	private String organization;
	private String email;

	public SPAuthenticationToken(Object principal, Object credentials, String organization, String email) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		this.organization = organization;
		this.email = email;
		super.setAuthenticated(false);
	}

	public SPAuthenticationToken(Object principal, Object credentials, String organization, String email,
			Collection<? extends GrantedAuthority> authority) {
		super(authority);
		this.principal = principal;
		this.credentials = credentials;
		this.organization = organization;
		this.email = email;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public String getOrganization() {
		return this.organization;
	}

	public String getEmail() {
		return this.email;
	}
}
