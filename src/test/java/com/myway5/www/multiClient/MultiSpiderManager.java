package com.myway5.www.multiClient;

import com.mway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.Pool.MultiFilterPagePool;
import com.myway5.www.ThreadPool.FilterSpiderThreadPool;
import com.myway5.www.ThreadPool.HttpSpiderThreadPool;
import com.myway5.www.ThreadPool.ProcessSpiderThreadPool;
import com.myway5.www.Util.ProcessSpiderConfig;
import com.myway5.www.client.SecondFilter;

public class MultiSpiderManager extends AbstSpiderManager{

	public void manage() {
		
		
	}
	
	public static void main(String[] args){
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		System.out.println("start");
		HttpSpiderThreadPool httpSpiderThreadPool = new HttpSpiderThreadPool(5, 5);
		httpSpiderThreadPool.setStartUrl("http://wallpaper.pconline.com.cn");
		
		ProcessSpiderThreadPool processSpiderThreadPool = new ProcessSpiderThreadPool(5, 5);
		ProcessSpiderConfig processSpiderConfig = new ProcessSpiderConfig();
		processSpiderConfig.setTargetUrl("http://wallpaper\\.pconline\\.com\\.cn/pic/\\d+.*\\.html");
		processSpiderConfig.setLimitation("http://wallpaper\\.pconline\\.com\\.cn/.*");
		processSpiderThreadPool.setProcessSpiderConfig(processSpiderConfig);
		
		FilterSpiderThreadPool firstFilterSpiderThreadPool = new FilterSpiderThreadPool(5, 5);
		firstFilterSpiderThreadPool.setPool(MultiFilterPagePool.getInstance());
		firstFilterSpiderThreadPool.setFilter(new MultiFirstFilter());
		
		FilterSpiderThreadPool secondFilterSpiderThreadPool = new FilterSpiderThreadPool(5, 5);
		secondFilterSpiderThreadPool.setPool(FilterPool.getInstance());
		secondFilterSpiderThreadPool.setFilter(new SecondFilter());
		
		while(true){
			httpSpiderThreadPool.startMultiExecute();
			processSpiderThreadPool.startMultiExecute();
			firstFilterSpiderThreadPool.startMultiExecute();
			secondFilterSpiderThreadPool.startMultiExecute();
		}
	}

}
