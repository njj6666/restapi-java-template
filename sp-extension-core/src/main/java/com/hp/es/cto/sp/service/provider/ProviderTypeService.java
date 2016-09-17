package com.hp.es.cto.sp.service.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.provider.ProviderType;
import com.hp.es.cto.sp.service.GenericService;

public interface ProviderTypeService extends GenericService<ProviderType> {
	ProviderType findByType(String type, String subType);

	List<ProviderType> findByType(String type);
	
	List<ProviderType> findByTypeWithConstraint(String type, String orgId);
	
	void deleteByType(String type);
}
