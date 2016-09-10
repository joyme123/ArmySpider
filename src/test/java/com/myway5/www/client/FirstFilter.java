package com.myway5.www.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.AbstFilterSpider;
import com.myway5.www.util.Page;

public class FirstFilter extends AbstFilterSpider{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static String base = "http://www.pconline.com.cn/images/html/viewpic_pconline.htm?";
	
	public void filter(Object o) {
		Page page = (Page)o;
		Document document = page.getDocument();
		
		/*获取要下载图片的尺寸*/
		String size = null;
		Elements sizes = document.select("div.size > a");
		for(Element e : sizes){
			size = e.val();
		}
		
		if(size == null)
			return;
		
		String script = document.data();
		Pattern pattern = Pattern.compile("var url = '(.*)'\\.replace\\('resolution',seso\\);");
		Matcher matcher = pattern.matcher(script);
		
		StringBuilder urlBuilder = null;
		if(matcher.find()){
			urlBuilder = new StringBuilder(matcher.group(1));
		}
		
		String url = urlBuilder.toString();
		url = url.replace("resolution",size);
		
		logger.debug("第一个过滤器启动----{}---{}",page.getUrl(),base+url);
		this.runNext(url);		//启动下一个过滤器
	}

}
