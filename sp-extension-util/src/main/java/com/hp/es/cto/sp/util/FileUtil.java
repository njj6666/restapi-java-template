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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility class for processing file and path.
 * 
 * @author Dream
 * 
 */
public class FileUtil {

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** URL prefix for loading from the file system: "file:" */
	public static final String FILE_URL_PREFIX = "file:";

	/** URL protocol for a file in the file system: "file" */
	public static final String URL_PROTOCOL_FILE = "file";

	/**
	 * Replaces all back slash with slash in the path string.
	 * Appends one slash to the the path end if there is no slash.
	 * 
	 * @param path
	 *            path to be formalized
	 * @return
	 *         formalized path string
	 */
	public static String formalizePath(String path) {
		return formalizePath(path, true);
	}

	/**
	 * Replaces all back slash with slash in the path string.
	 * If endWithSlash is true, appends one slash to the path end if there is not slash.
	 * If endWithSlash is false, no slash at the end of path.
	 * 
	 * @param path
	 *            path to be formalized
	 * @param endWithSlash
	 *            whether end with a slash
	 * @return
	 *         formalized path string
	 */
	public static String formalizePath(String path, boolean endWithSlash) {
		if (StringUtil.isNullOrEmpty(path)) {
			return path;
		}

		path = path.replaceAll("\\\\", "/");
		String lastChar = path.substring(path.length() - 1, path.length());

		if (lastChar.equals("/") && !endWithSlash) {
			return path.substring(0, path.length() - 1);
		}
		else if (!lastChar.equals("/") && endWithSlash) {
			return path + '/';
		}
		else {
			return path;
		}
	}

	/**
	 * Removes and replaces all escaped character in file name.
	 * 
	 * (If file name contains escaped character, IOException will be thrown when
	 * reading/writing from to the file.)
	 * 
	 * @param fileName
	 *            file name to be checked
	 * @return
	 *         file name without escaped character
	 */
	public static String removeFileNameEscapedChar(String fileName) {
		String result = fileName.replaceAll("\\\\", "_");
		result = result.replaceAll("/", "_");
		result = result.replaceAll(":", "-");
		result = result.replaceAll("\\*", "@");
		result = result.replaceAll("\\?", "@");
		result = result.replaceAll("\"", "'");
		result = result.replaceAll("<", "[");
		result = result.replaceAll(">", "]");
		result = result.replaceAll("|", "!");
		return result;
	}

	/**
	 * Retrieves file primary name from a file name or a full file path.
	 * 
	 * @param fileName
	 *            file name or full file path
	 * @return
	 *         file primary name
	 */
	public static String getFilePrimaryName(String fileName) {
		String result = fileName.replaceAll("\\\\", "/");
		List<String> listFileName = StringUtil.parseStringList(result, "/");
		result = listFileName.get(listFileName.size() - 1);

		if (result.contains(".")) {
			return result.substring(0, result.indexOf('.'));
		}
		else {
			return result;
		}
	}

	/**
	 * Retrieves file extended name from a file name or a full file path.
	 * 
	 * @param fileName
	 *            file name or full file path
	 * @return
	 *         file extended name
	 */
	public static String getFileExtendName(String fileName) {
		String result = fileName.replaceAll("\\\\", "/");
		List<String> listFileName = StringUtil.parseStringList(result, "/");
		result = listFileName.get(listFileName.size() - 1);

		if (result.contains(".")) {
			return result.substring(result.indexOf('.') + 1);
		}
		else {
			return "";
		}
	}

	/**
	 * Writes lines of content to a file, named with fileName located in filePath.
	 * (If the filePath or fileName is not created, this method will create it first.)
	 * 
	 * @param filePath
	 *            file path which file is located
	 * @param fileName
	 *            file name to be written to
	 * @param lines
	 *            lines of content
	 * @param isAppend
	 *            if true, append content to the file
	 *            if false, recreate a file and write lines of content to it
	 */
	public static void writeFile(String filePath, String fileName, List<String> lines, boolean isAppend) {
		String fileFullName = filePath + File.separator + fileName;
		FileWriter fw = null;
		try {
			if (lines == null) {
				return;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			fw = new FileWriter(formalizePath(fileFullName), isAppend);
			for (String line : lines) {
				fw.write(line + "\r\n");
			}
		}
		catch (IOException ex) {
			throw new RuntimeException("Can't write to file:" + fileFullName, ex);
		}
		finally {
			close(fw);
		}
	}

	/**
	 * Read contents of file into a list of string.
	 * Each line is a string in the lit.
	 * 
	 * @param file
	 *            file to read
	 * @return
	 *         a list of file lines
	 */
	public static List<String> readFileToList(File file) {
		List<String> lines = new ArrayList<String>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			for (String record = br.readLine(); record != null; record = br.readLine()) {
				lines.add(record);
			}

			return lines;
		}
		catch (IOException ex) {
			throw new RuntimeException("Can't read file:" + file, ex);
		}
		finally {
			close(fr);
			close(br);
		}
	}

