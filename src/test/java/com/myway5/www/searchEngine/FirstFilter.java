package com.myway5.www.searchEngine;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.myway5.www.Spider.AbstFilterSpider;
import com.myway5.www.Util.Page;

public class FirstFilter extends AbstFilterSpider{
	private Logger logger = LoggerFactory.getLogger(getClass());
	DocumentEntry documentEntry = DocumentEntry.getInstance();
	public void filter(Object o) {
		System.out.println("filter启动");
		Page page = (Page)o;
		Document document = page.getDocument();
		final HtmlDocument  htmlDocument = new HtmlDocument(document.getElementsByTag("title").text(),
													page.getUrl(),
													document.getElementsByTag("html").html());

		documentEntry.insertDocument(htmlDocument);
		
	}

}
