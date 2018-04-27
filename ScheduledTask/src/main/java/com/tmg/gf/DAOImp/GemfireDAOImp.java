package com.tmg.gf.DAOImp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;




import com.tmg.gf.DAO.CommonDAO;


@Component
public class GemfireDAOImp implements CommonDAO {
	
	private static Logger log= Logger.getLogger(GemfireDAOImp.class);
	@Resource(name="dataSourceGemfireThin")
	private DataSource dataSource;
	
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	 

	
	
	public boolean callProcedure(String procedure){
		
		Connection conn=null;
		CallableStatement cs=null;
		boolean f=false;
		try{
			conn=dataSource.getConnection();
			cs=conn.prepareCall(procedure);
			f=cs.execute();
			log.debug(procedure);
		}catch(Exception e){
			
		
			
			log.error("Exception ",e);
			
			
		}finally{
			try{
				if(cs!=null)
					cs.close();
				conn.close();
				
				
			}catch(Exception e){
				
				log.error("close conneciton error",e);
			}
			
			
		}
		
		return f;
		
		
		
		
		
	}
	

	
public int execute(String sql) throws SQLException{
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int total=0;
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next())
				total=rs.getInt(1);
			log.debug("sql is "+sql+"result is "+total);
		}catch(Exception e){
			
			log.error("error",e);
			log.info("sql is "+sql);
			
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				ps.close();
				conn.close();
			}catch(Exception e){
				log.error("close connection error",e);
			}
			
		}
		
		return total;
		
	}
	
	

}
