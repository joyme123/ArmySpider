package com.myway5.www.iotest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static void main(String[] args){
		try {
			System.out.println( new String(MessageDigest.getInstance("MD5").digest("http://baidu.com".getBytes())));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
