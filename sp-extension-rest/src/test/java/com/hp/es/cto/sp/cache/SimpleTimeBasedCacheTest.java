package com.hp.es.cto.sp.cache;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 
 * @author tanxu
 * @date Jan 12, 2012
 * @since
 */
public class SimpleTimeBasedCacheTest {

	@Test
	public void testStale() throws Exception {
		int milliseconds = 2000;
		SimpleTimeBasedCache cache = new SimpleTimeBasedCache(milliseconds);

		cache.put("fruit", "apple");
		assertTrue("Put failed.", cache.containsKey("fruit"));
		Thread.sleep(milliseconds + 1000);

		Object obj = cache.get("fruit");
		assertNull(obj);
		assertFalse(cache.containsKey("obj"));
	}

	@Test
	public void testConcurrentAccess() throws Exception {
		int milliseconds = 1;	// use very short expiration time for concurrent test
		final SimpleTimeBasedCache cache = new SimpleTimeBasedCache();
		cache.setTimeLimit(milliseconds);
		cache.setCacheSource(new CacheSource() {
			public Object getForCache(Object key) throws NoValueAvailableException, Exception {
				if ("fruit".equals(key))
					return "apple";
				return null;
			}
		});

		assertNotNull(cache.get("fruit"));

		Thread.sleep(10);

		int threadCount = 1000;
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) {
			threadPool.execute(new Runnable() {
				public void run() {
					try {
						assertNotNull(cache.get("fruit"));
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}
}
