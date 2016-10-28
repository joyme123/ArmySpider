package com.myway5.www.privateConstruct;

public class StaticTest1 {
	public int i;
	
	public void setI(int i){
		this.i = i;
		this.i++;
	}
	
	public static void main(String[] args){
		StaticTest1 test1 = new StaticTest1();
		test1.setI(StaticTest.i);
		System.out.println(test1.i);
		System.out.println(StaticTest.i);
	}
}
