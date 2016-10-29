package com.myway5.www.Urlpool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 提供了url的持久化存储，其中有两个关键的变量
 * cursor指向当前执行到的url的位置,读取和写入cursor.txt进行更新和保存
 * urlQueue用来保存所有的url,读取和写入url.txt进行更新和保存
 * 其他变量:
 * urlWriter用来写入url
 * cursor用来写入cursor
 * 
 * @author jiang
 *
 */


public class FileUrlPool extends AbstUrlPool implements IPersistence,Cloneable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static FileUrlPool urlPool = null;
	private ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<String>();	//线程安全
	private BufferedWriter urlWriter;			//线程安全
	private BufferedWriter cursorWriter;		//线程安全
	private AtomicInteger cursor;				//线程安全
	private int urlWriteCount = 150;			//url获取到多少条时写入
	private String path = System.getProperty("user.dir");
	/**
	 * 私有的构造方法，用来在类初始化时默认执行一些操作
	 */
	private FileUrlPool(){
		
	}
	
	/**
	 * 同时有多个线程实例化时会出问题
	 * 没有进行同步操作，因此多线程环境下创建谨慎使用
	 * @return
	 */
	public static FileUrlPool getInstance(){
		if(urlPool == null){
			urlPool = new FileUrlPool();
		}
		return urlPool;
	}
	
	private static class UrlPoolHolder{
		public static FileUrlPool urlPool = new FileUrlPool();
	}
	
	public void setUrlWriteCount(int num){
		this.urlWriteCount = num;
	}
	
	public int getUrlWriteCount(){
		return this.urlWriteCount;
	}
	/**
	 * 多线程环境下创建安全
	 * 这里的多线程指的是多个线程同时获取这个类的实例时
	 * @return
	 */
	public static FileUrlPool getThreadSafeInstance(){
		urlPool = UrlPoolHolder.urlPool;
		return urlPool;
	}
	
	private void readFile(){
		readCursor();    //先读入下标
		readUrl();       //读入url到队列中
	}
	/**
	 * 读入游标
	 */
	private void readCursor(){
		File file = new File(path+"cursor.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e1) {
				logger.error("文件创建失败:{}",e1.getMessage());
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			int num = 0;
			while(( line = reader.readLine())!=null){
				num = Integer.parseInt(line);
			}
			cursor = new AtomicInteger(num);
			logger.debug(String.valueOf(cursor));
			reader.close();
		} catch (FileNotFoundException e) {
			logger.debug("文件不存在，创建文件:{}",e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IO异常:{}",e.getMessage());
		}
	}
	
	/**
	 * 从Path中读取Url
	 */
	private void readUrl(){
		File file = new File(path+"url.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e1) {
				logger.error("url.txt创建失败:{}",e1.getMessage());
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int lineNum = 0;
			int pos = 0;
			if(cursor == null){
				cursor = new AtomicInteger(0);
			}else{
				pos = cursor.get();
				urlQueue.clear(); 		//因为这里是存持久化文件记录中读取，所以先清空urlQueue
				while((line = reader.readLine())!=null){
					if(lineNum >= pos){
						urlQueue.offer(line);		//找到未处理的url行，读入队列
						leftUrlCount.incrementAndGet();
					}
					pushInRemover(line); 		//所有url行都要读入来维持完整的remover
					lineNum++;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			logger.debug("url.txt不存在:{}",e.getMessage());
		} catch (IOException e) {
			logger.debug("url.txt读取异常:{}",e.getMessage());
		}
		
	}
	
	/**
	 * 设置存储路径，在这同时读入文件存储内容，并设置写入对象
	 * @param path 文件存储的路径
	 */
	public void setPath(String path){
		if(path.endsWith(File.separator))
			this.path = path;
		else
			this.path = path+File.separator;
		File tmp = new File(this.path);
		if(!tmp.exists()){
			tmp.mkdirs();		//如果当前路径不存在，就创建它
		}
		try {
			readFile(); 		//从文件中读入数据，如果不存在则创建他们
			
			urlWriter = new BufferedWriter(new FileWriter(this.path+"url.txt",true));		//获取url写对象
			cursorWriter = new BufferedWriter(new FileWriter(this.path+"cursor.txt",true));//获取cursor写对象
			
		} catch (IOException e) {
			logger.error("url或者cursor.txt读取异常:{}",e.getMessage());
		}
	}
	
	/**
	 * 将当前缓存写入文件
	 */
	synchronized public void flush() {
		try {
			urlWriter.flush();
			cursorWriter.flush();
		} catch (IOException e) {
			logger.error("持久化写入异常");
		}
	}

	@Override
	public void pushWithoutDuplicate(String url) {
		urlQueue.offer(url);
		try {
			urlWriter.append(url+"\n");
		} catch (IOException e) {
			logger.error("入栈IO异常:{}",e.getMessage());
		}
	}
	
	@Override
	public String pullWithoutUpdateLeftUrlCount() {
		int n = cursor.incrementAndGet();
		try {
			cursorWriter.append(String.valueOf(n)+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlQueue.poll();
	}
	
	/**
	 * 关闭文件写入对象，关闭周期线程
	 * @throws IOException
	 */
	public void close() throws IOException{
		urlWriter.close();
		cursorWriter.close();
	}
}
