package com.hp.es.cto.sp.cache;

/**
 * DOCME
 * 
 * @author tanxu
 * @date Jan 16, 2012
 * @since
 */
public class CacheEntry {

	final Object value;
	long createTime;

	CacheEntry(Object value) {
		this.value = value;
		this.createTime = System.currentTimeMillis();
	}
}
