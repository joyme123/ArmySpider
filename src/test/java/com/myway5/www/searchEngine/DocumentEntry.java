package com.myway5.www.searchEngine;

import java.util.Calendar;
import java.util.Date;

import org.bson.Document;

public class DocumentEntry {
	private MMongoClient mongoClient;
	private RedisClient redisClient;
	private static DocumentEntry documentEntry = null;
	public static DocumentEntry getInstance(){
		
		if(documentEntry == null){
			synchronized(DocumentEntry.class){
				if(documentEntry == null){
					documentEntry = new DocumentEntry();
				}
			}
		}
		
		return documentEntry;
	}

	private DocumentEntry(){
		this.mongoClient = new MMongoClient(Config.MONGO_HOST, Config.MONGO_PORT);
		this.redisClient = new RedisClient(Config.REDIS_HOST,Config.REDIS_PORT);
	}
	
	public void insertDocument(HtmlDocument document){
		this.mongoClient.insert(
				new Document("_id",document.getId())
				.append("title", document.getTitle())
				.append("url", document.getUrl())
				.append("content", document.getContent())
				);
		redisClient.rpush(document.getId());
	}
	
}
