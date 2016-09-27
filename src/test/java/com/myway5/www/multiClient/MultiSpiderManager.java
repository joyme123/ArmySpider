package com.myway5.www.multiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.myway5.www.Pool.MultiFilterPagePool;
import com.myway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.ThreadPool.FilterSpiderThreadPool;
import com.myway5.www.ThreadPool.HttpSpiderThreadPool;
import com.myway5.www.ThreadPool.ProcessSpiderThreadPool;
import com.myway5.www.Util.ProcessSpiderConfig;
import com.myway5.www.client.SecondFilter;

public class MultiSpiderManager extends AbstSpiderManager{
	
	public void manage() {
		
		
	}
	
	public static void main(String[] args){
		Date date = Calendar.getInstance().getTime();
		long start = Calendar.getInstance().getTimeInMillis();
		boolean http = true,process = true,first = true,second = true;
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		System.out.println("start");
		HttpSpiderThreadPool httpSpiderThreadPool = new HttpSpiderThreadPool(25, 25);
		httpSpiderThreadPool.setStartUrl("http://wallpaper.pconline.com.cn");
		
		ProcessSpiderThreadPool processSpiderThreadPool = new ProcessSpiderThreadPool(5, 5);
		ProcessSpiderConfig processSpiderConfig = new ProcessSpiderConfig();
		processSpiderConfig.setTargetUrl("http://wallpaper\\.pconline\\.com\\.cn/pic/\\d+.*\\.html");
		processSpiderConfig.setLimitation("http://wallpaper\\.pconline\\.com\\.cn/.*");
		processSpiderThreadPool.setProcessSpiderConfig(processSpiderConfig);
		
		FilterSpiderThreadPool firstFilterSpiderThreadPool = new FilterSpiderThreadPool(5, 5);
		firstFilterSpiderThreadPool.setPool(MultiFilterPagePool.getInstance());
		firstFilterSpiderThreadPool.setFilter(new MultiFirstFilter());
		
		FilterSpiderThreadPool secondFilterSpiderThreadPool = new FilterSpiderThreadPool(25, 25);
		secondFilterSpiderThreadPool.setPool(FilterPool.getInstance());
		secondFilterSpiderThreadPool.setFilter(new MultiSecondFilter());
		
		while(http){
			http = httpSpiderThreadPool.startMultiExecute();
			processSpiderThreadPool.startMultiExecute();
			firstFilterSpiderThreadPool.startMultiExecute();
			secondFilterSpiderThreadPool.startMultiExecute();
		}
		Date date2 = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:ii:ss");
		long stop = Calendar.getInstance().getTimeInMillis();
		System.out.println("开始时间:" + format.format(date));
		System.out.println("终止时间:" + format.format(date2));
		System.out.println("总耗时:" + (stop - start));
	}

}
