package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.service.GenericService;

public interface SubscriptionServerProductService extends GenericService<SubscriptionServerProduct> {
	
	/**
	 * Finds a list of product list by specific subscription server.
	 * 
	 * @param subscriptionServer
	 *            specific subscription server
	 * @return
	 *         a list of product
	 */
	List<SubscriptionServerProduct> findProductByServer(SubscriptionServer subscriptionServer);

	/**
	 * delete product record by server Id and product name
	 * @param serverId
	 * @param productName
	 */
	void deleteByServerIdAndProduct(String serverId, String productName);

	/**
	 * delete all products record by server Id
	 * @param serverId
	 */
	void deleteByServerId(String serverId);
}
