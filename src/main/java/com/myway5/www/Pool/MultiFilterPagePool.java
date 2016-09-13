package com.myway5.www.Pool;

public class MultiFilterPagePool extends AbstractMultiObjectPool{
	private static MultiFilterPagePool multiPagePool = null;
	
	public static MultiFilterPagePool getInstance(){
		if(multiPagePool == null){
			multiPagePool = new MultiFilterPagePool();
		}
		return multiPagePool;
	}
}
