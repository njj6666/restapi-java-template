package com.hp.es.cto.sp.util;

import org.junit.Assert;
import org.junit.Test;

public class NumericUtilTest {
	private Object obj1 = "100";
	private Object obj2 = 1234;
	private Object obj3 = "not a number";
	private Object obj4 = null;
	private Object obj5 = "56.987";

	@Test
	public void test_integer() {
		Assert.assertTrue(NumericUtil.isInteger(obj1));
		Assert.assertTrue(NumericUtil.isInteger(obj2));
		Assert.assertTrue(!NumericUtil.isInteger(obj3));
		Assert.assertTrue(!NumericUtil.isInteger(obj4));
		Assert.assertTrue(!NumericUtil.isInteger(obj5));

		Assert.assertEquals(100, NumericUtil.getInteger(obj1).intValue());
		Assert.assertEquals(1234, NumericUtil.getInteger(obj2).intValue());
		Assert.assertEquals(100, NumericUtil.getInteger(obj3, 100));
		Assert.assertEquals(100, NumericUtil.getInteger(obj4, 100));
		Assert.assertEquals(100, NumericUtil.getInteger(obj5, 100));
	}

	@Test
	public void test_long() {
		Assert.assertTrue(NumericUtil.isLong(obj1));
		Assert.assertTrue(NumericUtil.isLong(obj2));
		Assert.assertTrue(!NumericUtil.isLong(obj3));
		Assert.assertTrue(!NumericUtil.isLong(obj4));
		Assert.assertTrue(!NumericUtil.isLong(obj5));

		Assert.assertEquals(100, NumericUtil.getLong(obj1).longValue());
		Assert.assertEquals(1234, NumericUtil.getLong(obj2).longValue());
		Assert.assertEquals(100, NumericUtil.getLong(obj3, 100));
		Assert.assertEquals(100, NumericUtil.getLong(obj4, 100));
		Assert.assertEquals(100, NumericUtil.getLong(obj5, 100));
	}

	@Test
	public void test_double() {
		Assert.assertTrue(NumericUtil.isDouble(obj1));
		Assert.assertTrue(NumericUtil.isDouble(obj2));
		Assert.assertTrue(!NumericUtil.isDouble(obj3));
		Assert.assertTrue(!NumericUtil.isDouble(obj4));
		Assert.assertTrue(NumericUtil.isDouble(obj5));

		Assert.assertTrue(100.0 == NumericUtil.getDouble(obj1).doubleValue());
		Assert.assertTrue(1234.0 == NumericUtil.getDouble(obj2).doubleValue());
		Assert.assertTrue(100.0 == NumericUtil.getDouble(obj3, 100));
		Assert.assertTrue(100.0 == NumericUtil.getDouble(obj4, 100));
		Assert.assertTrue(56.987 == NumericUtil.getDouble(obj5, 100));
	}
}
