package com.myway5.www.RedisPersistenceClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.myway5.www.Monitor.MonitorEngineAgent;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.Urlpool.FileUrlPool;
import com.myway5.www.Urlpool.RedisUrlPool;
import com.myway5.www.client.FirstFilter;
import com.myway5.www.client.SecondFilter;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;

public class SpiderManager  extends AbstSpiderManager{

	public void manage() {
		
	}
	
	public static void main(String[] args){
		RedisUrlPool urlPool = RedisUrlPool.getInstance("127.0.0.1",6379);
		Date date = Calendar.getInstance().getTime();
		long start = Calendar.getInstance().getTimeInMillis();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		
		new MonitorEngineAgent(urlPool).start();
		System.out.println("start");
		FirstFilter firstFilter = new FirstFilter();
		SecondFilter secondFilter = new SecondFilter();
		firstFilter.setNextFilter(secondFilter);
		
		
		ProcessSpider processSpider = new ProcessSpider();
		processSpider.setTargetUrl("http://wallpaper\\.pconline\\.com\\.cn/pic/\\d+.*\\.html");
		processSpider.setLimitation("http://wallpaper\\.pconline\\.com\\.cn/.*");
		processSpider.setFilterSpider(firstFilter);
		

		
		SpiderManager spiderManager = new SpiderManager();
		spiderManager.setStartUrl("http://wallpaper.pconline.com.cn")
					 .setUrlPool(urlPool)
		    		 .setProcessSpider(processSpider)
		    		 .thread(5)
		    		 .run();
		Date date2 = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long stop = Calendar.getInstance().getTimeInMillis();
		System.out.println("开始时间:" + format.format(date));
		System.out.println("终止时间:" + format.format(date2));
		System.out.println("总耗时:" + (stop - start));
		
	}
}
