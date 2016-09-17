package com.hp.es.cto.sp.util;

import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilTest {
	private Object obj1 = "true";
	private Object obj2 = false;
	private Object obj3 = "False";
	private Object obj4 = "TRUE";
	private Object obj5 = null;
	private Object obj6 = "not boolean";

	@Test
	public void test() {
		// Assert for isBoolean(Object)
		Assert.assertTrue(BooleanUtil.isBoolean(obj1));
		Assert.assertTrue(BooleanUtil.isBoolean(obj2));
		Assert.assertTrue(BooleanUtil.isBoolean(obj3));
		Assert.assertTrue(BooleanUtil.isBoolean(obj4));
		Assert.assertTrue(!BooleanUtil.isBoolean(obj5));
		Assert.assertTrue(!BooleanUtil.isBoolean(obj6));

		// Assert for getBoolean(Object)
		Assert.assertTrue(BooleanUtil.getBoolean(obj1));
		Assert.assertTrue(!BooleanUtil.getBoolean(obj2));
		Assert.assertTrue(!BooleanUtil.getBoolean(obj3));
		Assert.assertTrue(BooleanUtil.getBoolean(obj4));
		try {
			BooleanUtil.getBoolean(obj5);
			Assert.fail("should throw exception");
		}
		catch (RuntimeException re) {
		}
		try {
			Assert.assertNull(BooleanUtil.getBoolean(obj6));
			Assert.fail("should throw exception");
		}
		catch (RuntimeException re) {
		}

		// Assert for getBoolean(Object, boolean)
		Assert.assertTrue(BooleanUtil.getBoolean(obj1, false));
		Assert.assertTrue(!BooleanUtil.getBoolean(obj2, false));
		Assert.assertTrue(!BooleanUtil.getBoolean(obj3, true));
		Assert.assertTrue(BooleanUtil.getBoolean(obj4, true));
		Assert.assertTrue(BooleanUtil.getBoolean(obj5, true));
		Assert.assertTrue(BooleanUtil.getBoolean(obj6, true));
	}
}
