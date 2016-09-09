package com.myway5.www.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.AbstFilterSpider;
import com.myway5.www.util.Page;

public class SecondFilter extends AbstFilterSpider{
	private Logger logger = LoggerFactory.getLogger(getClass());
	public void filter(Object o) {
		logger.debug("第二个过滤器启动----{}",((Page)o).getUrl());
		this.runNext(o);		//启动下一个过滤器
	}

}
