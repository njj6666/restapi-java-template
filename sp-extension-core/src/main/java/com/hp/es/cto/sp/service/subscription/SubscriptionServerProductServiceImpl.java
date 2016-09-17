package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionServerProductDao;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class SubscriptionServerProductServiceImpl extends
		GenericServiceImpl<SubscriptionServerProduct> implements
		SubscriptionServerProductService {
	private SubscriptionServerProductDao subscriptionServerProductDao;

	public void setSubscriptionServerProductDao(SubscriptionServerProductDao subscriptionServerProductDao) {
		this.subscriptionServerProductDao = subscriptionServerProductDao;
	}

	@Override
	public List<SubscriptionServerProduct> findProductByServer(
			SubscriptionServer subscriptionServer) {
		return subscriptionServerProductDao.findProductByServer(subscriptionServer);
	}

	@Override
	public void deleteByServerIdAndProduct(String serverId, String productName) {
		subscriptionServerProductDao.deleteByServerIdAndProduct(serverId,productName);
	}

	@Override
	public void deleteByServerId(String serverId) {
		subscriptionServerProductDao.deleteByServerId(serverId);
	}

}
