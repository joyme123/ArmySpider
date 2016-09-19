package com.myway5.www.Monitor;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorEngineAgent {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public void start(){
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try{
			ObjectName mxBeanName = new ObjectName("com.myway5.www.Monitor:type=MonitorEngine");
			MonitorEngineMXBean mxbean = new MonitorEngine();
	        mbs.registerMBean(mxbean, mxBeanName);
		}catch(Exception e){
			logger.debug("monitor error occurred{}",e.getMessage());
		}
	}
}
