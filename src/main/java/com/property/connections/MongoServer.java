package com.property.connections;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@Component("mongoServer")
@Qualifier("default")
@Scope("singleton")
public class MongoServer {

	private Morphia morphia = null;
	private MongoClient connection = null;
	
	public MongoServer() throws UnknownHostException
	{
		createMongoInstance();
		createMorphiaInstance();
	}
	
	
	@PostConstruct
	public void init()
	{
	}
	
	@PreDestroy
	public void destroy()
	{
		connection.close();
	}

	
	public Morphia getMorphia() {
		createMorphiaInstance();
		return morphia;
	}

	public MongoClient getConnection() throws UnknownHostException {
		createMongoInstance();
		return connection;
	}

	
	public void setConnection(MongoClient connection) {
		this.connection = connection;
	}

	public DB getDB(String database) throws MongoException, UnknownHostException {
		DB db = this.getConnection().getDB(database);
		boolean auth = db.authenticate("root", "root".toCharArray());
		if (!auth)
			throw new MongoException("MongoServer: Internal System Error");
		
		return db;
	}

	public DBCollection getCollection(String database, String collection) throws MongoException, UnknownHostException 
	{
		DB db = getDB(database);
		DBCollection localCollection = db.getCollection(collection);
		if (localCollection == null)
			throw new MongoException("MongoServer: Internal System Error");
		
		return localCollection;
	}

	public DBCollection GetRequestCollection()
	{
		try {
			return this.getCollection("Auditing", "RequestLogs");
		} catch (Exception e) {
			System.out.println("-- Mongo Error --");
			System.out.println("Error: " + e.getMessage());
			System.out.println("StackTrace: ");
			e.printStackTrace();
			return null;
		}
	}
	
	public DBCollection GetResponseCollection()
	{
		try {
			return this.getCollection("Auditing", "ResponseLogs");
		} catch (Exception e) {
			System.out.println("-- Mongo Error --");
			System.out.println("Error: " + e.getMessage());
			System.out.println("StackTrace: ");
			e.printStackTrace();
			return null;
		}
	}

	
	public void createMongoInstance() throws UnknownHostException
	{
		//TODO:: Get Instance from config file
		if (connection == null)
			connection = new MongoClient( "192.168.2.108" , 27017 );
	}
	
	public void createMorphiaInstance()
	{
		if (morphia == null)
			morphia = new Morphia();
	}
		
}
