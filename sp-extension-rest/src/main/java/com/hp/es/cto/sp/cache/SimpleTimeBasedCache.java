package com.hp.es.cto.sp.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * It's a simple time-based caching implementation, 
 * will invalidates entries in the cache based on absolute time periods <code>timeLimit</code>. <br/>
 * It add Items to the cache, and they remain in the cache for a specific amount of time. <br/>
 * Entries are removed from the cache when {@link #get(Object, Object)}is 
 * It's fast but not adaptive for access patterns
 * 
 * @author tanxu
 * @date Jan 16, 2012
 * @since SSO 11.0
 */
public class SimpleTimeBasedCache implements TimeLimitedCache {

	private static final Logger log = Logger.getLogger(SimpleTimeBasedCache.class.getName());

	private Map<Object, CacheEntry> cache;
	private CacheSource cacheSource;
	long timeLimit;

	public SimpleTimeBasedCache() {
		cache = new ConcurrentHashMap<Object, CacheEntry>();
	}

	/**
	 * @param timeLimit the absolute time periods in milliseconds
	 */
	public SimpleTimeBasedCache(long timeLimit) {
		this();
		this.timeLimit = timeLimit;
	}

	public Object put(Object key, Object value) {
		CacheEntry oldValue = cache.put(key, new CacheEntry(value));
		if (oldValue == null)
			return null;
		return oldValue.value;
	}

	public Object get(Object key) throws Exception {
		long startTime = System.currentTimeMillis();

		CacheEntry item = getCacheEntry(key);
		if (item == null) {
			if (cacheSource != null) {
				try {
					Object value = cacheSource.getForCache(key);
					item = new CacheEntry(value);
					cache.put(key, item);
				}
				catch (NoValueAvailableException ex) {
					log.warning("No Value Available for key [" + key + "] from cacheSource [" + cacheSource + "]");
				}
			}
		}

		if (item == null)
			return null;
		return item.value;
	}

	private CacheEntry getCacheEntry(Object key) {
		CacheEntry item = cache.get(key);
		if (item == null)
			return null;

		if (System.currentTimeMillis() - item.createTime > timeLimit) {
			cache.remove(key);
			return null;
		}
		return item;
	}

	public Object remove(Object key) {
		Object value = cache.remove(key).value;
		return value;
	}

	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	public void setCacheSource(CacheSource cacheSource) {
		this.cacheSource = cacheSource;
	}

	/**
	 * @param timeLimit the absolute time periods in milliseconds
	 */
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * @return the absolute time periods in milliseconds
	 */
	public long getTimeLimit() {
		return timeLimit;
	}

}
