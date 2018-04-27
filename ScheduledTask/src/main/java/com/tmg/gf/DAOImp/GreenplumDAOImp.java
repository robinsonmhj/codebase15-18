/**
 * 
 */
package com.tmg.gf.DAOImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tmg.Log.ProcessFile;


/**
 * @author Haojie Ma
 * @date Sep 10, 2015
 */
public class GreenplumDAOImp {
	
	private static Logger log= Logger.getLogger(GreenplumDAOImp.class);
	
	@Autowired
	@Qualifier("dataSourceGreenplum")
	private DataSource dataSource;
	
	
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
	
	public boolean getReportByJobId(long jobId){
		
		ResultSet rs = null;
		Connection conn=null;
		String sql="select count(1) as count from sandbox.AB_Report where job_id=?";
		try{
			conn=dataSource.getConnection();
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setLong(1, jobId);
			rs=ps.executeQuery();
			int count=0;
			while (rs.next()) {
				count=rs.getInt("count");
			}
			
			if(count!=0)
				return true;
			
		}catch(Exception e){
			
			log.info("Exception:",e);
			
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		
		return false;
		
	}
	
	
	
	
	public void insert(ProcessFile file){
		
		if(file==null)
			return;
		
		long jobId=file.getFileBean().getJobId();
		String jobName=file.getFileBean().getJobName();
		Timestamp startTime=new Timestamp(file.getJobStartTime().getTime());
		Timestamp completeTime=new Timestamp(file.getJobCompletedTime().getTime());
		boolean status=file.isfailed();
		String message=file.getMessage();
		if(message!=null)
			message=message.replaceAll("'", "'").replace("\\", "\\\\");
		String env=file.getEnv();
		
		Connection conn=null;
		//jobId,jobName,startTime,completeTime,failed,message,env
		String sql="insert into  sandbox.AB_Report values(?,?,?,?,?,?,?)";
		try{
			conn=dataSource.getConnection();
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setLong(1, jobId);
			ps.setString(2, jobName);
			ps.setTimestamp(3, startTime);
			ps.setTimestamp(4, completeTime);
			ps.setBoolean(5, status);
			ps.setString(6, message);
			ps.setString(7, env);
			ps.executeUpdate();
			
		}catch(Exception e){
			
			log.info("Exception:",e);
			
		}finally{
			
			try{
				conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		
		
		
	}
	
	
	
	

}


