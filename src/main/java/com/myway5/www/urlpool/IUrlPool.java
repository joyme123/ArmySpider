package com.myway5.www.urlpool;

public interface IUrlPool {
	public void push(String url);
	public String pull();
}
