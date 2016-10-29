package com.myway5.www.privateConstruct;

/**
 * 测试静态变量的传递
 * @author jiang
 *
 */
public class StaticTest {
	public static StaticTest st;
	public int i = 0;
	public static StaticTest getInstance(){
		if(st == null){
			st = new StaticTest();
		}
		return st;
	}
}
