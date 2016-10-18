package com.myway5.www.Spider;

import com.myway5.www.Util.Page;
import com.myway5.www.Util.ProcessSpiderConfig;

public interface IProcessSpider {
	public void setConfig(ProcessSpiderConfig config);
	public void setTargetUrl(String regex);
	public void setLimitation(String regex);
	public void process(Page page);
	public void setFilterSpider(IFilterSpider filterSpider);
}
