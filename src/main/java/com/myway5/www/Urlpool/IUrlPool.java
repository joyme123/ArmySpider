package com.myway5.www.Urlpool;

import java.io.IOException;

public interface IUrlPool {
	public void push(String url);
	public String pull();
}
