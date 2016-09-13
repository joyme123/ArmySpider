package com.myway5.www.Util;

import java.util.HashMap;

public class HttpSpiderConfig {
	private HashMap<String, String> cookies;
	private String method;
	private HashMap<String, String> data;
	private int timeout;
	private String userAgent;
	
	public HttpSpiderConfig(){
		
	}
	
	public HttpSpiderConfig(HashMap<String, String> cookies, String method, HashMap<String, String> data, int timeout,
			String userAgent) {
		this.cookies = cookies;
		this.method = method;
		this.data = data;
		this.timeout = timeout;
		this.userAgent = userAgent;
	}
	
	public HashMap<String, String> getCookies() {
		return cookies;
	}
	public void setCookies(HashMap<String, String> cookies) {
		this.cookies = cookies;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public HashMap<String, String> getData() {
		return data;
	}
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}
