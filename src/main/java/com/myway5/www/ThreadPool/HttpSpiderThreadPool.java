package com.myway5.www.ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.HttpSpider;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.Urlpool.UrlPool;
import com.myway5.www.Util.HttpSpiderConfig;

public class HttpSpiderThreadPool{
	private ThreadPoolExecutor executor = null;
	private UrlPool urlPool = UrlPool.getInstance();
	private HttpSpiderConfig config = null;
	private ProcessSpider processSpider = null;
	private boolean run = true;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private int waitFlag = 0;
	
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
	
	public void setProcessSpider(ProcessSpider processSpider){
		this.processSpider = processSpider;
	}
	
	public void startExecute(){
		while(run){
			if(!urlPool.isEmpty()){
				final String url = urlPool.pull();
				executor.execute(new Runnable() {
					
					public void run() {
						HttpSpider httpSpider = new HttpSpider();
						if(config!=null)
							httpSpider.setConfig(config);
						httpSpider.setPrecessSpider(processSpider);
						httpSpider.requestPage(url);
					}
				});
			}else{
				//如果等待3秒，仍然没有新的url加入，就认为任务结束啦
				try {
					if(waitFlag == 0){
						Thread.sleep(3000);
						waitFlag = 1;
					}else{
						run = false;
						logger.debug("execute finish,progress stop!!!");
					}
				} catch (InterruptedException e) {
					logger.debug("执行出错{}",e.getMessage());
				}
			}
		}
	}
	
	public void startMultiExecute(){
		if(!urlPool.isEmpty()){
				waitFlag = 0;
				final String url = urlPool.pull();
				executor.execute(new Runnable() {
					
					public void run() {
						HttpSpider httpSpider = new HttpSpider();
						if(config!=null)
							httpSpider.setConfig(config);
						httpSpider.setPrecessSpider(processSpider);
						httpSpider.requestPage(url);
					}
				});
//			}else{
//				//如果等待3秒，仍然没有新的url加入，就认为任务结束啦
//				try {
//					if(waitFlag == 0){
//						Thread.sleep(3000);
//						waitFlag = 1;
//					}else{
//						run = false;
//					}
//				} catch (InterruptedException e) {
//					logger.debug("执行出错{}",e.getMessage());
//				}
			}
	}
}
