package com.mway5.www.Spider;

import javax.naming.event.ObjectChangeListener;

public class ProcessSpider {
	private IFilterSpider filterSpider;
	public ProcessSpider setFilter(IFilterSpider filterSpider){
		this.filterSpider = filterSpider;
		return this;
	}
	
	public void process(Object o){
		System.out.println("处理爬虫启动-----"+o.toString());
		filterSpider.filter(o);
	}
}
