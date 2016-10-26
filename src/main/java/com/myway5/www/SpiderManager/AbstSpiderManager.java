package com.myway5.www.SpiderManager;

import java.util.LinkedList;
import java.util.Queue;

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
	protected HttpSpiderThreadPool httpSpiderThreadPool;
	protected AbstUrlPool urlPool;											//用来存储url实例的池
	private Queue<String> urlQueue;				//当urlPool为null时，用来临时存储url
	
	/*
	 * 设置用来处理页面的爬虫,包括提取新的url地址，发现target地址
	 * @param processSpider ProcessSpider对象
	 * @return SpiderManager对象
	 */
	public AbstSpiderManager setProcessSpider(IProcessSpider processSpider){
		this.processSpider = processSpider;
		waitForReady();
		return this;
	}
	
	/*
	 * 设置后续的过滤爬虫，可以不断的对页面进行处理，获取需要的信息，根据加入的过滤位置依次排序
	 * filterSpider必须在processSpider之后加入
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
		waitForReady();
		return this;
	}
	
	public AbstSpiderManager thread(int threadNum){
		httpSpiderThreadPool = new HttpSpiderThreadPool(threadNum, threadNum);
		waitForReady();
		return this;
	}
	
	public AbstSpiderManager thread(int corePoolSize,int MaximumPoolSize){
		httpSpiderThreadPool = new HttpSpiderThreadPool(corePoolSize, MaximumPoolSize);
		waitForReady();
		return this;
	}
	
	public AbstSpiderManager thread(int corePoolSize,int MaximumPoolSize,long keepAliveTime){
		httpSpiderThreadPool = new HttpSpiderThreadPool(corePoolSize, MaximumPoolSize,keepAliveTime);
		waitForReady();
		return this;
	}
	
	
	public AbstSpiderManager setStartUrl(String url){
		if(urlQueue == null)
			urlQueue = new LinkedList<String>();
		urlQueue.add(url);
		return this;
	}
	
	public void waitForReady(){
		if(urlPool != null&&
		   urlQueue != null&&
		   httpSpiderThreadPool != null&&
		   processSpider != null){
			httpSpiderThreadPool.setUrlPool(urlPool);
			httpSpiderThreadPool.setProcessSpider(processSpider);
			/**填充urlPool**/
			for(String url : urlQueue){
				urlPool.push(url);
			}
			
			//设置processSpider的属性
			processSpider.setUrlPool(urlPool);
			
		}
	}
	
	public void run(){
		httpSpiderThreadPool.startExecute();
	}
	
	public void shutdown(){
		
	}
}
