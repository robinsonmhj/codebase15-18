/**
 * 
 */
package com.tmg.thread;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

/**
 * @author Haojie Ma
 * @date Nov 5, 2015
 */
public class CloseStatement implements Runnable{
	
	private static Logger log=Logger.getLogger(CloseStatement.class);
	
	private PreparedStatement ps;
	
	
	public CloseStatement(){			
		
	}
	
	
	public CloseStatement(PreparedStatement ps){
		
		this.ps=ps;
		
	}
	
	
	
	
	public PreparedStatement getPs() {
		return ps;
	}


	public void setPs(PreparedStatement ps) {
		this.ps = ps;
	}


	public void run(){
		
		


		if(ps!=null){
				try{
					ps.cancel();
				}catch(Exception e){
					log.info("Cancel statement error",e);
				}finally{
					try{
						ps.close();
					}catch(Exception e){
						log.info("Close statement error",e);
					}
				}
		}
		
		
	}
	
	

}


