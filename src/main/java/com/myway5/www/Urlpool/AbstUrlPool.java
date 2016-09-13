package com.myway5.www.Urlpool;

public abstract class AbstUrlPool implements IUrlPool{
	private IDuplicateUrlRemover duplicateChecker = new HashMapDuplicateRemover();	//默认使用HashMap来去重
	
	public int getTotalUrlCount(){
		return duplicateChecker.getTotalUrlCount();
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
	
}
