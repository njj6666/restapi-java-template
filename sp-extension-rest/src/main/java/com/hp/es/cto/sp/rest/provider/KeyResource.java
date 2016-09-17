package com.hp.es.cto.sp.rest.provider;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.es.cto.sp.persistence.entity.keymgmt.UserKey;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;
import com.hp.es.cto.sp.rest.SPResource;
import com.hp.es.cto.sp.rest.form.ProviderKeyForm;
import com.hp.es.cto.sp.service.keymgmt.UserKeyService;
import com.hp.es.cto.sp.service.provider.ProviderKeyService;

/**
 * Tailored REST interface for OO flows
 * 
 * @author tanxu
 */
@Named
@Path("/v1/keys")
public class KeyResource extends SPResource {
	private static final Logger logger = LoggerFactory.getLogger(ProviderKeyResource.class);

	@Inject
	private UserKeyService userKeyService;
	@Inject
	private ProviderKeyService providerKeyService;

	@GET
	@Path("providers/{csa-provider-id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ProviderKeyForm> getProviderKeyByQuery(@PathParam("csa-provider-id") String providerId,
			@QueryParam("availability-zone") String zone) {
		logger.info("get provider key by provider id: {}, availability zone: {}", providerId, zone);

		List<ProviderKey> providerKeys;
		if (isNullOrEmpty(zone)) {
			providerKeys = providerKeyService.findByProviderId(providerId);
		}
		else {
			providerKeys = providerKeyService.findByProviderIdAndRegion(providerId, zone);
		}

		return ProviderKeyResource.copyProviderKeyList(providerKeys);
	}

	/**
	 * Get user's public keys
	 * 
	 * @param email
	 *            the user email
	 * @return concatenates all public keys of the user in single string, we assume this will be used to inject into
	 *         ~/.ssh/authorized_keys
	 */
	@GET
	@Path("users/{email}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserKeyByEmail(@PathParam("email") String email) {
		logger.info("get user key by email: {}", email);

		List<UserKey> userKeys = userKeyService.findByEmail(email);
		StringBuilder keys = new StringBuilder();
		for (UserKey userKey : userKeys) {
			keys.append(new String(userKey.getKeyFile()));
			keys.append("\n");
		}
		return keys.toString();
	}

	@GET
	@Path("users/{email}/{key-name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserKeyByKeyName(@PathParam("email") String email, @PathParam("key-name") String keyName) {
		logger.info("get user key by email: {}, key-name: {}", email, keyName);

		List<UserKey> userKeys = userKeyService.findByEmail(email);
		if (userKeys == null || userKeys.size() == 0) {
			throwNotFound("User key not found by email: " + email);
		}
		UserKey key = null;
		for (UserKey userKey : userKeys) {
			if (userKey.getName().equals(keyName)) {
				key = userKey;
			}
		}
		if (key == null) {
			throwNotFound("User key not found by email: " + email + ", key name: " + keyName);
		}
		return new String(key.getKeyFile());
	}
}
