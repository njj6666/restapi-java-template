package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;

/**
 * Database access implementation class for accessing
 * SUBSCRIPTION_SERVER_PRODUCT table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class SubscriptionServerProductDaoImpl extends
		GenericDaoImpl<SubscriptionServerProduct> implements
		SubscriptionServerProductDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionServerProduct> findProductByServer(
			SubscriptionServer subscriptionServer) {
		return getHibernateTemplate().find(
				"from SubscriptionServerProduct t where t.server = ?",
				subscriptionServer);
	}

	@Override
	public void deleteByServerIdAndProduct(String serverId, String productName) {
		String sql = "delete from SUBSCRIPTION_SERVER_PRODUCT where SERVER_ID='"
				+ serverId + "' and PRODCUT_NAME='" + productName + "'";
		super.excuteBySql(sql);
	}

	@Override
	public void deleteByServerId(String serverId) {
		String sql = "delete from SUBSCRIPTION_SERVER_PRODUCT where SERVER_ID='"
				+ serverId + "'";
		super.excuteBySql(sql);
	}

}
