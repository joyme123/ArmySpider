package com.myway5.www.client;

import com.mway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.Monitor.MonitorEngineAgent;
import com.myway5.www.Spider.ProcessSpider;

public class SpiderManager extends AbstSpiderManager{

	public void manage() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		
		new MonitorEngineAgent().start();
		System.out.println("start");
		FirstFilter firstFilter = new FirstFilter();
		SecondFilter secondFilter = new SecondFilter();
		firstFilter.setNextFilter(secondFilter);
		
		
		ProcessSpider processSpider = new ProcessSpider();
		processSpider.setTargetUrl("http://wallpaper\\.pconline\\.com\\.cn/pic/\\d+.*\\.html");
		processSpider.setLimitation("http://wallpaper\\.pconline\\.com\\.cn/.*");
		processSpider.setFilterSpider(firstFilter);
		

		
		SpiderManager spiderManager = new SpiderManager();
		spiderManager.setTargetUrl("http://wallpaper.pconline.com.cn")
		    		 .setProcessSpider(processSpider)
		    		 .thread(5)
		    		 .run();
	}

}
