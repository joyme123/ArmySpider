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
	private AtomicInteger leftUrlCount = new AtomicInteger(0);
	
	public static UrlPool getInstance(){
		if(urlPool == null){
			urlPool = new UrlPool();
		}
		return urlPool;
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
		leftUrlCount.incrementAndGet();
	}

	public String pull() {
		leftUrlCount.decrementAndGet();
		return urlQueue.poll();
	}
	
	public int getLeftUrlCount(){
		return leftUrlCount.intValue();
	}
}
