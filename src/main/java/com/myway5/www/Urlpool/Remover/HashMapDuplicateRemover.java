package com.myway5.www.Urlpool.Remover;

import java.util.concurrent.ConcurrentHashMap;

public class HashMapDuplicateRemover implements IDuplicateUrlRemover{
	private ConcurrentHashMap<String, Boolean> urls = new ConcurrentHashMap<String, Boolean>();

	public Boolean isDuplicated(String url) {
		if(urls.get(url) == null){
			urls.put(url, true);
			return false;
		}
		return true;
	}

	public long getTotalUrlCount() {
		return urls.size();
	}

}
