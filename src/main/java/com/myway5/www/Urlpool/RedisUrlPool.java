package com.myway5.www.Urlpool;

import java.io.IOException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 使用redis数据库进行持久化的类，同时也可以通过redis数据进行分布式的爬虫开发
 * redis采用的是单进程，单线程模式，多个客户端的访问会变成队列形式的访问，因此不存在多客户端之间
 * 并发访问的冲突问题，但是单个客户端的多连接之间可能会导致连接问题，所以在读写时需要池化,并且保证获取
 * Jedis对象时是synchronized
 * 
 * redis数据库中维持一个集合(set,set中字符串唯一)和一个列表(linked-list)
 * totalSet:所有的的url，用来保证加入的url不会重复
 * leftList:用来加入新的url(rpush)和弹出要使用的url(lpop)
 * @author jiang
 *
 */
public class RedisUrlPool extends AbstUrlPool implements IPersistence{
	private JedisPool jedisPool;
	private Jedis jedis = null;
	private static RedisUrlPool urlPool;
	private static boolean empty;
	
	private RedisUrlPool(String host,int port){
		jedisPool = new JedisPool(new JedisPoolConfig(),host,port);
		jedis = jedisPool.getResource();
		empty = false;
	}
	
	public static RedisUrlPool getInstance(String host,int port){
		if(urlPool == null){
			urlPool = new RedisUrlPool(host, port);
			
		}
		return urlPool;
	}
	
	/**
	 * 多线程环境下创建安全
	 * 这里的多线程指的是多个线程同时获取这个类的实例时
	 * @return
	 */
	public static RedisUrlPool getThreadSafeInstance(String host,int port){
		return RedisUrlPoolHolder.get(host, port);
	}
	
	public static class RedisUrlPoolHolder{
		public static RedisUrlPool urlPool;
		public static RedisUrlPool get(String host,int port){
			return urlPool = new RedisUrlPool(host, port);
		}
	}
	
	

	@Override
	public void pushWithoutDuplicate(String url) {
		
	}
	/**
	 * 使用redis数据库时会自动对插入的url进行去重，所以这里直接重写父类的push方法即可
	 */
	 public void push(String url){
		try{

			if(!jedis.sismember("totalSet", url)){
				jedis.sadd("totalSet", url);		//如果不存在，就加入
				jedis.rpush("leftList", url);
				leftUrlCount.incrementAndGet();
				empty = false;
			}
		} catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	

	@Override
	synchronized public String pullWithoutUpdateLeftUrlCount() {
		Jedis jedis = null;
		String url = null;
		try{
			jedis = jedisPool.getResource();
			url = jedis.lpop("leftList");
			if(url.equals("nil")){
				empty = true;
				return null;
			}
		} catch (Exception e) {  
            e.printStackTrace();  
        }
		return url;
	}
	
	
	/*
	 * 重写isEmpty()
	 * @see com.myway5.www.Urlpool.AbstUrlPool#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return empty;
	}

	/**
	 * 释放redis的资源
	 */
	public void close() throws IOException {
		jedis.close();
		jedisPool.destroy(); 
	}
}
