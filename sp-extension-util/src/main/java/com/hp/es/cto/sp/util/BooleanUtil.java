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
 * Utility class for processing and converting Boolean/boolean type.
 * 
 * @author Dream
 * 
 */
public class BooleanUtil {
	/**
	 * Checks whether object is a boolean type or object is boolean string.
	 * 
	 * @param object
	 *            object to parse
	 * @return
	 *         true if object is a boolean type or "true" "false" (case insensitive)
	 */
	public static boolean isBoolean(Object object) {
		String str = object + "";
		return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
	}

	/**
	 * Parses and converts object to Boolean type.
	 * (case insensitive)
	 * If object is true or "true", the result is true.
	 * If object is false or "false", the result is false.
	 * Otherwise, throw runtime exception.
	 * 
	 * @param object
	 *            object to convert
	 * @return
	 *         result of converting
	 */
	public static boolean getBoolean(Object object) {
		String str = object + "";
		if (object == null || StringUtil.isNullOrEmpty(str)) {
			throw new RuntimeException("object is null.");
		}
		else {
			if (str.equalsIgnoreCase("true")) {
				return true;
			}
			if (str.equalsIgnoreCase("false")) {
				return false;
			}

			throw new RuntimeException("object is not a boolean value.");
		}
	}

	/**
	 * Parses and converts object to Boolean type.
	 * (case insensitive)
	 * If object is true or "true", the result is true.
	 * If object is false or "false", the result is false.
	 * Otherwise, return defaultValue.
	 * 
	 * @param object
	 *            object to convert
	 * @param defaultValue
	 *            default value
	 * @return
	 *         result of converting
	 */
	public static boolean getBoolean(Object object, boolean defaultValue) {
		try {
			return getBoolean(object);
		}
		catch (RuntimeException e) {
			return defaultValue;
		}
	}
}
