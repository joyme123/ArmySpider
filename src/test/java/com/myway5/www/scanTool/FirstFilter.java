package com.myway5.www.scanTool;

import java.io.IOException;
import javax.swing.JTextPane;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.AbstFilterSpider;
import com.myway5.www.Util.Page;

public class FirstFilter extends AbstFilterSpider{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private JTextPane area;
	private JTextPane badArea;
	
	public FirstFilter(JTextPane area,JTextPane badArea) {
		this.area = area;
		this.badArea = badArea;
	}
	
	public String urlFormat(String url){
		String[] string = url.split("\\?");
		String[] param = string[1].split("&");
		StringBuffer buffer = new StringBuffer();
		buffer.append("?");
		int i = 0;
		for(String s : param){
			if(i == 0){
				buffer.append(s+"'");
				i++;
			}else{
				buffer.append("&"+s+"'");
			}
		}
		return string[0]+buffer.toString();
	}
	
	public void filter(Object o) {
		Page page = (Page)o;
		String url = "";
		if(page.getUrl().contains("?")){
			url = urlFormat(page.getUrl());
		}else{
			url = page.getUrl()+"?id=1'";
		}
		Connection jsoup = Jsoup.connect(url);
		try {
			Response response = jsoup.execute();
			if(response.body().contains("SQL")){
				badArea.setText(url+"         可注入\n"+badArea.getText());
			}else{
				area.setText(url+"        不可注入\n"+area.getText());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("能否注入判断启动");
		
//		Document document = page.getDocument();
		
//		/*获取要下载图片的尺寸*/
//		String size = null;
//		Elements sizes = document.select("div.size > a");
//		for(Element e : sizes){
//			size = e.val();
//		}
//		
//		if(size == null)
//			return;
//		
//		String script = document.data();
//		Pattern pattern = Pattern.compile("var url = '(.*)'\\.replace\\('resolution',seso\\);");
//		Matcher matcher = pattern.matcher(script);
//		
//		StringBuilder urlBuilder = null;
//		if(matcher.find()){
//			urlBuilder = new StringBuilder(matcher.group(1));
//		}
//		
//		String url = urlBuilder.toString();
//		url = url.replace("resolution",size);
//		
//		logger.debug("第一个过滤器启动----{}---{}",page.getUrl(),base+url);
//		this.runNext(url);		//启动下一个过滤器
	}

}
