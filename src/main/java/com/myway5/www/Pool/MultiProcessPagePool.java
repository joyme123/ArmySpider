package com.myway5.www.Pool;

public class MultiProcessPagePool extends AbstractMultiObjectPool{
	private static MultiProcessPagePool multiPagePool = null;
	
	public static MultiProcessPagePool getInstance(){
		if(multiPagePool == null){
			multiPagePool = new MultiProcessPagePool();
		}
		return multiPagePool;
	}
}
