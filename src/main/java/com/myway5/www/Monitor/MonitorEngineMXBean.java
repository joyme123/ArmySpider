package com.myway5.www.Monitor;

public interface MonitorEngineMXBean {
	public int getTotalUrlCount();
	public int getLeftUrlCount();
	public int getSucceedUrlCount();
	public int getFailedUrlCount();
}
