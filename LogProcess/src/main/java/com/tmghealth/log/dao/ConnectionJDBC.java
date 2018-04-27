/**
 * 
 */
package com.tmghealth.log.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;



/**
 * @author Haojie Ma
 * @date Apr 23, 2015
 */
public class ConnectionJDBC {
	
	//private static Connection connection;
	private static  Logger log=Logger.getLogger(ConnectionJDBC.class);
	
	
	
	public static Connection getConnection(){
		
		Connection connection=null;
		
		try{
			String driver="org.postgresql.Driver";
			String url="jdbc:postgresql://gpv2devsegedw1.xxxx.com:5433/devtmgdw_2";
			String user="xxxx";
			String password="";
			
			//System.out.println("driver:"+driver+",url:"+url+",user:"+user+",password:"+password);
			
			Class.forName(driver);
			connection = DriverManager.getConnection(url,user,password);
			
			
			
		}catch(Exception e){
			
			
			log.info("create connection error", e);
			//System.out.print("create connection error\n");
			//e.printStackTrace();
			
		}
		
		return connection;
		
	}

}


