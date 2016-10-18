package com.myway5.www.Spider;

import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Pool.MultiProcessPagePool;
import com.myway5.www.Urlpool.AbstUrlPool;
import com.myway5.www.Urlpool.MemoryUrlPool;
import com.myway5.www.Util.HttpSpiderConfig;
import com.myway5.www.Util.Page;


public class HttpSpider {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static String GET = "get";
	private static String POST = "post";
	private IProcessSpider processSpider = null;
	private Page page = new Page();
	private String method;
	private Map<String,String> data;
	private Map<String,String> cookies;
	private int timeout = 3000;//默认3秒，0代表无时间限制
	private String userAgent;
	private AbstUrlPool urlPool;
	private MultiProcessPagePool processPagePool = MultiProcessPagePool.getInstance();
	
	/*设置processSpider*/
	public void setProcessSpider(IProcessSpider processSpider2){
		this.processSpider = processSpider2;
	}
	
	/*设置cookie*/
	public void setCookies(Map<String,String> cookies){
		this.cookies = cookies;
	}
	
	public void setUrlPool(AbstUrlPool urlPool){
		this.urlPool = urlPool;
	}
	
	/*设置开始url*/
	public void setStartUrl(String url){
		urlPool.push(url);
	}
	
	/*设置要发送的数据*/
	public void setData(Map<String,String> data){
		this.data = data;
	}
	
	/*
	 * 设置超时时间，默认为三秒，0代表无超时限制
	 * @param int timeout 超时时间，毫秒为单位
	 */
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
	
	/*
	 * 设置请求方式，默认为GET
	 * @param String method 总共有两种:"get"或者"post"，不区分大小写
	 */
	public void setMethod(String method){
		this.method = method;
	}
	
	public void setUserAgent(String userAgent){
		this.userAgent = userAgent;
	}
	
	public void setConfig(HttpSpiderConfig config){
		this.method = config.getMethod();
		this.data = config.getData();
		this.timeout = config.getTimeout();
		this.cookies = config.getCookies();
		this.userAgent = config.getUserAgent();
	}
	
	public void requestPage(String url){
		Connection con = null;
		Document document = null;
		try{
			con = Jsoup.connect(url);
			logger.debug("Http爬虫启动---{}",url);
			if(cookies != null){
				con.cookies(cookies);
			}
			if(data != null){
				con.data(data);
			}
			if(userAgent != null){
				con.userAgent(userAgent);
			}
			con.timeout(timeout);
							
			if(method == null || method.equalsIgnoreCase(GET)){
				try {
					document = con.get();
				} catch (IOException e) {
					MemoryUrlPool.getInstance().updateFailedCount();
					logger.debug("http request failed{}"+e.getMessage());
				}
			}else if(method.equalsIgnoreCase(POST)){
				try {
					document = con.post();
				} catch (IOException e) {
					MemoryUrlPool.getInstance().updateFailedCount();
					logger.debug("http request failed{}"+e.getMessage());
				}
			}else{
				logger.error("不支持的请求类型");
			}
			if(document!=null){
				page.setDocument(document);
				page.setUrl(url);
				//如果设置了processSpider，则以普通的模式启动
				if(processSpider != null){
					processSpider.process(page);
				}else{
					//否则将page保存到page pool里
					processPagePool.push(page);
				}
				MemoryUrlPool.getInstance().updateSucceedCount();
				logger.trace("httpSpider启动");
			}
		}catch(IllegalArgumentException e){		//URL无效异常
			MemoryUrlPool.getInstance().updateFailedCount();
			logger.debug("无效或失败链接地址{}",url);
		}
	}
}
