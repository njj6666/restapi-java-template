package com.hp.es.cto.sp.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {
	@Test
	public void test_isNullOrEmpty() {
		String s1 = null;
		String s2 = "";
		String s3 = "  \t  ";
		String s4 = "not empty";

		Assert.assertTrue(StringUtil.isNullOrEmpty(s1));
		Assert.assertTrue(StringUtil.isNullOrEmpty(s2));
		Assert.assertTrue(StringUtil.isNullOrEmpty(s3));
		Assert.assertTrue(!StringUtil.isNullOrEmpty(s4));

		Assert.assertTrue(StringUtil.isNullOrEmpty(s1, false));
		Assert.assertTrue(StringUtil.isNullOrEmpty(s2, false));
		Assert.assertTrue(!StringUtil.isNullOrEmpty(s3, false));
		Assert.assertTrue(!StringUtil.isNullOrEmpty(s4, false));
	}

	@Test
	public void test_isEmail() {
		String s1 = null;
		String s2 = "  \t  ";
		String s3 = "aa@cc.";
		String s4 = "@cc.com";
		String s5 = "aa@cc.com";

		Assert.assertTrue(!StringUtil.isEmail(s1));
		Assert.assertTrue(!StringUtil.isEmail(s2));
		Assert.assertTrue(!StringUtil.isEmail(s3));
		Assert.assertTrue(!StringUtil.isEmail(s4));
		Assert.assertTrue(StringUtil.isEmail(s5));
	}

	@Test
	public void test_parseStringList() {
		String list = "aa;  bb;cc \t ; ;;dd";
		String delimiter = ";";

		List<String> resultList = StringUtil.parseStringList(list, delimiter);
		Assert.assertEquals(4, resultList.size());
		Assert.assertEquals("aa", resultList.get(0));
		Assert.assertEquals("bb", resultList.get(1));
		Assert.assertEquals("cc", resultList.get(2));
		Assert.assertEquals("dd", resultList.get(3));

		resultList = StringUtil.parseStringList(list, delimiter, false);
		Assert.assertEquals(5, resultList.size());
		Assert.assertEquals("aa", resultList.get(0));
		Assert.assertEquals("  bb", resultList.get(1));
		Assert.assertEquals("cc \t ", resultList.get(2));
		Assert.assertEquals(" ", resultList.get(3));
		Assert.assertEquals("dd", resultList.get(4));

		resultList = StringUtil.parseStringList(list, delimiter, true, false);
		Assert.assertEquals(6, resultList.size());
		Assert.assertEquals("aa", resultList.get(0));
		Assert.assertEquals("bb", resultList.get(1));
		Assert.assertEquals("cc", resultList.get(2));
		Assert.assertEquals("", resultList.get(3));
		Assert.assertEquals("", resultList.get(4));
		Assert.assertEquals("dd", resultList.get(5));

		resultList = StringUtil.parseStringList(list, delimiter, false, false);
		Assert.assertEquals(6, resultList.size());
		Assert.assertEquals("aa", resultList.get(0));
		Assert.assertEquals("  bb", resultList.get(1));
		Assert.assertEquals("cc \t ", resultList.get(2));
		Assert.assertEquals(" ", resultList.get(3));
		Assert.assertEquals("", resultList.get(4));
		Assert.assertEquals("dd", resultList.get(5));
	}

	@Test
	public void test_isStringInList() {
		String list = "aa;  bb;cc \t ; ;;dd";
		String delimiter = ";";
		String s1 = null;
		String s2 = " ";
		String s3 = "aa";
		String s4 = "bb";

		Assert.assertTrue(StringUtil.isStringInList(s1, list, delimiter));
		Assert.assertTrue(StringUtil.isStringInList(s2, list, delimiter));
		Assert.assertTrue(StringUtil.isStringInList(s3, list, delimiter));
		Assert.assertTrue(!StringUtil.isStringInList(s4, list, delimiter));
	}

	@Test
	public void test_isStartWith() {
		String str = "iAmWordsForTesting";
		String s1 = null;
		String s2 = "";
		String s3 = "iAm";
		String s4 = "IAM";

		Assert.assertTrue(!StringUtil.isStartWith(s1, str, true));
		Assert.assertTrue(StringUtil.isStartWith(s2, str, true));
		Assert.assertTrue(StringUtil.isStartWith(s3, str, true));
		Assert.assertTrue(StringUtil.isStartWith(s4, str, true));

		Assert.assertTrue(!StringUtil.isStartWith(s1, str, false));
		Assert.assertTrue(StringUtil.isStartWith(s2, str, false));
		Assert.assertTrue(StringUtil.isStartWith(s3, str, false));
		Assert.assertTrue(!StringUtil.isStartWith(s4, str, false));
	}

	@Test
	public void test_isEndWith() {
		String str = "iAmWordsForTesting";
		String s1 = null;
		String s2 = "";
		String s3 = "ing";
		String s4 = "ING";

		Assert.assertTrue(!StringUtil.isEndWith(s1, str, true));
		Assert.assertTrue(StringUtil.isEndWith(s2, str, true));
		Assert.assertTrue(StringUtil.isEndWith(s3, str, true));
		Assert.assertTrue(StringUtil.isEndWith(s4, str, true));

		Assert.assertTrue(!StringUtil.isEndWith(s1, str, false));
		Assert.assertTrue(StringUtil.isEndWith(s2, str, false));
		Assert.assertTrue(StringUtil.isEndWith(s3, str, false));
		Assert.assertTrue(!StringUtil.isEndWith(s4, str, false));
	}

	@Test
	public void test_addToStringList() {
		String delimiter = ";";
		String list = "";
		String[] items = new String[] { "aa", "bb", "cc" };

		for (String item : items) {
			list = StringUtil.addToStringList(list, item, delimiter);
		}

		Assert.assertEquals("aa;bb;cc", list);
	}

	@Test
	public void test_fillChar() {
		char c = '0';
		String s = "91";
		Assert.assertEquals("0091", StringUtil.fillChar(s, 4, c, true));
		Assert.assertEquals("9100", StringUtil.fillChar(s, 4, c, false));
	}

	@Test
	public void test_formatWords() {
		String s = "test_database_namespace";
		String delimiter = "_";

		Assert.assertEquals("TestDatabaseNamespace", StringUtil.formatWords(s, delimiter, true));
		Assert.assertEquals("testDatabaseNamespace", StringUtil.formatWords(s, delimiter, false));
	}
	
}
