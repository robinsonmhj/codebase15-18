/**
 * 
 */
package com.tmg.gemfire.DAOImp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tmg.core.Properties;
import com.tmg.gemfire.Util.TmgLogger;

/**
 * @author Haojie Ma
 * @date Apr 23, 2015
 */
public class ConnectionJDBC {
	
	//private static Connection connection;
	private static  Logger log=Logger.getLogger(TmgLogger.LOGGER_NAME);
	
	
	
	public static Connection getConnection(){
		
		Connection connection=null;
		
		try{
			String driver=Properties.getProperty("jdbc.driverClassName.greenplum");
			String url=Properties.getProperty("jdbc.url.greenplum");
			String user=Properties.getProperty("jdbc.username.greenplum");
			String password=Properties.getProperty("jdbc.password.greenplum");
			
			//System.out.println("driver:"+driver+",url:"+url+",user:"+user+",password:"+password);
			
			Class.forName(driver);
			connection = DriverManager.getConnection(url,user,password);
			
			//there must be something wrong with the Properties class
			//Class.forName("org.postgresql.Driver");
			//connection = DriverManager.getConnection("jdbc:postgresql://192.168.1.10:5432/postgres","robinson","hadoop");
			
			
		}catch(Exception e){
			
			
			log.log(Level.SEVERE,"create connection error", e);
			//System.out.print("create connection error\n");
			//e.printStackTrace();
			
		}
		
		return connection;
		
	}

}


