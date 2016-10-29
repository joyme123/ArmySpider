package com.myway5.www.scanTool;

import java.util.Date;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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
		frame.setLayout(new FlowLayout());
		
		JLabel urlLabel = new JLabel("站点url");
		final JTextField urlField = new JTextField(30);
		//urlField.setText("http://localhost:8080/sql/Less-1/");
		urlField.setText("http://www.gxcme.edu.cn/");
		
		JLabel limitLabel = new JLabel("限制正则");
		final JTextField limitField = new JTextField(30);
		//limitField.setText("http://localhost:8080/.*");
		limitField.setText("http://.*.gxcme.edu.cn/.*");
		
		JLabel threadLable = new JLabel("开启线程数");
		final JTextField threadField = new JTextField(4);
		threadField.setText("5");
		
		JButton button = new JButton("开始抓取");
		button.setPreferredSize(new Dimension(160, 80));
		button.setFont(new Font("幼圆", 1, 20));
		
		//正常网页
		final JTextPane area = new JTextPane();
		area.setAutoscrolls(true);
		area.setForeground(Color.BLUE);
		area.setEditable(false); // 不可录入
		area.setPreferredSize(new Dimension(460, 300));
		
		//可注入网页
		final JTextPane badArea = new JTextPane();
		badArea.setAutoscrolls(true);
		badArea.setForeground(Color.RED);
		badArea.setEditable(false); // 不可录入
		badArea.setPreferredSize(new Dimension(460, 300));
		
		frame.add(urlLabel);
		frame.add(urlField);
		frame.add(limitLabel);
		frame.add(limitField);
		frame.add(threadLable);
		frame.add(threadField);
		frame.add(area);
		frame.add(badArea);
		frame.add(button);
		frame.setBounds(40, 40, 1000, 500);
		frame.setVisible(true);
		
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				final String startUrl = urlField.getText();
				area.setText("加入链接:"+startUrl);
				final String limit = limitField.getText();
				final int threadNum = Integer.parseInt(threadField.getText());
				
				new Thread(new Runnable() {
					
					public void run() {
						start(startUrl, limit, threadNum,area,badArea);
						
					}
				}).start();
				
			}
		});
	}	
	
	public static void start(String startUrl,String limit,int threadNum,JTextPane area,JTextPane badArea){
		Date date = Calendar.getInstance().getTime();
		long start = Calendar.getInstance().getTimeInMillis();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		//使用文件实现持久化存储
		FileUrlPool urlPool = FileUrlPool.getInstance();
		urlPool.setPath("/home/jiang/spiderFile");
		new MonitorEngineAgent(urlPool).start();
		System.out.println("start");
		FirstFilter firstFilter = new FirstFilter(area,badArea);
		
		
		ProcessSpider processSpider = new ProcessSpider();			//不设置targetUrl则会将所有的url加入url队列中
		processSpider.setLimitation(limit);							//防止扫描到网站外部 "http://.*\.dhu\.edu\.cn/.*"
		processSpider.setFilterSpider(firstFilter);
		

		Scan scan = new Scan();
					
		scan.setProcessSpider(processSpider)
		    		 .setStartUrl(startUrl)	//"http://www.dhu.edu.cn"
		    		 .setUrlPool(urlPool)
		    		 .thread(threadNum)
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
