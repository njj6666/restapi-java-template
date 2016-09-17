package com.hp.es.cto.sp.service.context.util;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.hp.es.cto.sp.persistence.jaxb.ContextImport;
import com.hp.es.cto.sp.persistence.jaxb.subscription.SubscriptionReport;
import com.hp.es.cto.sp.persistence.jaxb.subscription.log.SubscriptionLogs;

public class JaxbUtilTest {
	
	@Test
	public void test_unmashall() {
		InputStream is = this.getClass().getResourceAsStream(
				"/contextImport.xml");
		ContextImport ci= (ContextImport)JaxbUtil.unmashall(is, "com.hp.es.cto.sp.persistence.jaxb");
		Assert.assertEquals("Belluci", ci.getContextNodeImport().getName());
	}
	
	@Test
	public void test_mashall() {
		InputStream is = this.getClass().getResourceAsStream(
				"/contextImport.xml");
		ContextImport ci= (ContextImport)JaxbUtil.unmashall(is, "com.hp.es.cto.sp.persistence.jaxb");
		String cistr= JaxbUtil.mashall(ci, "com.hp.es.cto.sp.persistence.jaxb");
	}
	
	
	@Test
	public void test_unmashallSubscription() {
		InputStream is = this.getClass().getResourceAsStream(
				"/subscriptionReport.xml");
		SubscriptionReport sr= (SubscriptionReport)JaxbUtil.unmashall(is, "com.hp.es.cto.sp.persistence.jaxb.subscription");
		Assert.assertEquals(2, sr.getSubscriptionRecord().size());
	}
	
	@Test
	public void test_mashallSubscription() {
		InputStream is = this.getClass().getResourceAsStream(
				"/subscriptionReport.xml");
		SubscriptionReport sr= (SubscriptionReport)JaxbUtil.unmashall(is, "com.hp.es.cto.sp.persistence.jaxb.subscription");
		String cistr= JaxbUtil.mashall(sr, "com.hp.es.cto.sp.persistence.jaxb.subscription");
		System.out.println(cistr);
	}
	
	@Test
	public void test_unmashallSubscriptionLog() {
		InputStream is = this.getClass().getResourceAsStream(
				"/subscriptionLog.xml");
		SubscriptionLogs sr= (SubscriptionLogs)JaxbUtil.unmashall(is, "com.hp.es.cto.sp.persistence.jaxb.subscription.log");
		Assert.assertEquals(0, sr.getSubscriptionLog().size());
	}
	
	@Test
	public void test_mashallSubscriptionLog() {
		InputStream is = this.getClass().getResourceAsStream(
				"/subscriptionLog.xml");
		SubscriptionLogs sr= (SubscriptionLogs)JaxbUtil.unmashall(is, "com.hp.es.cto.sp.persistence.jaxb.subscription.log");
		String cistr= JaxbUtil.mashall(sr, "com.hp.es.cto.sp.persistence.jaxb.subscription.log");
		System.out.println(cistr);
	}
	
	@Test
	public void test_time() {
		String sourceTime = "2014-12-29 00:00:00";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date sourceDate = null;
		try {
			sourceDate = formatter.parse(sourceTime);
			System.out.println(sourceDate.toString());
		}
		catch (ParseException e) {
		}
		
		TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT");
		TimeZone targetTimeZone = Calendar.getInstance().getTimeZone();
		
		Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
		System.out.println(formatter.format(new Date(targetTime)));
	}
	
	
}
