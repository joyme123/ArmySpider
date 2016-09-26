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
	
	public Boolean startMultiExecute(){
		boolean run = true;
		if(!multiPool.isEmpty()){
			final Object o = multiPool.pull();
			Future<Boolean> future = executor.submit(new Callable<Boolean>() {
				public Boolean call() throws Exception {
					filterSpider.filter(o);
					return true;
				}
			});
			try {
				if(future.get()){
					//任务执行结束
					runningThreadCount--;
				}
			} catch (InterruptedException e) {
				//线程异常中断，也认为执行结束
				runningThreadCount--;
				e.printStackTrace();
			} catch (ExecutionException e) {
				// 执行异常也认为结束
				runningThreadCount--;
				e.printStackTrace();
			}
		}else if(runningThreadCount == 0){
			//如果当前正在执行的线程数也为0，则httpSpider的任务结束
			//关闭hhtpSpider的线程池
			executor.shutdown();
			run = false;
		}
		
		return run;
	}
}
