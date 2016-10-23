package com.myway5.www.Urlpool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FileUrlPool extends AbstUrlPool implements IPersistence,Cloneable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static FileUrlPool urlPool = null;
	private ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<String>();
	private BufferedWriter urlWriter;
	private BufferedWriter cursorWriter;
	private AtomicInteger cursor;
	private String path = System.getProperty("user.dir");
	private ScheduledExecutorService service;
	/**
	 * 私有的构造方法，用来在类初始化时默认执行一些操作
	 */
	private FileUrlPool(){
		
	}
	
	/**
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
	/**
	 * 多线程环境下创建安全
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
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+"cursor.txt"));
			String line = null;
			while(( line = reader.readLine())!=null);
			if(line == null)
				cursor = new AtomicInteger(0);
			else
				cursor = new AtomicInteger(Integer.parseInt(line));
			reader.close();
		} catch (FileNotFoundException e) {
			File file = new File(path+"cursor.txt");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				logger.error("文件创建失败:{}",e1.getMessage());
			}
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
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+"url.txt"));
			String line;
			int lineNum = 0;
			if(cursor == null){						//如果游标为空，则重新创建url.txt
				File file = new File(path+"url.txt");
				file.createNewFile();
				cursor = new AtomicInteger(0);
			}else{
				while((line = reader.readLine())!=null){
					if(lineNum >= cursor.get()){
						urlQueue.offer(line);		//找到未处理的url行，读入队列
					}else{
						pushInRemover(line); 		//已经处理的url行也要读入，来维持完整的remover
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			File file = new File(path+"url.txt");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				logger.error("url.txt创建失败:{}",e1.getMessage());
			}
			logger.debug("url.txt不存在,重新创建:{}",e.getMessage());
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
		try {
			readFile(); 		//从文件中读入数据，如果不存在则创建他们
			
			urlWriter = new BufferedWriter(new FileWriter(path+"url.txt"));		//获取url写对象
			cursorWriter = new BufferedWriter(new FileWriter(this.path+"cursor.txt"));//获取cursor写对象
			
			flush();
		} catch (IOException e) {
			logger.error("url或者cursor.txt读取异常:{}",e.getMessage());
		}
	}
	
	/**
	 * 将当前缓存写入文件
	 */
	public void flush() {
		Runnable runnable = new Runnable(){

			public void run() {
				try {
					urlWriter.flush();
					cursorWriter.flush();
				} catch (IOException e) {
					logger.error("文件写入异常"+e.getMessage());
				}
			}
		};
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.MILLISECONDS);
	}

	@Override
	public void pushWithoutDuplicate(String url) {
		urlQueue.offer(url);
		try {
			urlWriter.append(url);
		} catch (IOException e) {
			logger.error("入栈IO异常:{}",e.getMessage());
		}
		totalCount.incrementAndGet();
		leftUrlCount.incrementAndGet();
	}

	@Override
	public String pull() {
		leftUrlCount.decrementAndGet();
		cursor.incrementAndGet();
		return urlQueue.poll();
	}
	
	/**
	 * 关闭文件写入对象，关闭周期线程
	 * @throws IOException
	 */
	public void close() throws IOException{
		urlWriter.close();
		cursorWriter.close();
		service.shutdown();
	}
}
