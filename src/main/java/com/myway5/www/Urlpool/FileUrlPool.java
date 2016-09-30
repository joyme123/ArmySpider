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

public class FileUrlPool extends AbstUrlPool implements IPersistence{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static FileUrlPool urlPool = null;
	private ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<String>();
	private BufferedWriter urlWriter;
	private BufferedWriter cursorWriter;
	private AtomicInteger cursor;
	private String path = System.getProperty("user.dir");
	private FileUrlPool(){
		
	}
	
	public static FileUrlPool getInstance(){
		if(urlPool == null){
			urlPool = new FileUrlPool();
		}
		return urlPool;
	}
	
	private void readCursor(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+"url.txt"));
			String line;
			while(( line = reader.readLine())!=null);
			cursor = new AtomicInteger(Integer.parseInt(line));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPath(String path){
		this.path = path;
		try {
			urlWriter = new BufferedWriter(new FileWriter(path+"url.txt"));
			cursorWriter = new BufferedWriter(new FileWriter(this.path+"cursor.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void flush() {
		try {
			urlWriter.flush();
			cursorWriter.flush();
		} catch (IOException e) {
			logger.error("文件写入异常"+e.getMessage());
		}
		
	}

	@Override
	public void pushWithoutDuplicate(String url) {
		// TODO Auto-generated method stub
		super.pushWithoutDuplicate(url);
	}

	@Override
	public String pull() {
		// TODO Auto-generated method stub
		return super.pull();
	}
	
	
}
