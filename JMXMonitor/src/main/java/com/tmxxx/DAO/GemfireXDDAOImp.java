/**
 * 
 */
package com.tmxxx.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tmxxx.Bean.Host;
import com.tmxxx.Bean.ServerType;

/**
 * @author Haojie Ma
 * @date Jul 25, 2016
 */

@Component("gfxd")
public class GemfireXDDAOImp implements CommonDAO{
	
	private static Logger log= Logger.getLogger(GemfireXDDAOImp.class);

	@Resource(name="dataSourceGemfireXD")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
	public List<String> getTableList(String groupName){
		
		List<String> list = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement ps=null;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			ps=conn.prepareStatement("select tableschemaname||'.'||tablename from sys.SYSTABLES where tabletype='T' and servergroups=?");
			//ps=conn.prepareStatement("select tableschemaname||'.'||tablename from sys.SYSTABLES where tabletype='T'");
			ps.setString(1, groupName);
			rs=ps.executeQuery();
			//if(rs==null)
				//System.out.println("no table returned");
			while(rs.next()){
				list.add(rs.getString(1));
				//System.out.println("table:"+rs.getString(1));
			}
		}catch(Exception e){
			
			log.error("getTableList error",e);
			
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
			
		return list;
		
		
	}
	
public List<Host> getServers(ServerType type){
		
		List<Host> hostList= new ArrayList<Host>();
		ResultSet rs = null;
		PreparedStatement ps=null;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			ps=conn.prepareStatement("select host,id from sys.MEMBERS where kind=?");
			ps.setString(1, type.type());
			rs=ps.executeQuery();
			while(rs.next()){
				String host=rs.getString("host");
				String id=rs.getString("id").replace(":", "-");
				Host h= new Host(host,id);
				hostList.add(h);
			}
		}catch(Exception e){
			
			log.error("getTableList error",e);
			
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
			
		return hostList;
		
		
	}
	

}


