package com.myway5.www.Monitor;

import com.myway5.www.Urlpool.AbstUrlPool;

public class MonitorEngine implements MonitorEngineMXBean{

	private AbstUrlPool urlPool;
	public MonitorEngine(AbstUrlPool urlPool){

		this.urlPool = urlPool;
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
