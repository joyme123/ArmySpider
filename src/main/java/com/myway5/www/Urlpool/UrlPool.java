package com.myway5.www.Urlpool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
/*
 * url地址池，保存url，单例模式
 * 
 */
public class UrlPool extends AbstUrlPool{
	private static UrlPool urlPool = null;
	private ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<String>();
	private AtomicInteger totalCount = new AtomicInteger(0);		//url总数量
	private AtomicInteger leftUrlCount = new AtomicInteger(0);		//还没执行的url数量
	private AtomicInteger succeedCount = new AtomicInteger(0);	 	//成功的url数量
	private AtomicInteger failedCount = new AtomicInteger(0);		//失败的url数量
	
	public static UrlPool getInstance(){
		if(urlPool == null){
			urlPool = new UrlPool();
		}
		return urlPool;
	}
	
	public int getTotalCount(){
		return this.totalCount.get();
	}
	
	
	public int getLeftUrlCount(){
		return leftUrlCount.get();
	}
	
	public int getSucceedCount(){
		return this.succeedCount.get();
	}
	
	public int getFailedCount(){
		return this.failedCount.get();
	}
	
	
	public void updateSucceedCount(){
		succeedCount.incrementAndGet();
	}
	
	public void updateFailedCount(){
		failedCount.incrementAndGet();
	}
	
	/**
	 * 判断url池是否为空，如果为空则返回true,否则返回false
	 * @return boolean
	 */
	public boolean isEmpty(){
		if(leftUrlCount.intValue() <= 0){
			return true;
		}
		return false;
	}

	@Override
	public void pushWithoutDuplicate(String url) {
		urlQueue.offer(url);
		totalCount.incrementAndGet();		//更新总数
		leftUrlCount.incrementAndGet();		//更新剩余数
	}

	public String pull() {
		leftUrlCount.decrementAndGet();
		return urlQueue.poll();
	}
}
