package com.myway5.www.filePersistenceClientwithbloom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.AbstFilterSpider;

public class SecondFilter  extends AbstFilterSpider{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void filter(Object o) {
		logger.debug("第二个过滤器启动----{}",(String)o);
		String url = (String)o;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		
		File file = new File(System.getProperty("user.dir")+"/download/");
		if(!file.exists())
			file.mkdir();
		
		File tempfile = new File(System.getProperty("user.dir")+"/download/"+DigestUtils.md5Hex(url)+".jpg");
		if(tempfile.exists())
			return;
		try {
			
			CloseableHttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			if(entity.getContentLength()<0){
				logger.debug("文件下载失败{}",url);
				return;
			}
			
			FileOutputStream fos = new FileOutputStream(tempfile);
			BufferedInputStream is = new BufferedInputStream(entity.getContent());
			byte[] buf = new byte[1024];
			int len = 0;
			while((len = is.read(buf)) != -1){
				fos.write(buf,0,len);
			}
			fos.flush();
			fos.close();
			is.close();
			
		} catch (ClientProtocolException e) {
			logger.debug("data client protocol error,{}",e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug("data io error,{}",e.getMessage());
		}
		//this.runNext(o);		//启动下一个过滤器
	}

}
