package com.mway5.www.Spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class HttpSpider {
	
	private ProcessSpider processSpider;
	private CloseableHttpClient client;
	private String content = "测试content";
	public void setPrecessSpider(ProcessSpider processSpider){
		this.processSpider = processSpider;
	}
	public void requestPage(){
		client = HttpClients.createDefault();
		HttpGet request = new HttpGet("http://baidu.com");
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		InputStream content;
		try {
			content = entity.getContent();
			BufferedReader buf = new BufferedReader(new InputStreamReader(content));
			System.out.println("http爬虫启动-----"+buf.toString());
			processSpider.process(buf.toString());
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
