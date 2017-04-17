package com.myway5.www.searchEngine;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class HtmlDocument {
	private String id;
	private String title;
	private String url;
	private String content;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public HtmlDocument(String title,String url,String content){
		StringBuilder sb = new StringBuilder();
		sb.append(Config.CODE);
		sb.append(Calendar.getInstance().getTimeInMillis());
		try {
			sb.append(byteArrayToHex(MessageDigest.getInstance("MD5").digest(url.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.id = sb.toString();
		this.title = title;
		this.url = url;
		this.content = content;
	}
	
	private String byteArrayToHex(byte[] byteArray) {  
		  
		   // 首先初始化一个字符数组，用来存放每个16进制字符  
		   char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
		  
		   // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
		   char[] resultCharArray =new char[byteArray.length * 2];    
		  
		   // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去    
		   int index = 0;  
		  
		   for (byte b : byteArray) {  
		      resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];    
		      resultCharArray[index++] = hexDigits[b& 0xf];  
		   }  
		  
		  
		  
		   // 字符数组组合成字符串返回  
		   return new String(resultCharArray);  

	}
	
	
}
