package com.myway5.www.Urlpool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
/*
 * url地址池，保存url，单例模式
 * 
 */
public class MemoryUrlPool extends AbstUrlPool{
	private static MemoryUrlPool urlPool = null;
	private ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<String>();
	
	private MemoryUrlPool(){}
	
	public static MemoryUrlPool getInstance(){
		if(urlPool == null){
			urlPool = new MemoryUrlPool();
		}
		return urlPool;
	}

	@Override
	public void pushWithoutDuplicate(String url) {
		urlQueue.offer(url);
		totalCount.incrementAndGet();		//更新总数
		leftUrlCount.incrementAndGet();		//更新剩余数
	}

	@Override
	public String pull() {
		leftUrlCount.decrementAndGet();
		return urlQueue.poll();
	}
}
