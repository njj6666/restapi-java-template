package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServer;
import com.hp.es.cto.sp.persistence.entity.subscription.SubscriptionServerProduct;

/**
 * Database access implementation class for accessing
 * PRODUCT_VERSION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Robin
 */
public class ProductVersionDaoImpl extends
		GenericDaoImpl<ProductVersion> implements
		ProductVersionDao {

	@SuppressWarnings("unchecked")
	@Override
	public ProductVersion findVersionByPrimaryVersion(
			ProductVersion productVersion) {
		List<ProductVersion> productVersionList = getHibernateTemplate().find(
				"from ProductVersion t where t.productName = ? and t.primaryVersion = ? ",
				productVersion.getProductName(),productVersion.getPrimaryVersion());
		return productVersionList.size()>0?productVersionList.get(0):null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProductVersion findDefaultVersion(ProductVersion productVersion) {
		List<ProductVersion> productVersionList = getHibernateTemplate().find(
				"from ProductVersion t where t.productName = ? and t.defaultFlag = 'Y' ",
				productVersion.getProductName());
		return productVersionList.size()>0?productVersionList.get(0):null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVersion> findVersionByProduct(String productName) {
		return getHibernateTemplate().find(
				"from ProductVersion t where t.productName = ?",
				productName);
	}


}
