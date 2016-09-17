package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;

/**
 * Database access interface for accessing Product_Version table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Robin
 */
public interface ProductVersionDao extends GenericDao<ProductVersion> {
	/**
	 * Finds a list of Product-Version list by specific subscription Server.
	 * 
	 * @param subscriptionServer
	 *            specific subscription server
	 * @return
	 *         a list of Subscription Server product
	 */
	ProductVersion findVersionByPrimaryVersion(ProductVersion productVersion);
	
	ProductVersion findDefaultVersion(ProductVersion productVersion);
	
	List<ProductVersion> findVersionByProduct(String productName);


}
