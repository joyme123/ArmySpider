package com.myway5.www.Spider;

public interface IFilterSpider {
	public IFilterSpider setNextFilter(IFilterSpider nextFilterSpider);
	public void filter(Object o);
}
