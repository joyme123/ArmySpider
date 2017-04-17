package com.myway5.www.searchEngine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.myway5.www.Monitor.MonitorEngineAgent;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.Urlpool.FileUrlPool;
import com.myway5.www.Urlpool.Remover.BloomFilterDuplicateRemover;

public class SearchSpider  extends AbstSpiderManager{

	public void manage() {
		
	}
	
	public static void main(String[] args){
		//RedisUrlPool urlPool = RedisUrlPool.getInstance("127.0.0.1",6379);
		FileUrlPool urlPool = FileUrlPool.getInstance();
		urlPool.setPath("/home/jiang/spiderFile");
		urlPool.setDuplicateChecker(new BloomFilterDuplicateRemover(8000000)); 	//设置去重类为bloomFilter,8000000的预计插入量
		Date date = Calendar.getInstance().getTime();
		long start = Calendar.getInstance().getTimeInMillis();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		
		new MonitorEngineAgent(urlPool).start();
		System.out.println("start");
		FirstFilter firstFilter = new FirstFilter();
		
		
		ProcessSpider processSpider = new ProcessSpider();
		processSpider.setFilterSpider(firstFilter);
		

		
		SearchSpider spiderManager = new SearchSpider();
		spiderManager.setProcessSpider(processSpider)
					.setStartUrl("http://www.cnblogs.com")
					 .setUrlPool(urlPool)
		    		 .thread(1)
		    		 .run();
		Date date2 = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long stop = Calendar.getInstance().getTimeInMillis();
		System.out.println("开始时间:" + format.format(date));
		System.out.println("终止时间:" + format.format(date2));
		System.out.println("总耗时:" + (stop - start));
		
	}
}
