/**
 * 
 */
package com.tmg.gemfire.Action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tmg.core.Container;
import com.tmg.gemfire.DAOImp.GemfireDAOImp;

/**
 * @author Haojie Ma
 * @date Jun 15, 2016
 */

@Service("DeleteData")
@Scope("prototype")
public class DeleteData implements Runnable{
	
	private static Logger log=Logger.getLogger(DeleteData.class);
	
	@Autowired
	private  GemfireDAOImp gfDaoImp;
	
	
	private Container<String> container;
	
	private List<Integer> reservedClientList;


	public void setContainer(Container<String> container) {
		this.container = container;
	}

	
	

	public List<Integer> getReservedClientList() {
		return reservedClientList;
	}




	public void setReservedClientList(List<Integer> reservedClientList) {
		this.reservedClientList = reservedClientList;
	}




	public void run(){
		
		String schema="ODS";
		
		while(true){
			
			String table=container.get();
			if(table==null)
				return;
			//System.out.println(Thread.currentThread().getName()+"hi");
			List<Integer> totalClientList=gfDaoImp.getClientList(table);
			List<Integer> clientList= new ArrayList<Integer>();
			for(int clientId:totalClientList){
				if(!reservedClientList.contains(clientId))
					clientList.add(clientId);
			}
			for(Integer clientId:clientList){
				
				StringBuilder sql=new StringBuilder("delete from ").append(schema).append(".\"").append(table).append("\"");
				sql.append(" where client_id=").append(clientId).append(";");
					boolean f=true;
					while(f){
						try{
							int count=gfDaoImp.executeUpdate(sql.toString());
							f=false;
							log.info(table+" succeed,count="+count+",client_id="+clientId);
						}catch(Exception e){
							f=true;
							log.info(table+" failed");
							log.info("",e);
						}
					}
				
			}
			
			
			
		}
		
	}

}


