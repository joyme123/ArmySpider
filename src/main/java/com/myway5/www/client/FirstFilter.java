package com.myway5.www.client;

import com.mway5.www.Spider.AbstFilterSpider;

public class FirstFilter extends AbstFilterSpider{
	

	public void filter(Object o) {
		System.out.println("第一个过滤器启动----"+o.toString());
		this.runNext(o);		//启动下一个过滤器
	}

}
