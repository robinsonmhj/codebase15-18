package com.tmghealth.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;



import java.util.Map;

import org.apache.log4j.Logger;

public class GreenplumDAOImp  {
	
	private static Logger log=Logger.getLogger(GreenplumDAOImp.class);

	
	
	
	
	public int[] executeMultipleQuery(String sql){
		
		
		//long start=System.currentTimeMillis();
		Connection conn=null;
		PreparedStatement ps=null;
		String single=null;
		int[] results=null;
		
		try{
			conn=ConnectionJDBC.getConnection();
			conn.setAutoCommit(false);
			String[] sqls=sql.split(";");
			int count=sqls.length;
			results= new int[count];
			for(int i=0;i<count;i++){
				single=sqls[i];
				ps=conn.prepareStatement(single);
				results[i]=ps.executeUpdate();
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
