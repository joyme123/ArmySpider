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
		System.out.println("start");
		HttpSpiderThreadPool httpSpiderThreadPool = new HttpSpiderThreadPool(5, 10);
		httpSpiderThreadPool.setStartUrl("http://wallpaper.pconline.com.cn");
		
		ProcessSpiderThreadPool processSpiderThreadPool = new ProcessSpiderThreadPool(5, 10);
		ProcessSpiderConfig processSpiderConfig = new ProcessSpiderConfig();
		processSpiderConfig.setTargetUrl("http://wallpaper\\.pconline\\.com\\.cn/pic/\\d+.*\\.html");
		processSpiderConfig.setLimitation("http://wallpaper\\.pconline\\.com\\.cn/.*");
		processSpiderThreadPool.setProcessSpiderConfig(processSpiderConfig);
		
		FilterSpiderThreadPool firstFilterSpiderThreadPool = new FilterSpiderThreadPool(5, 10);
		firstFilterSpiderThreadPool.setPool(new MultiFilterPagePool());
		firstFilterSpiderThreadPool.setFilter(new MultiFirstFilter());
		
		FilterSpiderThreadPool secondFilterSpiderThreadPool = new FilterSpiderThreadPool(5, 10);
		secondFilterSpiderThreadPool.setPool(new FilterPool());
		secondFilterSpiderThreadPool.setFilter(new SecondFilter());
		
		httpSpiderThreadPool.startExecute();
		processSpiderThreadPool.startExecute();
		firstFilterSpiderThreadPool.startExecute();
		secondFilterSpiderThreadPool.startExecute();
	}

}
