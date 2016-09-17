package com.hp.es.cto.sp.persistence.dao.provider;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderServer;

/**
 * Database access implementation class for accessing PROVIDER_RESOURCE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class ProviderServerDaoImpl extends GenericDaoImpl<ProviderServer> implements ProviderServerDao {

	static final String SELECT_FROM_PROVIDER_SERVER = " Select * from PROVIDER_SERVER ";

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderServer> findByProviderId(String iaasProviderId) {
		return getHibernateTemplate().find("from ProviderServer t where t.csaProviderId = ?", iaasProviderId);
	}

	@Override
	public void deleteByProviderId(String iaasProviderId) {
		String sql = "delete from PROVIDER_SERVER where CSA_PROVIDER_ID='" + iaasProviderId + "'";
		super.excuteBySql(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderServer> findByProviderIdAndAllocated(String iaasProviderId, String isAllocated) {
		return getHibernateTemplate().find("from ProviderServer t where t.csaProviderId = ? and isAllocated = ?", iaasProviderId, isAllocated);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderServer> findByProviderIdAndAllocatedAndSize(String providerId, String isAllocated, String size) {
		return getHibernateTemplate().find("from ProviderServer t where t.csaProviderId = ? and isAllocated = ? and size = ?", providerId, isAllocated, size);

	}

	@Override
	public List<ProviderServer> findByConditions(String providerId, String isAllocated, String size, String name, String osType, String ipAddress, String publicIpAddress, String privateIpAddress, String provider) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_PROVIDER_SERVER);
		if (!isNullOrEmpty(providerId)) {
			appendCondition(sb, params, "CSA_PROVIDER_ID", "LIKE", "providerId", "%" + providerId + "%");
		}
		if (!isNullOrEmpty(isAllocated)) {
			appendCondition(sb, params, "ISALLOCATED", "LIKE", "isAllocated", "%" + isAllocated + "%");
		}
		if (!isNullOrEmpty(size)) {
			appendCondition(sb, params, "SIZE", "LIKE", "size", "%" + size + "%");
		}
		if (!isNullOrEmpty(name)) {
			appendCondition(sb, params, "NAME", "LIKE", "name", "%" + name + "%");
		}
		if (!isNullOrEmpty(osType)) {
			appendCondition(sb, params, "OS_TYPE", "LIKE", "osType", "%" + osType + "%");
		}
		if (!isNullOrEmpty(ipAddress)) {
			appendCondition(sb, params, "IP_ADDRESS", "LIKE", "ipAddress", "%" + ipAddress + "%");
		}
		if (!isNullOrEmpty(publicIpAddress)) {
			appendCondition(sb, params, "PUBLIC_IP_ADDRESS", "LIKE", "publicIpAddress", "%" + publicIpAddress + "%");
		}
		if (!isNullOrEmpty(privateIpAddress)) {
			appendCondition(sb, params, "PRIVATE_IP_ADDRESS", "LIKE", "privateIpAddress", "%" + privateIpAddress + "%");
		}
		if (!isNullOrEmpty(provider)) {
			appendCondition(sb, params, "PROVIDER", "LIKE", "provider", "%" + provider + "%");
		}
		return super.queryEntityBySql(sb.toString(), params, ProviderServer.class, 0, 0);

	}

}
