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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for string processing.
 * 
 * @author Dream
 * 
 */
public class StringUtil {
	/**
	 * Checks whether str is null or empty.
	 * (Do trim before checking).
	 * 
	 * @param str
	 *            string to check
	 * @return
	 *         true if str is null or "" after trim.
	 */
	public static boolean isNullOrEmpty(String str) {
		return isNullOrEmpty(str, true);
	}

	/**
	 * Checks whether str is null or empty.
	 * Do trim before checking if doTrim is true.
	 * 
	 * @param str
	 *            string to check
	 * @param doTrim
	 *            whether do trim before checking
	 * @return
	 *         true if str is null or ""
	 */
	public static boolean isNullOrEmpty(String str, boolean doTrim) {
		if (str == null) {
			return true;
		}
		else {
			if (doTrim) {
				return str.trim().equals("");
			}

			return str.equals("");
		}
	}

	/**
	 * Checks whether s is a valid email address or not.
	 * The method checks using a regular expression that validate the email address whether
	 * it contains an '@' signal and necessary characters.
	 * 
	 * @param s
	 *            string to check
	 * @return
	 *         true if s is a valid email address
	 */
	public static boolean isEmail(String s) {
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s + "");
		return m.matches();
	}

	/**
	 * Parses a string delimited by delimiter into a list of string element.
	 * (Do trim for each string element).
	 * (Remove empty string element).
	 * 
	 * For example,
	 * stringList = "a,b,c  , d, , e"
	 * delimiter = ","
	 * returns a list: [a,c,b,c,e]
	 * 
	 * @param stringList
	 *            string to be parsed
	 * @param delimiter
	 *            delimiter to parse
	 * @return
	 *         list of string element
	 */
	public static List<String> parseStringList(String stringList, String delimiter) {
		return parseStringList(stringList, delimiter, true);
	}

	/**
	 * Parses a string delimited by delimiter into a list of string element.
	 * (Do trim for each string element if isTrim is true).
	 * (Remove empty string element).
	 * 
	 * For example,
	 * stringList = "a,b,c  , d, , e"
	 * delimiter = ","
	 * returns a list: [a,c,b,c,e]
	 * 
	 * @param stringList
	 *            string to be parsed
	 * @param delimiter
	 *            delimiter to parse
	 * @param isTrim
	 *            whether do trim for each string element
	 * @return
	 *         list of string element
	 */
	public static List<String> parseStringList(String stringList, String delimiter, boolean isTrim) {
		return parseStringList(stringList, delimiter, isTrim, true);
	}

	/**
	 * Parses a string delimited by delimiter into a list of string element.
	 * (Do trim for each string element if isTrim is true).
	 * (Remove empty string element if isRemoveEmpty is true).
	 * 
	 * For example,
	 * stringList = "a,b,c  , d, , e"
	 * delimiter = ","
	 * returns a list: [a,c,b,c,e]
	 * 
	 * @param stringList
	 *            string to be parsed
	 * @param delimiter
	 *            delimiter to parse
	 * @param isTrim
	 *            whether do trim for each string element
	 * @param isRemoveEmpty
	 *            whether remove empty string element
	 * @return
	 *         list of string element
	 */
	public static List<String> parseStringList(String stringList, String delimiter, boolean isTrim,
			boolean isRemoveEmpty) {
		String temp = stringList;
		List<String> result = new ArrayList<String>();
		if (isNullOrEmpty(temp)) {
			return result;
		}
		if (isNullOrEmpty(delimiter)) {
			result.add(temp);
			return result;
		}

		if (isTrim) {
			while (temp.indexOf(delimiter) != -1) {
				result.add(temp.substring(0, temp.indexOf(delimiter)).trim());
				temp = temp.substring(temp.indexOf(delimiter) + delimiter.length(), temp.length());
			}
			result.add(temp.trim());
		}
		else {
			while (temp.indexOf(delimiter) != -1) {
				result.add(temp.substring(0, temp.indexOf(delimiter)));
				temp = temp.substring(temp.indexOf(delimiter) + delimiter.length(), temp.length());
			}
			result.add(temp);
		}

		if (isRemoveEmpty) {
			for (int i = 0; i < result.size(); i++) {
				if (result.get(i).equals("")) {
					result.remove(i);
					i--;
				}
			}
		}
		return result;
	}

	/**
	 * Parse a string by delimiter into a map.
	 * (Do trim for each element key and value).
	 * 
	 * For example:
	 * stringList = "ip = 192.168.0.1; user = root; password = esit;"
	 * delimiter = ";"
	 * returns a map of [(ip, 192.168.0.1), (user, root), (password, esit)]
	 * 
	 * @param stringList
	 *            string to be parsed
	 * @param delimiter
	 *            delimiter to parse
	 * @return
	 *         a map object of result
	 */
	public static Map<String, String> parseStringListKeyValue(String stringList, String delimiter) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<String> list = parseStringList(stringList, delimiter);
		for (String s : list) {
			if (s.contains("=")) {
				List<String> alItem = parseStringList(s, "=", true, false);
				map.put(alItem.get(0), alItem.get(1));
			}
		}
		return map;
	}

	public static String wrapIntoString(Map<String, String> map, String delimiter) {
		StringBuffer sb = new StringBuffer();

		if (map != null) {
			Iterator<Entry<String, String>> newseted = map.entrySet().iterator();
			while (newseted.hasNext()) {
				Entry<String, String> entry = newseted.next();
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append(delimiter);
			}
		}
		return sb.toString();
	}

	/**
	 * Checks whether str exists in a string list delimited by delimiter.
	 * 
	 * @param str
	 *            str to check
	 * @param list
	 *            string list
	 * @param delimiter
	 *            string list delimiter
	 * @return
	 *         true if str exist in list
	 */
	public static boolean isStringInList(String str, String list, String delimiter) {
		if (isNullOrEmpty(str)) {
			return true;
		}
		if (isNullOrEmpty(list)) {
			return false;
		}

		List<String> listList = parseStringList(list, delimiter, false, false);
		for (int i = 0; i < listList.size(); i++) {
			if (str.equals(listList.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether str is started with start.
	 * 
	 * @param start
	 *            start
	 * @param str
	 *            string to check
	 * @param isIgnoreCase
	 *            whether to ignore letter case
	 * @return
	 *         true if str is started with start
	 */
	public static boolean isStartWith(String start, String str, boolean isIgnoreCase) {
		if (start == null) {
			return false;
		}
		if (str == null) {
			return false;
		}

		if (str.length() < start.length()) {
			return false;
		}
		if (start.length() == 0) {
			return true;
		}

		String temp = str.substring(0, start.length());
		if (isIgnoreCase) {
			return temp.equalsIgnoreCase(start);
		}
		else {
			return temp.equals(start);
		}
	}

	/**
	 * Checks whether str is ended with end.
	 * 
	 * @param end
	 *            end
	 * @param str
	 *            str to check
	 * @param isIgnoreCase
	 *            whether to ignore letter case
	 * @return
	 *         true if str is ended with end
	 */
	public static boolean isEndWith(String end, String str, boolean isIgnoreCase) {
		if (end == null) {
			return false;
		}
		if (str == null) {
			return false;
		}

		if (str.length() < end.length()) {
			return false;
		}
		if (end.length() == 0) {
			return true;
		}

		String temp = str.substring(str.length() - end.length(), str.length());
		if (isIgnoreCase) {
			return temp.equalsIgnoreCase(end);
		}
		else {
			return temp.equals(end);
		}
	}

	/**
	 * Add an string item to a string list, which delimited by delimiter.
	 * 
	 * @param list
	 *            string list
	 * @param item
	 *            string item
	 * @param delimiter
	 *            delimiter
	 * @return
	 *         updated string list after adding
	 */
	public static String addToStringList(String list, String item, String delimiter) {
		if (isNullOrEmpty(list)) {
			return item;
		}
		else {
			return list + delimiter + item;
		}
	}

	/**
	 * Fill specific char in a string until the string reaches certain length.
	 * 
	 * For example, you want to convert "56" to "0056".
	 * Use: fillChar(string, 4, '0', true)
	 * 
	 * @param string
	 *            string to be filled in
	 * @param toLength
	 *            length
	 * @param ch
	 *            char to fill with
	 * @param fillInFrontOrEnd
	 *            fill in the front of string if true
	 *            fill in the end of string if false
	 * @return
	 *         result string after filling
	 */
	public static String fillChar(String string, int toLength, char ch, boolean fillInFrontOrEnd) {
		String result = string;
		while (result.length() < toLength) {
			if (fillInFrontOrEnd) {
				result = ch + result;
			}
			else {
				result = result + ch;
			}
		}
		return result;
	}

	/**
	 * Formats a single word to a format: Xxxxx
	 * First letter is upper case and followed by letters in lower case.
	 * 
	 * @param word
	 *            word to format
	 * @return
	 *         word after formatting
	 */
	public static String formatWord(String word) {
		if (isNullOrEmpty(word)) {
			return word;
		}
		else {
			return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
		}
	}

	/**
	 * Formats a string list of words delimited to a camel forat: XxxxXxxxXxxx or xxxxXxxxXxxx
	 * 
	 * @param words
	 *            string list of words
	 * @param delimiter
	 *            delimiter
	 * @param isHeaderUpperCase
	 *            first letter is in upper case if true
	 *            otherwise first letter is in lower case
	 * @return
	 *         string of formatting result
	 */
	public static String formatWords(String words, String delimiter, boolean isHeaderUpperCase) {
		if (isNullOrEmpty(words)) {
			return words;
		}
		List<String> listWords = parseStringList(words, delimiter);
		return formatWords(listWords, isHeaderUpperCase);
	}

	/**
	 * Formats a list of words delimited to a camel forat: XxxxXxxxXxxx or xxxxXxxxXxxx
	 * 
	 * @param listWords
	 *            list of words
	 * @param isHeaderUpperCase
	 *            first letter is in upper case if true
	 *            otherwise first letter is in lower case
	 * @return
	 *         string of formatting result
	 */
	public static String formatWords(List<String> listWords, boolean isHeaderUpperCase) {
		StringBuffer sb = new StringBuffer();
		for (String word : listWords) {
			sb.append(formatWord(word));
		}

		String result = sb.toString();
		if (!isHeaderUpperCase) {
			result = result.substring(0, 1).toLowerCase() + result.substring(1);
		}
		return result;
	}

	/**
	 * check the string is matched regex or not
	 * 
	 * @param repx
	 *            regex string
	 * @param str
	 *            want to check string
	 * @return
	 *         true or false
	 */
	public static boolean isMatchedRegExp(String repx, String str) {
		if (str != null && "".equals(str)) {
			return str.matches(repx);
		}
		Pattern p = Pattern.compile(repx);
		Matcher m = p.matcher(str);
		String result = "";
		if (m.find()) {
			result = m.group(0);
		}
		return !"".equals(result);
	}
}
