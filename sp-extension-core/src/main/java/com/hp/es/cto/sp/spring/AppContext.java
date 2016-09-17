package com.hp.es.cto.sp.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * This class provides application-wide access to the Spring ApplicationContext. The ApplicationContext is injected by the class "ApplicationContextProvider".
 * 
 * @author
 */
public class AppContext {

	private static ApplicationContext ctx;

	/**
	 * Injected from the class "ApplicationContextProvider" which is automatically loaded during Spring-Initialization.
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
		ctx = applicationContext;
	}

	/**
	 * Get access to the Spring ApplicationContext from everywhere in your Application.
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
	
	/**
	 * Get data source driver class
	 * 
	 * @return
	 */
	public static String getDriverManagerDataSourceClassUrl() {
		DriverManagerDataSource ds = (DriverManagerDataSource) ctx.getBean("spextDS");
		String driverClass = ds.getUrl();
		return driverClass;
	}

}
