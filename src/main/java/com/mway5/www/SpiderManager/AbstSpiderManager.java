package com.mway5.www.SpiderManager;

import com.myway5.www.Spider.HttpSpider;
import com.myway5.www.Spider.IFilterSpider;
import com.myway5.www.Spider.ProcessSpider;

public abstract class AbstSpiderManager implements ISpiderManager{
	private Boolean isFirstFilter = true;
	protected IFilterSpider preFilterSpider;
	protected ProcessSpider processSpider;
	protected HttpSpider httpSpider;
	/*
	 * 设置进行Http请求的爬虫
	 * @param httpSpider HttpSpider对象
	 * @return SpiderManager对象
	 */
	public AbstSpiderManager setHttpSpider(HttpSpider httpSpider){
		this.httpSpider = httpSpider;
		if(processSpider!=null){
			this.httpSpider.setPrecessSpider(processSpider);
		}
		return this;
	}
	
	/*
	 * 设置用来处理页面的爬虫,包括提取新的url地址，发现target地址
	 * @param processSpider ProcessSpider对象
	 * @return SpiderManager对象
	 */
	public AbstSpiderManager setProcessSpider(ProcessSpider processSpider){
		this.processSpider = processSpider;
		if(httpSpider != null){
			httpSpider.setPrecessSpider(processSpider);
		}
		return this;
	}
	
	/*
	 * 设置后续的过滤爬虫，可以不断的对页面进行处理，获取需要的信息
	 * @param filterSpider FilterSpider对象
	 * @return SpiderManager对象
	 */
	public AbstSpiderManager setFilterSpider(IFilterSpider filterSpider){
		//如果是第一个过滤器，则
		if(isFirstFilter){
			this.processSpider.setFilterSpider(filterSpider);
			isFirstFilter = false;
		}else{
			this.preFilterSpider.setNextFilter(filterSpider);
		}
		preFilterSpider = filterSpider;
		return this;
	}
	public void run(){
		httpSpider.requestPage();
	}
	
}
