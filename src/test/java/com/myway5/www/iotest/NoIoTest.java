package com.myway5.www.iotest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoIoTest {
	public static void main(String[] args){
		Date date = Calendar.getInstance().getTime();
		long start = Calendar.getInstance().getTimeInMillis();
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		String path = System.getProperty("user.dir")+"/src/test/java/com/myway5/www/iotest/io.txt";
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		final FileWriter temp = writer;
		for(int i = 0; i < 1000000; i++){
			final int num = i;
			executorService.submit(new Runnable() {
				
				public void run() {
					try {
						temp.append("hello world\n"+String.valueOf(num));
						if(num % 100 == 0){			//每100次flush一次，模拟多线程环境下定时写入
							//temp.flush();			//去除flush操作，最后再flush
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
		}
		try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date date2 = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long stop = Calendar.getInstance().getTimeInMillis();
		System.out.println("开始时间:" + format.format(date));
		System.out.println("终止时间:" + format.format(date2));
		System.out.println("总耗时:" + (stop - start));
	}
}
