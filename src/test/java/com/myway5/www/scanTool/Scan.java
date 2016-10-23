package com.myway5.www.scanTool;

import java.util.Date;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.myway5.www.Monitor.MonitorEngineAgent;
import com.myway5.www.Spider.ProcessSpider;
import com.myway5.www.SpiderManager.AbstSpiderManager;
import com.myway5.www.Urlpool.FileUrlPool;

public class Scan extends AbstSpiderManager{

	public void manage() {
		// TODO Auto-generated method stub
		
	}
	
	public static void init(){
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout(20, 20, 20));
		
		JLabel urlLabel = new JLabel("站点url");
		final JTextField urlField = new JTextField(30);
		urlField.setText("http://www.ahchsz.com/");
		
		JLabel limitLabel = new JLabel("限制正则");
		final JTextField limitField = new JTextField(30);
		limitField.setText("http://.*\\.ahchsz\\.com/.*");
		
		JLabel threadLable = new JLabel("开启线程数");
		final JTextField threadField = new JTextField(4);
		JButton button = new JButton("开始抓取");
		final JTextArea area = new JTextArea(20, 20);
		area.setAutoscrolls(true);
		frame.add(urlLabel);
		frame.add(urlField);
		frame.add(limitLabel);
		frame.add(limitField);
		frame.add(threadLable);
		frame.add(threadField);
		frame.add(button);
		frame.add(area);
		frame.setBounds(40, 40, 500, 500);
		frame.setVisible(true);
		
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				final String startUrl = urlField.getText();
				area.setText("加入链接:"+startUrl);
				final String limit = limitField.getText();
				final int threadNum = Integer.parseInt(threadField.getText());
				
				new Thread(new Runnable() {
					
					public void run() {
						start(startUrl, limit, threadNum,area);
						
					}
				}).start();
				
			}
		});
	}	
	
	public static void start(String startUrl,String limit,int threadNum,JTextArea area){
		Date date = Calendar.getInstance().getTime();
		long start = Calendar.getInstance().getTimeInMillis();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		//使用文件实现持久化存储
		FileUrlPool urlPool = FileUrlPool.getInstance();
		urlPool.setPath("D:\\spiderFile");
		new MonitorEngineAgent(urlPool).start();
		System.out.println("start");
		FirstFilter firstFilter = new FirstFilter(area);
		SecondFilter secondFilter = new SecondFilter();
		firstFilter.setNextFilter(secondFilter);
		
		
		ProcessSpider processSpider = new ProcessSpider();			//不设置targetUrl则会将所有的url加入url队列中
		processSpider.setLimitation(limit);							//防止扫描到网站外部 "http://.*\.dhu\.edu\.cn/.*"
		processSpider.setFilterSpider(firstFilter);
		

		threadNum = 5;			//开启5个线程
		Scan scan = new Scan();
					
		scan.setProcessSpider(processSpider)
		    		 .thread(threadNum)
		    		 .setUrlPool(urlPool)
		    		 .setStartUrl(startUrl)	//"http://www.dhu.edu.cn"
		    		 .run();
		Date date2 = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long stop = Calendar.getInstance().getTimeInMillis();
		System.out.println("开始时间:" + format.format(date));
		System.out.println("终止时间:" + format.format(date2));
		System.out.println("总耗时:" + (stop - start)+"ms");
	}
	
	public static void main(String[] args){
		init();
	}

}
