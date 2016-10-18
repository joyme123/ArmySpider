package com.myway5.www.privateConstruct;

public class Test {
	private int a = 0;
	private Test(){
		setA(1);
	}
	
	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
	
	
	
	
	
	public static void main(String[] args){
		Test test = new Test();
		System.out.println(test.getA());		//输出1，私有的构造方法仍然会被执行
	}


}