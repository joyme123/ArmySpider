package com.myway5.www.ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.myway5.www.Pool.AbstractMultiObjectPool;
import com.myway5.www.Spider.IFilterSpider;

public class FilterSpiderThreadPool{
	private ThreadPoolExecutor executor = null;
	private AbstractMultiObjectPool multiPool = null;
	private IFilterSpider filterSpider = null;
	private volatile int runningThreadCount = 0;
	
	public FilterSpiderThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,BlockingQueue<Runnable> workQueue){
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public FilterSpiderThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
	}
	
	public FilterSpiderThreadPool(int corePoolSize,int maximumPoolSize){
		long keepAliveTime = 3000;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
	}
	
	public void setPool(AbstractMultiObjectPool multiPool){
		this.multiPool = multiPool;
	}
	
	public void setFilter(IFilterSpider filterSpider){
		this.filterSpider = filterSpider;
	}
	
	public int getRunningThreadCount() {
		return runningThreadCount;
	}

	public void setRunningThreadCount(int runningThreadCount) {
		this.runningThreadCount = runningThreadCount;
	}
	
	public void startMultiExecute(){
		if(!multiPool.isEmpty()){
			final Object o = multiPool.pull();
			executor.execute(new Runnable() {
				public void run() {
					filterSpider.filter(o);
					
				}
			});
		}
	}
}
