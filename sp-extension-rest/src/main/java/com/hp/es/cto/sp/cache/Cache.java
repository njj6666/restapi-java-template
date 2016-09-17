/* $Id: Cache.java,v 1.1 2004/05/21 14:19:04 qmay Exp $ */
package com.hp.es.cto.sp.cache;

/**
 * An object that maps keys to values. A cache cannot contain duplicate keys;
 * each key can map to at most one value. The objects are presumably expensive
 * to access and therefore, once accessed are stored in the cached for faster
 * future access.
 * 
 * @version $Revision: 1.1 $
 * @author Quintin May
 */
public interface Cache
{
	/**
	 * Associates the specified value with the specified key in this cache. If
	 * the cache previously contained a mapping for this key, the old value is
	 * replaced by the specified value.
	 * 
	 * @param key key with which the specified value is to be associated.
	 * @param value to be associated with the specified key.
	 * @return previous value associated with specified key, or <code>null</code>
	 *         if there was no mapping for key. A <code>null</code> return
	 *         can also indicate that the cache previously associated <code>null</code>
	 *         with the specified key, if the implementation supports <code>null</code>
	 *         values.
	 */
	public Object put(Object key, Object value);

	/**
	 * Returns the value to which this cache maps the specified key. Returns
	 * <code>null</code> if the cache contains no mapping for this key and if
	 * there is no source defined for the cache. A return value of <code>null</code>
	 * does not necessarily indicate that the cache contains no mapping for the
	 * key; it's also possible that the cache explicitly maps the key to <code>null</code>.
	 * The containsKey operation may be used to distinguish these two cases.
	 * 
	 * @param key key whose associated value is to be returned.
	 * @return the value to which this cache maps the specified key, or <code>null</code>
	 *         if the cache contains no mapping for this key.
	 * @throws Exception if a source is configured and it cannot retrieve the
	 *             requested value;
	 */
	public Object get(Object key) throws Exception;

	/**
	 * Removes the mapping for this key from this cache if it is present
	 * 
	 * @param key key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or <code>null</code>
	 *         if there was no mapping for key.
	 */
	public Object remove(Object key);

	/**
	 * Returns true if this cache contains a mapping for the specified key.
	 * 
	 * @param key key whose presence in this cache is to be tested.
	 * @return true if the cache contains a mapping for the specified key.
	 */
	public boolean containsKey(Object key);

	/**
	 * If a cache has a cache source defined, then the <code>get</code>
	 * method will access the cache source if the cache does not contain a
	 * mapping for the requested key. If the source returns a value, the cache
	 * will add a mapping for it, otherwise if the source throws an exception,
	 * the cache will not add a mapping for the key.
	 * 
	 * @param source the source from which to retrieve values.
	 */
	public void setCacheSource(CacheSource source);
}
