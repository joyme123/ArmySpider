package com.myway5.www.client;

import com.mway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.Spider.HttpSpider;
import com.myway5.www.Spider.ProcessSpider;

public class SpiderManager extends AbstSpiderManager{

	public void manage() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		System.out.println("start");
		HttpSpider httpSpider = new HttpSpider();
		httpSpider.setStartUrl("http://wallpaper.pconline.com.cn");
		
		ProcessSpider processSpider = new ProcessSpider();
		processSpider.setTargetUrl("http://wallpaper\\.pconline\\.com\\.cn/pic/\\d+.*\\.html");
		processSpider.setLimitation("http://wallpaper\\.pconline\\.com\\.cn/.*");
		
		FirstFilter firstFilter = new FirstFilter();
		SecondFilter secondFilter = new SecondFilter();
		//ThirdFilter thirdFilter = new ThirdFilter();
		
		SpiderManager spiderManager = new SpiderManager();
		spiderManager
		.setHttpSpider(httpSpider)
		.setProcessSpider(processSpider)
		.setFilterSpider(firstFilter)
		.setFilterSpider(secondFilter)
		.run();
	}

}
