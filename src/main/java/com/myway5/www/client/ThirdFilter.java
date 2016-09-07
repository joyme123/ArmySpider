package com.myway5.www.client;

import com.mway5.www.Spider.AbstFilterSpider;

public class ThirdFilter extends AbstFilterSpider{

	public void filter(Object o) {
		System.out.println("第三个过滤器启动----"+o.toString());
	}

}
