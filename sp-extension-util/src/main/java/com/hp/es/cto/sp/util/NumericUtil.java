/*************************************************************************     
 *******************                                                           
 * HP CONFIDENTIAL *                                                           
 *******************                                                           
 *                                                                             
 * [2007] - [2013] Hewlett Packard Corporation                                 
 *                                                                             
 * All Rights Reserved.                                                        
 *                                                                             
 * NOTICE:                                                                     
 *     All information contained herein is, and remains the property of        
 * Hewlett Packard Corporation and its suppliers, if any.  The intellectual    
 * and technical concepts contained herein are proprietary and confidential to 
 * Hewlett Packard Corporation and its suppliers and may be covered by U.S. and
 * Foreign Patents, patents in process, and are protected by trade secret or   
 * copyright law. Dissemination of this information or reproduction of this    
 * material is strictly forbidden unless prior written permission is obtained  
 * from Hewlett Packard Corporation.                                           
 *                                                                             
 */
package com.hp.es.cto.sp.util;

/**
 * Utility class for processing and parsing integer, float number, etc.
 * 
 * @author Dream
 * 
 */
public class NumericUtil {
	/**
	 * Checks whether object is an integer or not.
	 * If object is integer primitive type, or an integer in string, return true.
	 * 
	 * @param object
	 *            object to parse
	 * @return
	 *         true if object is an integer
	 */
	public static boolean isInteger(Object object) {
		if (object == null) {
			return false;
		}
		else {
			String str = object + "";
			try {
				Integer.parseInt(str);
				return true;
			}
			catch (NumberFormatException ex) {
				return false;
			}
		}
	}

	/**
	 * Checks whether object is a long integer or not.
	 * If object is long primitive type, or a long integer in string, return true.
	 * 
	 * @param object
	 *            object to parse
	 * @return
	 *         true if object is a long integer
	 */
	public static boolean isLong(Object object) {
		if (object == null) {
			return false;
		}
		else {
			String str = object + "";
			try {
				Long.parseLong(str);
				return true;
			}
			catch (NumberFormatException ex) {
				return false;
			}
		}
	}

	/**
	 * Checks whether object is a double or not.
	 * If object is double primitive type, or a double in string, return true.
	 * 
	 * @param object
	 *            object to parse
	 * @return
	 *         true if object is a double
	 */
	public static boolean isDouble(Object object) {
		if (object == null) {
			return false;
		}
		else {
			String str = object + "";
			try {
				Double.parseDouble(str);
				return true;
			}
			catch (NumberFormatException ex) {
				return false;
			}
		}
	}

	/**
	 * Parses and converts object to an integer.
	 * Throw java.lang.NumberFormatException if conversion fails.
	 * 
	 * @param object
	 *            object to convert
	 * @return
	 *         result of conversion
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable integer.
	 */
	public static Integer getInteger(Object object) {
		if (object instanceof Integer) {
			return (Integer) object;
		}
		String str = object + "";
		if (object == null || StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		else {
			return Integer.parseInt(str);
		}
	}

	/**
	 * Parses and converts object to an integer.
	 * Return defaultValue if conversion fails.
	 * 
	 * @param object
	 *            object to convert
	 * @param defaultValue
	 *            default value
	 * @return
	 *         result of conversion
	 */
	public static int getInteger(Object object, int defaultValue) {
		if (!isInteger(object)) {
			return defaultValue;
		}
		else {
			return getInteger(object).intValue();
		}
	}

	/**
	 * Parses and converts object to a long integer.
	 * Throw java.lang.NumberFormatException if conversion fails.
	 * 
	 * @param object
	 *            object to convert
	 * @return
	 *         result of conversion
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable long integer.
	 */
	public static Long getLong(Object object) {
		if (object instanceof Long) {
			return (Long) object;
		}
		String str = object + "";
		if (object == null || StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		else {
			return Long.parseLong(str);
		}
	}

	/**
	 * Parses and converts object to a long integer.
	 * Return defaultValue if conversion fails.
	 * 
	 * @param object
	 *            object to convert
	 * @param defaultValue
	 *            default value
	 * @return
	 *         result of conversion
	 */
	public static long getLong(Object object, long defaultValue) {
		if (!isLong(object)) {
			return defaultValue;
		}
		else {
			return getLong(object).longValue();
		}
	}

	/**
	 * Parses and converts object to a double.
	 * Throw java.lang.NumberFormatException if conversion fails.
	 * 
	 * @param object
	 *            object to convert
	 * @return
	 *         result of conversion
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable number.
	 */
	public static Double getDouble(Object object) {
		if (object instanceof Double) {
			return (Double) object;
		}
		String str = object + "";
		if (object == null || StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		else {
			return Double.parseDouble(str);
		}
	}

	/**
	 * Parses and converts object to a double.
	 * Return defaultValue if conversion fails.
	 * 
	 * @param object
	 *            object to convert
	 * @param defaultValue
	 *            default value
	 * @return
	 *         result of conversion
	 */
	public static double getDouble(Object object, double defaultValue) {
		if (!isDouble(object)) {
			return defaultValue;
		}
		else {
			return getDouble(object).doubleValue();
		}
	}

	/**
	 * Retrieves the minimum integer in the list.
	 * 
	 * @param list
	 *            list of integer
	 * @return
	 *         the minimum integer
	 */
	public static int min(int... list) {
		int min = list[0];
		for (int i : list) {
			if (i < min) {
				min = i;
			}
		}
		return min;
	}

	/**
	 * Retrieves the minimum long integer in the list
	 * 
	 * @param list
	 *            list of long integer
	 * @return
	 *         the minimum long integer
	 */
	public static long min(long... list) {
		long min = list[0];
		for (long i : list) {
			if (i < min) {
				min = i;
			}
		}
		return min;
	}

	/**
	 * Retrieves the minimum double in the list
	 * 
	 * @param list
	 *            list of double
	 * @return
	 *         the minimum double
	 */
	public static double min(double... list) {
		double min = list[0];
		for (double i : list) {
			if (i < min) {
				min = i;
			}
		}
		return min;
	}

	/**
	 * Retrieves the maximum integer in the list
	 * 
	 * @param list
	 *            list of integer
	 * @return
	 *         the maximum integer
	 */
	public static int max(int... list) {
		int max = list[0];
		for (int i : list) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}

	/**
	 * Retrieves the maximum long integer in the list.
	 * 
	 * @param list
	 *            list of long integer
	 * @return
	 *         the maximum long integer
	 */
	public static long max(long... list) {
		long max = list[0];
		for (long i : list) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}

	/**
	 * Retrieves the maximum double in the list.
	 * 
	 * @param list
	 *            list of double
	 * @return
	 *         the maximum double
	 */
	public static double max(double... list) {
		double max = list[0];
		for (double i : list) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}
}
