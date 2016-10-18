package com.myway5.www.Spider;

import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Pool.MultiFilterPagePool;
import com.myway5.www.Urlpool.AbstUrlPool;
import com.myway5.www.Urlpool.MemoryUrlPool;
import com.myway5.www.Util.Page;
import com.myway5.www.Util.ProcessSpiderConfig;

public class ProcessSpider implements IProcessSpider{
	private IFilterSpider filterSpider = null;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String targetUrlRegex = null;
	private String limitation = null;
	private AbstUrlPool urlPool = null;
	
	/*注入FilterSpider以调用*/
	public void setFilterSpider(IFilterSpider filterSpider){
		this.filterSpider = filterSpider;
	}
	
	public void setConfig(ProcessSpiderConfig config) {
		setTargetUrl(config.getTargetUrl());
		setLimitation(config.getLimitation());
	}
	
	public void setTargetUrl(String regex){
		this.targetUrlRegex = regex;
	}
	
	public void setLimitation(String regex){
		this.limitation = regex;
	}

	public void setUrlPool(AbstUrlPool urlPool){
		this.urlPool = urlPool;
	}
	
	/*
	 * ProcessSpider的处理函数，这里进行新的url的发现，并将感兴趣的区域传递给filter进行处理
	 * @param Page 可以通过HttpSpider获取到的Page对象直接传递过来（普通模式)，也可以在ProcessSpiderThreadPool中传递(多线程组合模式)
	 */
	public void process(Page page){
		logger.debug("处理爬虫启动-----{}",page.getUrl());
		Elements links = page.getDocument().getElementsByTag("a");
		
		for(Element link : links){
			String temp = link.attr("abs:href").trim();			//获取页面的绝对地址
			if(limitation == null || Pattern.matches(limitation, temp)){
				urlPool.push(temp);
			}
		}
		//如果对目标地址没有要求或者是需要的地址，才会传递到过滤器中进行处理
		if(targetUrlRegex == null || Pattern.matches(targetUrlRegex, page.getUrl())){
			//如果设置了filterSpider,则使用普通的模式启动，否则将page提交的pool里
			if(filterSpider != null){
				filterSpider.filter(page);
			}else{
				MultiFilterPagePool multiFilterPagePool = MultiFilterPagePool.getInstance();
				multiFilterPagePool.push(page);
			}
			
		}
	}


}
