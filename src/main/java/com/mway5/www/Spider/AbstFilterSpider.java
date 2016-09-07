package com.mway5.www.Spider;

public abstract class AbstFilterSpider implements IFilterSpider{
	protected IFilterSpider nextFilterSpider;
	public IFilterSpider setNextFilter(IFilterSpider nextFilterSpider) {
		this.nextFilterSpider = nextFilterSpider;
		return this;
	}
	
	/**
	 * 启动下一个过滤器
	 */
	public void runNext(Object o){
		this.nextFilterSpider.filter(o);
	}
}
