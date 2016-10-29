package com.myway5.www.Urlpool;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.myway5.www.Urlpool.Remover.HashMapDuplicateRemover;
import com.myway5.www.Urlpool.Remover.IDuplicateUrlRemover;

public abstract class AbstUrlPool implements IUrlPool{
	private IDuplicateUrlRemover duplicateChecker = new HashMapDuplicateRemover();	//默认使用HashMap来去重
	protected AtomicInteger totalCount = new AtomicInteger(0);		//url总数量,不需要在子类中更新
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
	
	public long getTotalUrlCount(){
		return duplicateChecker.getTotalUrlCount();
	}
	
	/**
	 * 设置默认的removeChecker
	 * @param remover
	 */
	public void setDuplicateChecker(IDuplicateUrlRemover duplicateChecker){
		this.duplicateChecker = duplicateChecker;
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
			leftUrlCount.incrementAndGet();		//添加结束后leftUrlCount要自增
		}
	}

	/*
	 * 需要自己实现的方法
	 */
	public abstract void pushWithoutDuplicate(String url);



	public String pull() {
		String string = pullWithoutUpdateLeftUrlCount();
		if(string != null){
			leftUrlCount.decrementAndGet();
		}
		return string;
	}
	
	public abstract String pullWithoutUpdateLeftUrlCount();
	
	/**
	 * FileUrlPool需要调用的方法
	 * @throws IOException
	 */
	public void flush(){
		
	}
	
	/**
	 * 在使用FileUrlPool时通过这个函数获取一次写入的url条数
	 * 为0时表示不是使用的该类
	 * @return
	 */
	public int getUrlWriteCount(){
		return 0;
	}
	
	/**
	 * FileUrlPool需要调用的方法
	 * @throws IOException
	 */
	public void close() throws IOException{
		
	}
}
