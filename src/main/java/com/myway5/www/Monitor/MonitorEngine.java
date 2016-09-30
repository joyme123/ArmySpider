package com.myway5.www.Monitor;

import com.myway5.www.Urlpool.MemoryUrlPool;

public class MonitorEngine implements MonitorEngineMXBean{

	private MemoryUrlPool urlPool;
	public MonitorEngine(){

		this.urlPool = MemoryUrlPool.getInstance();
	}
	
	public int getTotalUrlCount() {
		return this.urlPool.getTotalCount();
	}

	public int getLeftUrlCount() {
		return this.urlPool.getLeftUrlCount();
		
	}

	public int getSucceedUrlCount() {
		return this.urlPool.getSucceedCount();
		
	}

	public int getFailedUrlCount() {
		return this.urlPool.getFailedCount();
		
	}

}
