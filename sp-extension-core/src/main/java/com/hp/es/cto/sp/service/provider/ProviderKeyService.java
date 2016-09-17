package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderKey;
import com.hp.es.cto.sp.service.GenericService;

public interface ProviderKeyService extends GenericService<ProviderKey> {

	List<ProviderKey> findByProviderIdAndRegion(String providerId, String region);

    List<ProviderKey> findByProviderId(String providerId);
}
