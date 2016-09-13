package com.myway5.www.Urlpool;

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

	public int getTotalUrlCount() {
		return urls.size();
	}

}
