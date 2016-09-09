package com.myway5.www.Spider;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.urlpool.UrlPool;
import com.myway5.www.util.Page;

public class ProcessSpider {
	private IFilterSpider filterSpider;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String targetUrlRegex = null;
	private String limitation = null;
	
	/*注入FilterSpider以调用*/
	public void setFilterSpider(IFilterSpider filterSpider){
		this.filterSpider = filterSpider;
	}
	
	public void setTargetUrl(String regex){
		this.targetUrlRegex = regex;
	}
	
	public void setLimitation(String regex){
		this.limitation = regex;
	}
	
	/*
	 * ProcessSpider的处理函数，这里进行新的url的发现，并将感兴趣的区域传递给filter进行处理
	 * @param Page HttpSpider获取到的Page对象
	 */
	public void process(Page page){
		logger.debug("处理爬虫启动-----{}",page.getUrl());
		Elements links = page.getDocument().getElementsByTag("a");
		
		//获取静态的线程池
		UrlPool urlPool = UrlPool.getInstance();
		for(Element link : links){
			String temp = link.attr("href").trim();
			if(limitation == null || Pattern.matches(limitation, temp))
				urlPool.push(temp);
		}
		//如果对目标地址没有要求或者是需要的地址，才会传递到过滤器中进行处理
		if(targetUrlRegex == null || Pattern.matches(targetUrlRegex, page.getUrl())){
			filterSpider.filter(page);
		}
	}
}
