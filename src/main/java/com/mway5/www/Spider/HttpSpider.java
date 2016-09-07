package com.mway5.www.Spider;

public class HttpSpider {
	private ProcessSpider processSpider;
	public void setPrecessSpider(ProcessSpider processSpider){
		this.processSpider = processSpider;
	}
	public void requestPage(){
		String string = "hahaha";
		System.out.println("http爬虫启动-----"+string);
		processSpider.process(string);
	}
}
