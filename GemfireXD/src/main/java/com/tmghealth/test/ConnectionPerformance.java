/**
 * 
 */
package com.tmghealth.test;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tmg.gemfire.Action.DeleteData;
import com.tmg.gemfire.DAOImp.GemfireDAOImp;

/**
 * @author Haojie Ma
 * @date Nov 9, 2017
 */

@Service("ConnectionPerformance")
@Scope("prototype")
public class ConnectionPerformance implements Runnable{
	
	private static Logger log=Logger.getLogger(ConnectionPerformance.class);
	private Map<Integer,String> sqlMap;
	private Map<Integer,List<String>> parameters;
	
	@Autowired
	private  GemfireDAOImp gfDaoImp;
	
	public ConnectionPerformance(Map<Integer,String> sqlMap,Map<Integer,List<String>> parameters){
		
		this.sqlMap=sqlMap;
		this.parameters=parameters;
		
	}
	
	public ConnectionPerformance(){
		
	}
	
	public void setSqlMap(Map<Integer,String> sqlMap){
		this.sqlMap=sqlMap;
	}
	
	public void setParameters(Map<Integer,List<String>> parameters){
		this.parameters=parameters;
	}
	
	
	public void run(){
		
		int round=200;
		int sleep=500;
		while(round>0){
			int key=(int)(Math.random()*3);
			
			String sql=sqlMap.get(key);
			
			int index=(int)(Math.random()*100);
			System.out.println("key="+key+",index="+index);
			String para=parameters.get(key).get(index);
			
			
			try{
				//synchronized(Integer.class){
					gfDaoImp.execute(sql, para);
				//}
				
				Thread.sleep(sleep);
			}catch(Exception e){
				log.info("canot sleep",e);
			}
			
			round--;
			
		}
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	

}


