package com.myway5.www.Spider;

public abstract class AbstFilterSpider implements IFilterSpider{
	protected IFilterSpider nextFilterSpider = null;
	public IFilterSpider setNextFilter(IFilterSpider nextFilterSpider) {
		this.nextFilterSpider = nextFilterSpider;
		return this;
	}
	
	public boolean isSetNextFilterSpider(){
		if(this.nextFilterSpider != null)
			return true;
		return false;
	}
	
	/**
	 * 启动下一个过滤器
	 */
	public void runNext(Object o){
		if(this.nextFilterSpider != null)
			this.nextFilterSpider.filter(o);
	}
}
