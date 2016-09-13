package com.myway5.www.Util;

public class ProcessSpiderConfig {
	private String targetUrl;
	private String limitation;
	
	public ProcessSpiderConfig(){
		
	}
	
	public ProcessSpiderConfig(String targetUrl, String limitation) {
		this.targetUrl = targetUrl;
		this.limitation = limitation;
	}
	
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getLimitation() {
		return limitation;
	}
	public void setLimitation(String limitation) {
		this.limitation = limitation;
	}
	
}
