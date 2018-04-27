/**
 * 
 */
package com.tmxxx.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @author Haojie Ma
 * @date Sep 10, 2015
 */

@Component("gp")
public class GreenplumDAOImp{

	private static Logger log = Logger.getLogger(GreenplumDAOImp.class);

	@Autowired
	@Qualifier("dataSourceGreenplum")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	


public int[] executeMultipleQuery(String sql){
		
		
		//long start=System.currentTimeMillis();
		Connection conn=null;
		PreparedStatement ps=null;
		String single=null;
		int[] results=null;
		try{
			conn=dataSource.getConnection();
			conn.setAutoCommit(false);
			String[] sqls=sql.split(";");
			int count=sqls.length;
			results= new int[count];
			for(int i=0;i<count;i++){
				single=sqls[i];
				ps=conn.prepareStatement(single);
				//ps.setQueryTimeout(60*40);
				results[i]=ps.executeUpdate();
				log.debug(single+", the result is "+results[i]);
			}
			
			conn.commit();
			
			
		}catch(Exception e){
			
			if(conn!=null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.info("Roll back error",e);
				}
			
			log.info("failed sql is "+single,e);
			
			
		}finally{
			
			try{
				if(ps!=null)
					ps.close();
					
				if(conn!=null)
					conn.close();	
			}catch(Exception e){
				
				log.error("close connection error",e);
			}
			
			
		}
		
		
		return results;
		
	}

}
