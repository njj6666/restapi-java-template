package com.hp.es.cto.sp.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

public class CSAClient {

	private static final Logger logger = LoggerFactory.getLogger(CSAClient.class);

	/**
	 * Invoke CSA REST API
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public String invoke(String path, String format) throws Exception {
		String objXML = "";
		while (true) {
			Properties prop = new Properties();
			ClassLoader loader = CSAClient.class.getClassLoader();
			InputStream stream = loader.getResourceAsStream("/security.properties");
			prop.load(stream);
			String rest_base_url = prop.getProperty("csa_rest_base_url");
			String rest_admin = prop.getProperty("csa_rest_admin");
			String rest_password = prop.getProperty("csa_rest_password");

			String url = rest_base_url + path;
			objXML = executeHttpGet(url, rest_admin, rest_password,format);
			logger.debug(objXML);
			if (!StringUtils.isEmpty(objXML) && !objXML.contains("OptimisticLockingFailureException")) {
				break;
			}
			Thread.sleep(500);
		}

		return objXML;
	}

	private String executeHttpGet(String url, String username, String password, String format) throws Exception {
		HttpClient httpClient = InsecureHttpClient.createHttpClient();
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Accept", format);
			if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
				String credential = username + ":" + password;
				byte[] encoded = Base64.encode(credential.getBytes("UTF-8"));
				String token = new String(encoded, "UTF-8");
				httpGet.setHeader("Authorization", "Basic " + token);
			}

			HttpResponse httpResponse = httpClient.execute(httpGet);
			String buffer = "";
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer);
			}
			return sb.toString();
		}
		finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

}
