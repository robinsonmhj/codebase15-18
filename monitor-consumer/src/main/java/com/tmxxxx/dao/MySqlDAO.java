package com.tmxxxx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component("MySqlDAO")
public class MySqlDAO {
	private static Logger log= Logger.getLogger(MySqlDAO.class);
	@Resource(name="dataSource")
	private DataSource dataSource;
	
	

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
		//long end=System.currentTimeMillis();
		
		//System.out.println("Using time:"+(end-start));
		
		return results;
		
	}



public int executeSingleQuery(String sql){
	//long start=System.currentTimeMillis();
	Connection conn=null;
	PreparedStatement ps=null;
	int result=0;
	try{
		conn=dataSource.getConnection();
		ps=conn.prepareStatement(sql);
	    result=ps.executeUpdate();
	}catch(Exception e){
		log.info("failed sql is "+sql,e);
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
	return result;
	
}

}



