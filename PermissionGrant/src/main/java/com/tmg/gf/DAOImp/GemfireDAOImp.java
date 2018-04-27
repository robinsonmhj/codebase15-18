package com.tmg.gf.DAOImp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;





import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;











import com.tmg.gf.DAO.CommonDAO;



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
		}catch(Exception e){

			log.error("Exception ",e);
			log.info("Failed procedure is "+procedure);
			
			
		}finally{
			try{
				if(cs!=null)
					cs.close();
				if(conn!=null)
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
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			}catch(Exception e){
				log.error("close connection error",e);
			}
			
		}
		
		return total;
		
	}


public int update(String sql) throws SQLException{
	
	Connection conn=null;
	PreparedStatement ps=null;
	int count=0;
	try{
		conn=dataSource.getConnection();
		ps= conn.prepareStatement(sql);
		count=ps.executeUpdate();
	}catch(Exception e){
		log.error("error",e);
		log.info("sql is "+sql);
		
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
	
	return count;
	
}

	
	
public List<String> getTablesBySchema(String schema,String pattern){
	
	List<String> tableList = new ArrayList<String>();
	
	
	Connection conn=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	String sql=null;
	if(pattern==null||pattern.isEmpty())
		sql="select tablename from sys.systables where tabletype=? and tableschemaname=?";
	else
		sql="select tablename from sys.systables where tabletype=? and tableschemaname=? and tablename "+pattern;
	try{
		conn=dataSource.getConnection();
		ps= conn.prepareStatement(sql);
		ps.setString(1,"T");
		ps.setString(2,schema.toUpperCase());
		rs=ps.executeQuery();
		while(rs.next()){
			String table=rs.getString(1);
			tableList.add(table);
		}
			
	}catch(Exception e){
		
		log.error("error",e);
		log.info("sql is "+sql);
		
	}finally{
		
		try{
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			if(conn!=null)
				conn.close();
		}catch(Exception e){
			log.error("close connection error",e);
		}
		
	}

	return tableList;

}


public List<String> getSchema(){
	
	List<String> schemaList= new ArrayList<String>();
	
	Connection conn=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	String sql="select schemaName from sys.sysschemas";
	try{
		conn=dataSource.getConnection();
		ps= conn.prepareStatement(sql);
		rs=ps.executeQuery();
		while(rs.next()){
			String schema=rs.getString(1);
			schemaList.add(schema);
		}
			
	}catch(Exception e){
		
		log.error("error",e);
		log.info("sql is "+sql);
		
	}finally{
		
		try{
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			if(conn!=null)
				conn.close();
		}catch(Exception e){
			log.error("close connection error",e);
		}
		
	}

	
	return schemaList;
	
	
}


public List<String> getFunctionList(String schemaName){
	
List<String> functionList= new ArrayList<String>();
	
	Connection conn=null;
	ResultSet rs=null;
	try{
		conn=dataSource.getConnection();
		DatabaseMetaData meta=conn.getMetaData();
		rs=meta.getFunctions(null, schemaName, "%");
		while(rs.next()){
			String function=rs.getString(3);
			functionList.add(function);
		}
			
	}catch(Exception e){
		
		log.error("error",e);
		
	}finally{
		
		try{
			if(rs!=null)
				rs.close();
			if(conn!=null)
				conn.close();
		}catch(Exception e){
			log.error("close connection error",e);
		}
		
	}

	
	return functionList;
	
	
	
}


public int[] executeMultipleQuery(String sql){
	
	
	//long start=System.currentTimeMillis();
	Connection conn=null;
	PreparedStatement ps=null;
	String single=null;
	int[] results=null;
	
	try{

		conn=dataSource.getConnection();
		conn.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
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
	
	
	//long end=System.currentTimeMillis();
	
	//System.out.println("Using time:"+(end-start));
	
	return results;
	
}

public List<String> getProcedureList(String schemaName){
	
	List<String> procedureList= new ArrayList<String>();
	
	Connection conn=null;
	ResultSet rs=null;
	try{
		conn=dataSource.getConnection();
		DatabaseMetaData meta=conn.getMetaData();
		rs=meta.getProcedures(null, schemaName, "%");
		while(rs.next()){
			String procedure=rs.getString(3);
			procedureList.add(procedure);
		}
			
	}catch(Exception e){
		
		log.error("error",e);
		
	}finally{
		
		try{
			if(rs!=null)
				rs.close();
			if(conn!=null)
				conn.close();
		}catch(Exception e){
			log.error("close connection error",e);
		}
		
	}

	
	return procedureList;
	
	
}




/**
 * @param schemaName
 * @return
 */
public List<String> getFunctionListBySchemaName(String schemaName) {
	// TODO Auto-generated method stub
	return null;
}

}
