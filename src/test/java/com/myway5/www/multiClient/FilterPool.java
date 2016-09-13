package com.myway5.www.multiClient;

import com.myway5.www.Pool.AbstractMultiObjectPool;

public class FilterPool extends AbstractMultiObjectPool{
	private static FilterPool filterPool = null;
	
	public static FilterPool getInstance(){
		if(filterPool == null){
			filterPool = new FilterPool();
		}
		return filterPool;
	}
}
