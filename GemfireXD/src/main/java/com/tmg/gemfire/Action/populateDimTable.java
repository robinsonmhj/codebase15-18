/**
 * 
 */
package com.tmg.gemfire.Action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tmg.core.Container;
import com.tmg.core.Mapper;
import com.tmg.gemfire.Bean.Column;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;

/**
 * @author Haojie Ma
 * @date Apr 26, 2016
 */
public class populateDimTable implements Runnable{
	
	
	private Logger log=Logger.getLogger(populateDimTable.class);
	
	@Autowired
	private  GreenplumDAOImp gpDaoImp;
	
	
	Container<String> container;
	
	public populateDimTable(){
		
	}
	
	public void setContainer(Container<String> container){
		this.container=container;
	}
	
	
	public void run(){
		
		String schema="odm_eval";
		
		while(true){
			
			String tableName=container.get();
			if(tableName==null)
					return;
			
			String tableBasedId=null;
			if(tableName.equals("dim_explanation_code"))
				 tableBasedId="explain_code_id";
			else
				 tableBasedId=tableName.substring(tableName.indexOf("_")+1, tableName.length())+"_id";
			
			Table table=gpDaoImp.getTableMetaData(schema, tableName);
			
			List<Column> columnList=table.getCloumns();
			StringBuilder sb= new StringBuilder("create temp table ").append(tableName).append("(\n");
			String columnName,columnType;
			List<String> pkList=new ArrayList<String>();
			for(Column c:columnList){
				
				columnName=c.getName();
				columnType=c.getType();
				if(c.isPrimary())
					pkList.add(columnName);
				
				sb.append(columnName).append(" ").append(columnType);
				
				if(columnName.equals("valid_from_ts"))
					sb.append(" default '2000-01-01 00:00:00',");
				else if(columnName.equals("active_ind"))
					sb.append(" default true,");
				else if(columnName.equals("valid_to_ts"))
					sb.append(" default '2199-12-31 00:00:00',");
				else if(columnName.equals("etl_end_ts")||columnName.equals("effective_date")||columnName.equals("expiry_date"))
					sb.append(",");
				else{
					sb.append(" default ").append(Mapper.getDefaultValue(columnType)).append(",");
					
				}
				
			}
			
			if(pkList.size()==0||(pkList.size()==1&&tableName.contains(pkList.get(0)))){
				log.info(tableName+" ignored, as it has only no primary or have only 1 and table name contains the primary key");
				continue;
			}
					
			
			//remove the last comma
			sb.deleteCharAt(sb.length()-1);
			
			sb.append(")\n");
			
			List<String> dsList=gpDaoImp.getDistributedKey(schema, tableName);
			
			if(!dsList.isEmpty())
				sb.append("DISTRIBUTED BY (");
			
			
			if(dsList.size()==1&&tableBasedId.equalsIgnoreCase(dsList.get(0)))
				sb.append("subclient_id,");
			else{
				for(String c:dsList)
					if(!tableBasedId.equalsIgnoreCase(c))
						sb.append(c).append(",");
			}
			
			
			//remove the last comma
			sb.deleteCharAt(sb.length()-1);
			sb.append(");\n");
			
			sb.append("insert into ").append(tableName).append("(");
			for(String c:pkList){
				//if(!tableBasedId.equalsIgnoreCase(c))
					//sb.append(c).append(",");
				if(c.equals("client_id")||c.equals("subclient_id"))
					sb.append(c).append(",");
				
			}
			if(tableName.endsWith("fact_member_address_history")||tableName.endsWith("fact_provider_address_history"))
				sb.append("address_type").append(",");
			
			
			//remove the last comma
			sb.deleteCharAt(sb.length()-1);
			sb.append(") select distinct ");
			
			
			for(String c:pkList){
				//if(!tableBasedId.equalsIgnoreCase(c)&&!"valid_from_ts".equalsIgnoreCase(c))//exclude valid_from_ts and the column named tableName_id(not including dim in the tableName)
					//sb.append(c).append(",");
				//only based on the clinet_id and subclient_id
				if(c.equals("client_id")||c.equals("subclient_id"))
					sb.append(c).append(",");
			}
			if(tableName.endsWith("fact_member_address_history")||tableName.endsWith("fact_provider_address_history"))
				sb.append("address_type").append(",");
			
			
			
			//remove the last comma
			sb.deleteCharAt(sb.length()-1);
			
			
			sb.append(" from ").append(schema).append(".").append(tableName).append(";\n");
			
			
			sb.append("insert into ").append(schema).append(".").append(tableName);
			sb.append(" select * from ").append(tableName).append(";\n");
			
			sb.append("drop table if exists ").append(tableName);
			
			
			gpDaoImp.executeMultipleQuery(sb.toString());
			
			
		}
		
		
		
		
	}
	
	
}


