package com.myway5.www.privateConstruct;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StaticTest1 {
	public int i;
	public StaticTest test;
	
	public void setI(StaticTest clazz){
		test = clazz;
		test.i++;
	}
	
	public void setI(Class clazz){
		try {
			Method method = clazz.getMethod("getInstance", null);
			test = (StaticTest)method.invoke(null, null);
			test.i++;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		StaticTest1 test1 = new StaticTest1();
		//test1.setI(StaticTest.class);
		test1.setI(StaticTest.getInstance());
		System.out.println(test1.test.i);
		System.out.println(StaticTest.getInstance().i);
	}
}
