/* $Id: CacheSource.java,v 1.1 2004/05/21 14:19:04 qmay Exp $ */
package com.hp.es.cto.sp.cache;

/**
 * An object implementing the CacheSource interface can provide values to a
 * cache when required.
 * 
 * @version $Revision: 1.1 $
 * @author Quintin May
 */
public interface CacheSource
{
	/**
	 * Returns the value to associate with the specified key in a cache.
	 * 
	 * @param key key whose value is to be mapped into the cache.
	 * @return the value to which the cache will map the specified key, or
	 *         <code>null</code> if there is no mapping for this key.
	 * @throws NoValueAvailableException if there is no value for the specified
	 *             key.
	 * @throws Exception if there is an error accessing or creating the value.
	 */
	public Object getForCache(Object key) throws NoValueAvailableException, Exception;
}