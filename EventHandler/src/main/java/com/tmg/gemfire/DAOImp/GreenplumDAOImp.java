package com.tmg.gemfire.DAOImp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;






import com.tmg.gemfire.DAO.CommonDAO;
import com.tmg.gemfire.Util.TmgLogger;



public class GreenplumDAOImp implements CommonDAO {
	
	private Logger log=Logger.getLogger(TmgLogger.LOGGER_NAME);
	
	public List<String> getPrimaryKey(String schemaName, String tableName){
		
		ResultSet rs = null;
		Connection conn=null;
		List<String> primaryList = new ArrayList<String>();
		try{
			conn=ConnectionJDBC.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();

			rs = dbmeta.getPrimaryKeys(null, schemaName, tableName);
			while (rs.next()) {
				// String tName=rsKey.getString("TABLE_NAME");
				String pri = rs.getString("COLUMN_NAME");
				primaryList.add(pri);
			}
		}catch(Exception e){
			
			log.log(Level.SEVERE,"Exception:",e);
			
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				conn.close();
			}catch(Exception e){
				log.log(Level.SEVERE,"Close connection error",e);
			}
			
		}
		
		return primaryList;
		
	}
	
	
	
	
	public int executeUpdate(String sql){
		Connection conn=null;
		PreparedStatement ps=null;
		int count=0;
		try{
			
			conn=ConnectionJDBC.getConnection();
			ps= conn.prepareStatement(sql);
			count=ps.executeUpdate();
			log.info("Sql is "+sql);
		}catch(Exception e){
			
			log.info("Sql is "+sql);
			log.log(Level.SEVERE,"error",e);
			//e.printStackTrace();
			
		}finally{
			
			try{
				ps.close();
				conn.close();
			}catch(Exception e){
				log.log(Level.SEVERE,"close connection error",e);
				//System.out.println("close connection error");
				//e.printStackTrace();
			}
		}
		
		return count;
		
	}
	
	//only used to get the count of a table
	public Map<String,String> execute(String sql) {
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Map<String,String> resultMap=new HashMap<String,String>();
		try{
			conn=ConnectionJDBC.getConnection();
			ps= conn.prepareStatement(sql.toLowerCase());
			rs=ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int count=rsmd.getColumnCount();
			while(rs.next()){
				
				for(int i=1;i<=count;i++){
					String key=rsmd.getColumnName(i);
					String value=rs.getString(key);
					resultMap.put(key, value);
				}
				break;
			}
				
			//log.info("sql is "+sql+"result is "+total);
			//System.out.println("Sql is "+sql+" result is "+total);
		}catch(Exception e){
			
			log.log(Level.SEVERE,"error",e);
			log.info("sql is "+sql);
			//e.printStackTrace();
			
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				ps.close();
				conn.close();
			}catch(Exception e){
				log.log(Level.SEVERE,"close connection error",e);
				//System.out.println("close connection error");
				//e.printStackTrace();
			}
			
		}
		
		return resultMap;
	}
	
	
	

	
	
	
	

}
