package com.myway5.www.client;

import com.mway5.www.Spider.HttpSpider;
import com.mway5.www.Spider.ProcessSpider;
import com.mway5.www.SpiderManager.AbstSpiderManager;

public class SpiderManager extends AbstSpiderManager{

	public void manage() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		HttpSpider httpSpider = new HttpSpider();
		ProcessSpider processSpider = new ProcessSpider();
		FirstFilter firstFilter = new FirstFilter();
		SecondFilter secondFilter = new SecondFilter();
		ThirdFilter thirdFilter = new ThirdFilter();
		
		SpiderManager spiderManager = new SpiderManager();
		spiderManager.setHttpSpider(httpSpider).setProcessSpider(processSpider).setFilterSpider(firstFilter)
		.setFilterSpider(secondFilter).setFilterSpider(thirdFilter).run();
	}

}
