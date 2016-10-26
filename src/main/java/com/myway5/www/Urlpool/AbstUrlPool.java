package com.myway5.www.Urlpool;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.myway5.www.Urlpool.Remover.HashMapDuplicateRemover;
import com.myway5.www.Urlpool.Remover.IDuplicateUrlRemover;

public abstract class AbstUrlPool implements IUrlPool{
	private IDuplicateUrlRemover duplicateChecker = new HashMapDuplicateRemover();	//默认使用HashMap来去重
	protected AtomicInteger totalCount = new AtomicInteger(0);		//url总数量,在子类中需要更新
	protected AtomicInteger leftUrlCount = new AtomicInteger(0);	//还没执行的url数量，在子类中需要更新
	private AtomicInteger succeedCount = new AtomicInteger(0);	 	//成功的url数量
	private AtomicInteger failedCount = new AtomicInteger(0);		//失败的url数量
	
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
	
	public int getTotalUrlCount(){
		return duplicateChecker.getTotalUrlCount();
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
	
	/**
	 * 提供给持久化接口使用，从文件中读入url时维持一个完整的remover
	 * @param url
	 */
	public void pushInRemover(String url){
		duplicateChecker.isDuplicated(url);
	}
	
	public void push(String url) {
		if(!duplicateChecker.isDuplicated(url)){
			//不重复的url
			pushWithoutDuplicate(url);
		}
	}

	/*
	 * 需要自己实现的方法
	 */
	public void pushWithoutDuplicate(String url){
		
	}


	/*
	 * 需要自己实现的方法
	 * @see com.myway5.www.Urlpool.IUrlPool#pull()
	 */
	public String pull() {
		return null;
	}
	
	/**
	 * FileUrlPool需要调用的方法
	 * @throws IOException
	 */
	public void flush(){
		
	}
	
	/**
	 * FileUrlPool需要调用的方法
	 * @throws IOException
	 */
	public void close() throws IOException{
		
	}
}
