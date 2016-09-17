package com.hp.es.cto.sp.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil {
	private ClassPathXmlApplicationContext ctx;

	public SpringUtil(String springXml) {
		ctx = new ClassPathXmlApplicationContext(new String[] { springXml });
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name) {
		Object bean = ctx.getBean(name);
		return (T) bean;
	}
}
