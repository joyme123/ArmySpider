package com.myway5.www.ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.myway5.www.Pool.MultiProcessPagePool;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.Util.Page;
import com.myway5.www.Util.ProcessSpiderConfig;

public class ProcessSpiderThreadPool{
	private ThreadPoolExecutor executor = null;
	private MultiProcessPagePool multiPagePool = MultiProcessPagePool.getInstance();
	private ProcessSpiderConfig config = null;		//所有processSpider遵循的配置
	private volatile int runningThreadCount;
	
	public ProcessSpiderThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,BlockingQueue<Runnable> workQueue){
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public ProcessSpiderThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
	}
	
	public ProcessSpiderThreadPool(int corePoolSize,int maximumPoolSize){
		long keepAliveTime = 3000;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
	}
	
	public void setProcessSpiderConfig(ProcessSpiderConfig config){
		this.config = config;
	}
	
	public Boolean startMultiExecute(){
		Boolean run = true;
		if(!multiPagePool.isEmpty()){
			final Page page = (Page) multiPagePool.pull();
			executor.execute(new Runnable() {
				
				public void run() {
					ProcessSpider processSpider = new ProcessSpider();
					if(config!=null){
						processSpider.setConfig(config);
					}
					processSpider.process(page);
				}
			});
		}else if(runningThreadCount == 0){
			//如果当前正在执行的线程数也为0，则httpSpider的任务结束
			//关闭hhtpSpider的线程池
			executor.shutdown();
			run = false;
		}
		
		return run;
	}
	
}
