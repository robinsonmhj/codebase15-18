/**
 * 
 */
package com.appannie.stock.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


/**
 * @author Haojie Ma
 * @date Jun 4, 2016
 */
@Repository("StockDAO")
public class StockDaoImpl implements CommonDAO{
	
	private static Logger log= Logger.getLogger(StockDaoImpl.class);
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Map<Integer,String> getSymbol(){
		Map<Integer,String> map= new HashMap<Integer,String>();
		ResultSet rs = null;
		Connection conn=null;
		PreparedStatement ps=null;
		String sql="select id,symbol from company";
		try{
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				int id=rs.getInt(1);
				String symbol=rs.getString(2);
				map.put(id, symbol);
			}
		}catch(Exception e){
			log.info("Exception:",e);
		}finally{
			try{
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		return map;
		
		
	}
	public Map<Integer,BigDecimal> getLastChange(){
		Map<Integer,BigDecimal> map= new HashMap<Integer,BigDecimal>();
		ResultSet rs = null;
		Connection conn=null;
		PreparedStatement ps=null;
		String sql="select c.company_id,c.change_price from transaction c join (select company_id,max(insertTime) as insertTime from transaction group by company_id) t on c.company_id=t.company_id where c.insertTime=t.insertTime";
		try{
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				int id=rs.getInt(1);
				BigDecimal change=rs.getBigDecimal(2);
				map.put(id, change);
			}
		}catch(Exception e){
			log.info("Exception:",e);
		}finally{
			try{
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		return map;
		
		
	}
	
	
	public int importFromFile(String tableName,String file){
		int count=-1;
		Connection conn=null;
		PreparedStatement ps=null;
		String sql="LOAD DATA LOCAL INFILE ? into table "+tableName+" FIELDS TERMINATED BY ','";
		try{
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sql);
			ps.setString(1, file);
			count=ps.executeUpdate();
		}catch(Exception e){
			log.info("failed sql"+ps.toString(),e);
		}finally{
			try{
				if(ps!=null)
					ps.close();
				conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		return count;
		
		
		
	}
	
	
	
	public Map<String,String> addressInfo(){
		
		Map<String,String> addressMap= new HashMap<String,String>();
		ResultSet rs = null;
		Connection conn=null;
		PreparedStatement ps=null;
		String sql="select symbol,address1,address2 from company";
		try{
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				String symbol=rs.getString(1);
				String address1=rs.getString(2);
				String address2=rs.getString(3);
				addressMap.put(symbol,address1+","+address2);
			}
		}catch(Exception e){
			log.info("Exception:",e);
		}finally{
			try{
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		return addressMap;
		
		
	}
	

}


