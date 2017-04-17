package com.myway5.www.searchEngine;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	private JedisPool jedisPool;
	private Jedis jedis;
	public RedisClient(String host,int port){
		jedisPool = new JedisPool(new JedisPoolConfig(),host,port);
		jedis = jedisPool.getResource();
	}
	
	synchronized public void rpush(String value){
		
		jedis.rpush(Config.REDIS_QUEUE, value);
	}
	
	public String lpop(){
		return jedisPool.getResource().lpop(Config.REDIS_QUEUE);
	}
	
	public void close(){
		jedisPool.close();
	}
}
