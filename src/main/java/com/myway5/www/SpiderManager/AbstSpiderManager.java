package com.myway5.www.SpiderManager;

import com.myway5.www.Spider.HttpSpider;
import com.myway5.www.Spider.IFilterSpider;
import com.myway5.www.Spider.IProcessSpider;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.ThreadPool.HttpSpiderThreadPool;
import com.myway5.www.Urlpool.AbstUrlPool;
import com.myway5.www.Urlpool.MemoryUrlPool;

public abstract class AbstSpiderManager implements ISpiderManager{
	private Boolean isFirstFilter = true;
	protected IFilterSpider preFilterSpider;
	protected IProcessSpider processSpider;
	protected HttpSpider httpSpider;
	protected HttpSpiderThreadPool httpSpiderThreadPool;
	protected AbstUrlPool urlPool;				//用来存储url实例的池
	/*
	 * 设置进行Http请求的爬虫
	 * @param httpSpider HttpSpider对象
	 * @return SpiderManager对象
	 */
	public AbstSpiderManager setHttpSpider(HttpSpider httpSpider){
		this.httpSpider = httpSpider;
		if(processSpider!=null){
			this.httpSpider.setProcessSpider(processSpider);
		}
		return this;
	}
	
	/*
	 * 设置用来处理页面的爬虫,包括提取新的url地址，发现target地址
	 * @param processSpider ProcessSpider对象
	 * @return SpiderManager对象
	 */
	public AbstSpiderManager setProcessSpider(IProcessSpider processSpider){
		this.processSpider = processSpider;
		if(httpSpiderThreadPool != null){
			httpSpiderThreadPool.setProcessSpider(processSpider);
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
	
	public AbstSpiderManager setUrlPool(AbstUrlPool urlPool){
		this.urlPool = urlPool;
		this.httpSpiderThreadPool.setUrlPool(urlPool);
		this.processSpider.setUrlPool(urlPool);
		return this;
	}
	
	public AbstSpiderManager thread(int threadNum){
		httpSpiderThreadPool = new HttpSpiderThreadPool(threadNum, threadNum);
		if(this.processSpider!=null){
			httpSpiderThreadPool.setProcessSpider(processSpider);
		}
		return this;
	}
	
	public AbstSpiderManager thread(int corePoolSize,int MaximumPoolSize){
		httpSpiderThreadPool = new HttpSpiderThreadPool(corePoolSize, MaximumPoolSize);
		if(this.processSpider!=null){
			httpSpiderThreadPool.setProcessSpider(processSpider);
		}
		return this;
	}
	
	public AbstSpiderManager thread(int corePoolSize,int MaximumPoolSize,long keepAliveTime){
		httpSpiderThreadPool = new HttpSpiderThreadPool(corePoolSize, MaximumPoolSize,keepAliveTime);
		if(this.processSpider!=null){
			httpSpiderThreadPool.setProcessSpider(processSpider);
		}
		return this;
	}
	
	
	public AbstSpiderManager setStartUrl(String url){
		this.urlPool.push(url);
		return this;
	}
	
	public void run(){
		httpSpiderThreadPool.startExecute();
	}
	
}
