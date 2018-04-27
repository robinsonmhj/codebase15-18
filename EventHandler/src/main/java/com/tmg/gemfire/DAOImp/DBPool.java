/**
 * 
 */
package com.tmg.gemfire.DAOImp;

/**
 * @author Haojie Ma
 * @date Apr 22, 2015
 */

import java.sql.*;

import org.apache.commons.dbcp.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tmg.core.Properties;
import com.tmg.gemfire.Util.TmgLogger;

public class DBPool {

	private static BasicDataSource connectionPool;
	private static Logger log=Logger.getLogger(TmgLogger.LOGGER_NAME);
	static {
		System.out.println("Static BasicDataSource");
		try{
			Class.forName(Properties.getProperty("jdbc.driverClassName.greenplum"));
		}catch(Exception e){
			log.log(Level.SEVERE,"DBPool exception,Didn't find JDBC driver",e);
			//System.out.println("DBPool exception,Didn't find JDBC driver");
			//e.printStackTrace();
			
			
		}
		
		connectionPool = new BasicDataSource();
		connectionPool.setUsername(Properties.getProperty("jdbc.username.greenplum"));
		connectionPool.setPassword(Properties.getProperty("jdbc.password.greenplum"));
		connectionPool.setDriverClassName(Properties.getProperty("jdbc.driverClassName.greenplum"));
		connectionPool.setUrl(Properties.getProperty("jdbc.url.greenplum"));
		connectionPool.setInitialSize(Integer.valueOf(Properties.getProperty("jdbc.initial.greenplum")));
		connectionPool.setMaxActive(Integer.valueOf(Properties.getProperty("jdbc.max.greenplum")));
		
	}

	public static Connection getConnection() {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
		} catch (Exception e) {

			log.log(Level.SEVERE,"DB Connection Pool, get connection error",e);
			//System.out.println("sys:DB Connection Pool, get connection error");
			//e.printStackTrace();

		}


		return connection;

	}

}
