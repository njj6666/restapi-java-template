package com.hp.es.cto.sp.rest;

import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * DOCME
 * 
 * @author tanxu
 */
public class SpringPOJOResourceFactory extends POJOResourceFactory {
    private ClassPathXmlApplicationContext ctx;

	public SpringPOJOResourceFactory(Class<?> scannableClass) {
		super(scannableClass);
		ctx = new ClassPathXmlApplicationContext("testbean.xml");
	}

	@Override
	public Object createResource(HttpRequest request, HttpResponse response, ResteasyProviderFactory factory) {
		Object pojo = super.createResource(request, response, factory);
		AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
		bpp.setBeanFactory(ctx.getAutowireCapableBeanFactory());
		bpp.processInjection(pojo);
		return pojo;
	}
	
	public ApplicationContext getAppContext(){
		return ctx;
	}
	
}
