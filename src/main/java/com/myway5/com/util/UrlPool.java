package com.myway5.com.util;

import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class UrlPool{
	private ConcurrentLinkedQueue<URL> urlPool;
	private AtomicInteger leftUrlCount = new AtomicInteger(0);
	public boolean isEmpty(){
		if(leftUrlCount.intValue() <= 0){
			return false;
		}
		return true;
	}
	public URL dequeue(){
		leftUrlCount.decrementAndGet();
		return urlPool.poll();
	}
	public void enqueue(URL url){
		leftUrlCount.incrementAndGet();
		urlPool.offer(url);
	}
	public int getLeftUrlCount(){
		return leftUrlCount.intValue();
	}
}
