/* $Id: NoValueAvailableException.java,v 1.1 2004/05/21 14:19:04 qmay Exp $ */

package com.hp.es.cto.sp.cache;

/**
 * The exception thrown by a cache source in order to notify the cache that no value is available for the specified key.
 * 
 * @version $Revision: 1.1 $
 * @author Quintin May
 */
public class NoValueAvailableException extends Exception
{
	private Object key;
	
	/**
	 * Create a no value available exception for the specified key.
	 * @param key the key for which no value is available.
	 */
	public NoValueAvailableException(Object key)
	{
		super("No value available for " + key + ".");
	}
	
	/**
	 * Return the key for which no value is available.
	 * @return the key.
	 */
	public Object getKey()
	{
		return key;
	}
}
