package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.service.GenericService;

public interface ProductVersionService extends GenericService<ProductVersion> {
	
	/**
	 * Finds a list of product list by specific subscription server.
	 * 
	 * @param subscriptionServer
	 *            specific product
	 * @return
	 *        Product Version
	 */
	ProductVersion findVersionByPrimaryVersion(ProductVersion productVersion);
	
	ProductVersion findDefaultVersion(ProductVersion productVersion);

	List<ProductVersion> findVersionByProduct(String productName);

}