	/**
	 * Read a property file content into a Properties object.
	 * Each property in file is an element of Properties object.
	 * 
	 * @param filePath
	 *            file path which property file is located in
	 * @param fileName
	 *            file name
	 * @return
	 *         Properties object which contains all properties settings in file
	 */
	public static Properties getIniProperties(String filePath, String fileName) {
		Properties pro = new Properties();
		FileReader fReader = null;
		BufferedReader reader = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(filePath + '/' + fileName);
			if (!file.exists()) {
				file.createNewFile();
				return pro;
			}
			fReader = new FileReader(filePath + '/' + fileName);
			reader = new BufferedReader(fReader);

			String info = reader.readLine();
			while (info != null) {
				if (info.length() == 0 || info.substring(0, 1).equals("#") || info.indexOf('.') <= 0) {
					continue;
				}
				else {
					List<String> list = StringUtil.parseStringList(info, "=", true);
					pro.setProperty(list.get(0), list.get(1));
				}
				info = reader.readLine();
			}

			return pro;
		}
		catch (IOException ex) {
			throw new RuntimeException("Can't read INI succussfully.", ex);
		}
		finally {
			close(reader);
			close(fReader);
		}
	}

	/**
	 * Closes an IO object.
	 * 
	 * @param obj
	 *            IO object to be close
	 */
	public static void close(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			}
			catch (IOException ex) {
				throw new RuntimeException("Can't close resource.", ex);
			}
		}
	}

	/**
	 * Resolve the given resource location to a <code>java.io.File</code>, i.e. to a file in the file system.
	 * <p>
	 * Does not check whether the file actually exists; simply returns the File that the given location would correspond
	 * to.
	 * 
	 * @param resourceLocation
	 *            the resource location to resolve: either a "classpath:" pseudo URL, a "file:" URL, or a plain file
	 *            path eg, "classpath:config/log4j.default", "file:/C:/reinier.reg", "C:/reinier.reg"
	 * @return
	 *         a corresponding File object
	 * @throws FileNotFoundException
	 *             if the resource cannot be resolved to a file in the file system
	 */
	public static File getFile(String resourceLocation) throws FileNotFoundException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			String description = "class path resource [" + path + "]";
			URL url = Thread.currentThread().getContextClassLoader().getResource(path);
			return getFile(url, description);
		}
		try {
			// try URL
			return getFile(new URL(resourceLocation), "URL");
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new File(resourceLocation);
		}
	}

	/**
	 * Resolve the given resource URL to a <code>java.io.File</code>, i.e. to a file in the file system.
	 * 
	 * @param resourceUrl
	 *            the resource URL to resolve
	 * @param description
	 *            a description of the original resource that the URL was created for (for example, a class path
	 *            location)
	 * @return
	 *         a corresponding File object
	 * @throws FileNotFoundException
	 *             if the URL cannot be resolved to a file in the file system
	 */
	private static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
			throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: " + resourceUrl);
		}
		try {
			return new File(resourceUrl.toURI().getSchemeSpecificPart());
		}
		catch (URISyntaxException ex) {
			// Fallback for URLs that are not valid URIs (should hardly ever happen).
			return new File(resourceUrl.getFile());
		}
	}

	/**
	 * Resolve the given resource string to a <code>java.io.InputStream</code>. Search from classpath(in jar or outside
	 * of jar) or from filesystem
	 * 
	 * @param resourceLocation
	 *            eg, "classpath:config/log4j.default", "file:///C:/reinier.reg"
	 * @return
	 *         a corresponding InputStream object
	 * @throws IOException
	 */
	public static InputStream getResourceAsStream(String resourceLocation) throws IOException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		}
		try {
			// try URL
			return new URL(resourceLocation).openStream();
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new FileInputStream(resourceLocation);
		}

	}

	public static File getFile(String resourceLocation, boolean allowedcreate) throws IOException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			URL url = Thread.currentThread().getContextClassLoader().getResource("");
			resourceLocation = url.getPath() + path;

		}
		File file = new File(resourceLocation);
		if (allowedcreate && !file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}
