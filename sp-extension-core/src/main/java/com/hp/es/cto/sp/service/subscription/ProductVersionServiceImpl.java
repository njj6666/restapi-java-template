package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.subscription.ProductVersionDao;
import com.hp.es.cto.sp.persistence.entity.subscription.ProductVersion;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class ProductVersionServiceImpl extends
		GenericServiceImpl<ProductVersion> implements
		ProductVersionService {
	private ProductVersionDao productVersionDao;

	public void setProductVersionDao(ProductVersionDao productVersionDao) {
		this.productVersionDao = productVersionDao;
	}

	@Override
	public ProductVersion findVersionByPrimaryVersion(
			ProductVersion productVersion) {
		return productVersionDao.findVersionByPrimaryVersion(productVersion);
	}

	@Override
	public ProductVersion findDefaultVersion(ProductVersion productVersion) {
		return productVersionDao.findDefaultVersion(productVersion);
	}
	
	@Override
	public List<ProductVersion> findVersionByProduct(String productName) {
		return productVersionDao.findVersionByProduct(productName);
	}



}
