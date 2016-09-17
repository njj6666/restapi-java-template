package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;

/**
 * Database access interface for accessing SUBSCRIPTION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface SubscriptionServerProductDao extends GenericDao<SubscriptionServerProduct> {
	/**
	 * Finds a list of Product list by specific subscription Server.
	 * 
	 * @param subscriptionServer
	 *            specific subscription server
	 * @return
	 *         a list of Subscription Server product
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
