package com.myway5.www.ThreadPool;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.HttpSpider;
import com.myway5.www.Spider.IProcessSpider;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.Urlpool.AbstUrlPool;
import com.myway5.www.Urlpool.MemoryUrlPool;
import com.myway5.www.Util.HttpSpiderConfig;

public class HttpSpiderThreadPool{
	private ThreadPoolExecutor executor = null;
	private AbstUrlPool urlPool = null;
	private HttpSpiderConfig config = null;
	private IProcessSpider processSpider = null;
	private boolean run = true;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private volatile int runningThreadCount = 0;
	//private int waitFlag = 0;
	
	public HttpSpiderThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,BlockingQueue<Runnable> workQueue) {
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public HttpSpiderThreadPool(int corePoolSize,int maximumPoolSize){
		long keepAliveTime = 3000;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
	}
	
	public HttpSpiderThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
	}
	
	public void setHttpSpiderConfig(HttpSpiderConfig config){
		this.config = config;
	}
	
	public void setStartUrl(String url){
		urlPool.push(url);
	}
	
	public void setProcessSpider(IProcessSpider processSpider){
		this.processSpider = processSpider;
	}
	
	public int getRunningThreadCount() {
		return runningThreadCount;
	}

	public void setRunningThreadCount(int runningThreadCount) {
		this.runningThreadCount = runningThreadCount;
	}
	
	public void setUrlPool(AbstUrlPool urlPool){
		this.urlPool = urlPool;
	}

	public void startExecute(){
		while(run){
			if(urlPool != null&&!urlPool.isEmpty()){
				
				final String url = urlPool.pull();
				runningThreadCount++;		//一条url出栈，则认为当前已经开始提交一项任务（可能立即执行，也可能需要排队）
				Future<Boolean> future = executor.submit(new Callable<Boolean>() {
					public Boolean call() throws Exception {
						HttpSpider httpSpider = new HttpSpider();
						if(config!=null)
							httpSpider.setConfig(config);
						httpSpider.setProcessSpider(processSpider);
						httpSpider.requestPage(url);
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
				run = false;
				//关闭hhtpSpider的线程池
				executor.shutdown();
				try {
					urlPool.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Boolean startMultiExecute(){
		boolean run = true;
		if(!urlPool.isEmpty()){
			final String url = urlPool.pull();
			Future<Boolean> future = executor.submit(new Callable<Boolean>() {
				public Boolean call() throws Exception {
					HttpSpider httpSpider = new HttpSpider();
					if(config!=null)
						httpSpider.setConfig(config);
					httpSpider.setProcessSpider(processSpider);
					httpSpider.requestPage(url);
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
			return true;
		}else if(runningThreadCount == 0){
			//如果当前正在执行的线程数也为0，则httpSpider的任务结束
			//关闭httpSpider的线程池
			executor.shutdown();
			try {
				urlPool.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			run = false;
		}
		return run;
	}
}
