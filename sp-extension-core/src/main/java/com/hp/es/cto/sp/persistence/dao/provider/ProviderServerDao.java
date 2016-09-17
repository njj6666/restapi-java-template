package com.hp.es.cto.sp.persistence.dao.provider;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.provider.ProviderServer;

/**
 * Database access interface for accessing PROVIDER_SERVER_RESOURCE table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface ProviderServerDao extends GenericDao<ProviderServer> {
	/**
	 * Finds a list of provider resources of specific iaas provider.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @return
	 *         a list of server resource records
	 */
	List<ProviderServer> findByProviderId(String iaasProviderId);
	
	/**
	 * remove a list of provider resources by specific iaas provider  .
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @return
	 *         whether the remove is success
	 */
	void deleteByProviderId(String iaasProviderId);
	
	/**
	 * Finds a list of provider resources of specific iaas provider with allocated status.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @param isAllocated
	 *            isAllocated status
	 * @return
	 *         a list of server resource records
	 */
	List<ProviderServer> findByProviderIdAndAllocated(String iaasProviderId, String isAllocated);
	
	/**
	 * Finds a list of provider resources of specific iaas provider with allocated status and size.
	 * 
	 * @param iaasProvider
	 *            specific iaas provider
	 * @param isAllocated
	 *            isAllocated status
	* @param size
	 *            server size
	 *@return
	 *         a list of server resource records
	 */
	List<ProviderServer> findByProviderIdAndAllocatedAndSize(String providerId,
			String isAllocated, String size);

	/**
	 * Finds a list of provider resources of specific server informations.
	 * 
	 * @param providerId
	 * @param isAllocated
	 * @param size
	 * @param name
	 * @param osType
	 * @param ipAddress
	 * @param publicIpAddress
	 * @param privateIpAddress
	 * @return
	 */
	List<ProviderServer> findByConditions(String providerId, String isAllocated, String size, String name, String osType, String ipAddress, String publicIpAddress, String privateIpAddress,  String provider);
}
