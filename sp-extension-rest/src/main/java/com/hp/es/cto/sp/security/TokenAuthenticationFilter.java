package com.hp.es.cto.sp.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * This class is responsible for handling authentication request
 * 
 * @author Pinghua
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	public static final String AUTH_TOKEN = "X-Auth-Token";
	public static final String AUTH_USER = "X-Auth-User";
	public static final String AUTH_ORG = "X-Auth-Org";
	private final Logger logger = Logger.getLogger(TokenAuthenticationFilter.class);

	public TokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	public TokenAuthenticationFilter() {
		super("/");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		logger.info("Request is to process authentication");

		Authentication authResult;
		try {
			authResult = attemptAuthentication(request, response);
			if (authResult == null) {
				return;
			}
			SecurityContextHolder.getContext().setAuthentication(authResult);
			chain.doFilter(request, response);
		}
		catch (InternalAuthenticationServiceException failed) {
			logger.error("An internal error occurred while trying to authenticate the user.", failed);
			unsuccessfulAuthentication(request, response, failed);
		}
		catch (AuthenticationException failed) {
			unsuccessfulAuthentication(request, response, failed);
		}

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// Always trust the call from localhost
		String serverName = request.getServerName();
		if ("localhost".equals(serverName) || "127.0.0.1".equals(serverName)) {
			List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
			auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			auths.add(new SimpleGrantedAuthority("ROLE_USER"));
			return new SPAuthenticationToken("admin", "", null, null, auths);
		}

		String username = request.getHeader(AUTH_USER);
		String organization = request.getHeader(AUTH_ORG);
		String authToken = request.getHeader(AUTH_TOKEN);
		String email = request.getParameter("email");

		SPAuthenticationToken authRequest = new SPAuthenticationToken(username, authToken, organization, email);

		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

		return getAuthenticationManager().authenticate(authRequest);
	}

}
