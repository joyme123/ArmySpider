package com.myway5.www.Pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AbstractMultiObjectPool {
	private ConcurrentLinkedQueue<Object> pagePool = new ConcurrentLinkedQueue<Object>();
	private AtomicInteger leftPageCount = new AtomicInteger(0);
	
	public boolean isEmpty(){
		if(leftPageCount.intValue() <= 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 入队列 入队列成功则返回true 失败返回false
	 * @param page
	 * @return
	 */
	public boolean push(Object o){
		if(pagePool.offer(o)){
			leftPageCount.incrementAndGet();
			return true;
		}
		return false;
	}
	
	public Object pull(){
		leftPageCount.decrementAndGet();
		return pagePool.poll();
		
	}
}
