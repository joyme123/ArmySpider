package com.myway5.www.searchEngine;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MMongoClient {
	private MongoClient mongoClient;
	private MongoCollection<Document> collection;
	
	public MMongoClient(String host,int port){
		mongoClient = new MongoClient( host,port );
		MongoDatabase database = mongoClient.getDatabase("search");
		collection = database.getCollection("document");
	}
	
	public void insert(Document document){
		collection.insertOne(document);
	}
	
	public void close(){
		this.mongoClient.close();
	}
}
