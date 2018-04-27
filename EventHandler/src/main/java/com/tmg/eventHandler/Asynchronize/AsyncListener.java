/**
 * 
 */
package com.tmg.eventHandler.Asynchronize;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



import java.util.logging.Logger;



import java.util.logging.Level;

import com.pivotal.gemfirexd.callbacks.AsyncEventListener;
import com.pivotal.gemfirexd.callbacks.Event;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;
import com.tmg.gemfire.Util.Column;
import com.tmg.gemfire.Util.DataType;
import com.tmg.gemfire.Util.Table;
import com.tmg.gemfire.Util.TmgLogger;

/**
 * @author Haojie Ma
 * @date Apr 16, 2015
 */


//2015-06-26 issues Due to the oldresult is null in the update event, if the column is not update, I will use blank as the value

public class AsyncListener implements AsyncEventListener {

    private static  Logger log=Logger.getLogger(TmgLogger.LOGGER_NAME);
    

	
	private GreenplumDAOImp gpDaoImp;
	
	private List<Table> tableList=new ArrayList<Table>();
	
	
	
	static{

		//System.out.println("system.out. I am AsyncListener, I am going to memory");
		log.info("I am AsyncListener, I am going to memory");
		
	}
	
	public  boolean processEvents(List<Event> events){
		//System.out.println("Async:PorcessEvent method");
		
		boolean successful=false;
		Event event;
		Event.Type eventType;
		String tableName,targetTable,columnName,updatedColumnName,dataType,value;
		String targetSchema="gf_ods";
		ResultSetMetaData meta;
		StringBuilder insertSql=null;
		String updateSql=null;
		List<String> updateList=null;
		Map<String, String> columnNameMap = null;
		Map<String,Map<String,String>> tableColumnMap = new HashMap<String,Map<String,String>>();
		int eventCount=0,executeCount=0,index;//count in the 
		//key tableName, value insert statements
		Map<String,StringBuilder> insertMap=new HashMap<String,StringBuilder>(); 
		Map<String,List<String>> updateMap= new HashMap<String,List<String>>();
		try{
			gpDaoImp= new GreenplumDAOImp();
			Iterator<Event> iter= events.iterator();
			
			while(iter.hasNext()){
				event=iter.next();
				eventType=event.getType();
				tableName= event.getTableName();
				String[] tmp=tableName.split("\\.");
				if(tmp.length!=2){
					targetTable=tableName;
					log.info("tableName is "+tableName);
					for(int i=0;i<tmp.length;i++)
						log.info("tmp["+i+"]"+tmp[i]);
					continue;
				}
					
				targetTable=tmp[1];
				
				//initial the insert statement
				if(!insertMap.containsKey(tableName)){
					insertSql = new StringBuilder("insert into ");
					insertSql.append(targetSchema).append(targetTable).append(" ( ");
					columnNameMap =new LinkedHashMap<String, String>();
					meta = event.getResultSetMetaData();
					int columnTotal = meta.getColumnCount();
					for (int i = 1; i <= columnTotal; i++) {
						columnName = meta.getColumnName(i);
						dataType = meta.getColumnTypeName(i);
						columnNameMap.put(columnName, dataType);
						insertSql.append(columnName).append(",");
					}
					
					tableColumnMap.put(tableName, columnNameMap);
					//insertSql.deleteCharAt(insertSql.length() - 1);
					//there is no del in GF and we should put del when inserting data to GP
					insertSql.append("del");
					insertSql.append(") values (");
				
				}else{
					insertSql=insertMap.get(tableName);
					columnNameMap=tableColumnMap.get(tableName);
					insertSql.append("(");
				}
					
				
				
				if(eventType==Event.Type.AFTER_DELETE){
					log.info("Async:after delete event");
					/*
					 //used to insert 
					ResultSet oldRs = event.getOldRowAsResultSet();
					for (String key : columnNameMap.keySet()) {
						dataType = columnNameMap.get(key);
						value = oldRs.getString(key);
						//if (key.equalsIgnoreCase("del"))
							//value = "1";
						if (DataType.isSpecial(dataType)&&value!=null){
								value=value.replaceAll("'", "''");//replace the column value which contains single quote
								if(value.contains("\\")){
									value=value.replace("\\", "\\\\");
									insertSql.append("E'").append(value).append("'");
								}else
									insertSql.append("'").append(value).append("'");
								
						}else
							insertSql.append(value);
						insertSql.append(",");

					}
					//the value for del
					insertSql.append("'1'");
					insertSql.append("),");
				*/
					
					List<String> pkList=gpDaoImp.getPrimaryKey(targetSchema.toLowerCase(),targetTable);
					if(updateMap.containsKey(tableName)){
						
						updateList=updateMap.get(tableName);
						
					}else{
						
						updateList= new ArrayList<String>();
						
					}
					
					StringBuilder sbUpdate= new StringBuilder("update ");
					sbUpdate.append(targetSchema).append(targetTable);
					sbUpdate.append(" set del=1 where ");
					
					ResultSet oldRs = event.getOldRowAsResultSet();
					for (String key : columnNameMap.keySet()) {
						dataType = columnNameMap.get(key);
						value = oldRs.getString(key);
						if(pkList.contains(key.toLowerCase())||pkList.contains(key.toUpperCase())){
							sbUpdate.append(key).append("=");
							if (DataType.isSpecial(dataType))
								sbUpdate.append("'").append(value).append("'");
							else
								sbUpdate.append("value");
							//put add in the end
							sbUpdate.append(" and ");
						}
						
					}
					//remove the last end
					index=sbUpdate.indexOf(" and ");
					String update=sbUpdate.substring(0, index);
					updateList.add(update);
					updateMap.put(tableName,updateList);
					
					
					eventCount++;
				//two things needs to be done
				//make the original one as delete
				//insert a new row
				}else if(eventType==Event.Type.AFTER_UPDATE){
					log.info("Async:after update event ");
					
					ResultSet updatedRs = event.getNewRowsAsResultSet();
					ResultSetMetaData updatedMeta = updatedRs.getMetaData();
					
					/*
					//known bug in GF
					ResultSet oldRs = event.getOldRowAsResultSet();
					if(oldRs==null)
					{
						log.info("oldRowAsResultSet is null+escape");
						continue;
					}
					*/
					List<String> pkList=gpDaoImp.getPrimaryKey(targetSchema.toLowerCase(),targetTable);
					List<String> updatedColumnNameList = new ArrayList<String>();
					
					if(updateMap.containsKey(tableName)){
						
						updateList=updateMap.get(tableName);
						
					}else{
						
						updateList= new ArrayList<String>();
						
					}
						
					StringBuilder sbUpdate= new StringBuilder("update ");
					sbUpdate.append(targetSchema).append(targetTable);
					sbUpdate.append(" set del=1 where ");

					int columnCount = updatedMeta.getColumnCount();

					for (int i = 1; i <= columnCount; i++) {
						updatedColumnName = updatedMeta.getColumnName(i);
						log.info("Updated columnName"+i+":"+updatedColumnName);
						updatedColumnNameList.add(updatedColumnName);
					}
					
					StringBuilder select=new StringBuilder("select * from ");
					select.append(targetSchema).append(targetTable).append(" where ");
					
					for (String key : columnNameMap.keySet()) {
						dataType = columnNameMap.get(key);
						value = updatedRs.getString(key);
						if(pkList.contains(key.toLowerCase())||pkList.contains(key.toUpperCase())){
							select.append(key).append("=");
							if (DataType.isSpecial(dataType))
								select.append("'").append(value).append("'");
							else
								select.append("value");
							//put add in the end
							select.append(" and ");
						}
					}
					
					index=select.indexOf(" and ");
					String selectSql=select.substring(0, index);
					Map<String,String> result=gpDaoImp.execute(selectSql);
					

					insertSql= insertMap.get(tableName);
					
					for (String key : columnNameMap.keySet()) {
						dataType = columnNameMap.get(key);

						if (updatedColumnNameList.contains(key)) {
							value = updatedRs.getString(key);
							if(pkList.contains(key.toLowerCase())||pkList.contains(key.toUpperCase())){
								sbUpdate.append(key).append("=");
								if (DataType.isSpecial(dataType))
									sbUpdate.append("'").append(value).append("'");
								else
									sbUpdate.append("value");
								//put add in the end
								sbUpdate.append(" and ");
							}
								
						} else {
							//value = oldRs.getString(key);
							if(!result.isEmpty())
								value=result.get(key);
							else
								value="";
						}

						if (DataType.isSpecial(dataType)&value!=null){
							value=value.replaceAll("'", "''");//replace the column value which contains single quote
							
							if(value.contains("\\")){
								value=value.replace("\\", "\\\\");
								insertSql.append("E'").append(value).append("'");
							}else
								insertSql.append("'").append(value).append("'");
							
							
						}else
							insertSql.append(value);

						insertSql.append(",");

					}

					insertSql.deleteCharAt(insertSql.length() - 1);
					//remove the last end
					index=sbUpdate.indexOf(" and ");
					String update=sbUpdate.substring(0, index);
					updateList.add(update);
					updateMap.put(tableName,updateList);
					

					insertSql.append("),");
					
					eventCount+=2;
					
				}else if(eventType==Event.Type.AFTER_INSERT){
					log.info("Async:after insert event");
					
					ResultSet newRow = event.getNewRowsAsResultSet();

					for (String key : columnNameMap.keySet()) {
						dataType = columnNameMap.get(key);
						value = newRow.getString(key);

						if (DataType.isSpecial(dataType)&value!=null){
							value=value.replaceAll("'", "''");//replace the column value which contains single quote
							if(value.contains("\\")){
								value=value.replace("\\", "\\\\");
								insertSql.append("E'").append(value).append("'");
							}else
								insertSql.append("'").append(value).append("'");
							
						}else
							insertSql.append(value);

						insertSql.append(",");

					}
					//insertSql.deleteCharAt(insertSql.length() - 1);
					//value for column del
					insertSql.append("'0'");
					insertSql.append("),");
					
					eventCount++;
					
				}
				
				insertMap.put(tableName, insertSql);
				
			}
			
			log.info("There are "+eventCount+" in the event list");
			//execute
			
			
			for(String key:insertMap.keySet()){
				
				insertSql=insertMap.get(key);
				
				//remove the last comma
				insertSql.deleteCharAt(insertSql.length()-1);
				
				insertSql.append(";");
				
				int count=gpDaoImp.executeUpdate(insertSql.toString());
				
				if(count==0){
					
					insertMap.remove(key);
					log.info("The following insertSql executed failed and removed from queue");
					log.info(insertSql.toString());
				}
				
				log.info("Table:"+key+",count:"+count);
				
				executeCount+=count;
				
			}
			
			for(String key:updateMap.keySet()){
				updateList=updateMap.get(key);
				Iterator<String> iterator=updateList.iterator();
				while(iterator.hasNext()){
					updateSql=iterator.next();
					gpDaoImp.executeUpdate(updateSql);
					executeCount++;
				}
			}
			
			log.info("Total execute count:"+executeCount);
			//System.out.println("Async:Total execute count:"+executeCount);
			
		
			if(executeCount!=eventCount){
				log.info("execut Count doesn't match event count");
				//System.err.println("Async:execut Count doesn't match event count");
				log.info("executeCount="+executeCount+"+eventCount="+eventCount);
				//System.err.println("Async:executeCount="+executeCount+"+eventCount="+eventCount);
				
			}else{
				
				successful=true;
				
			}
				
			
			
			
		}catch(Exception e){
			
			log.log(Level.SEVERE,"Exception in processEvent",e);
			//System.out.println("Async:Exception e");
			//e.printStackTrace();
			
		}
		
		
		
		
		
		return successful;
		
	}
	  
	// the very last method will be called
	  public  void close(){
		  
	  }
	  
	  //the very first method it will call 
	  public  void init(String paramString){
		  
		 // System.out.println("Async:init method");
		  //log.info(Level.INFO, "Async:init method");
		  
	  }
	  
	  //call after init
	  public  void start(){
		  
		 //log.info(Level.INFO,"Async:start method");
		  
	  }
	
	  
	  public void putTable2List(Event event){
		 
		try{
			String tableName=event.getTableName();
			Iterator<Table> iter= tableList.iterator();
			Table table;
			while(iter.hasNext()){
				table= iter.next();
				if(tableName.equals(table.getName()))
					return;
			}
			ResultSetMetaData  meta = event.getResultSetMetaData();
			int columnTotal = meta.getColumnCount();
			List<Column> list= new ArrayList<Column>();
			for (int i = 1; i <= columnTotal; i++) {
				String columnName = meta.getColumnName(i);
				String dataType = meta.getColumnTypeName(i);
				Column column= new Column(columnName,dataType);
				list.add(column);
			}
			table= new Table(tableName);
			table.setCloumns(list);
			if(!tableList.contains(table)){
				tableList.add(table);
			}
		}catch(Exception e){
			log.log(Level.SEVERE,"Exception",e);
			//System.out.println("Async:putTable2List exception");
			//e.printStackTrace();
		}
	
	  }
	  
	  
	  public Table getTable(String tableName){
		  
		  Iterator<Table> iter= tableList.iterator();
		  Table table=null;
		  while(iter.hasNext()){
			  table=iter.next();
			  if(table.getName().equals(tableName))
				  return table; 
		  }
		  return table;
	  }
	  
	  
	  /*
	  
	  public static void main(String[] args){
		  try{
			  
			  CommonDAO  gpDaoImp= new GreenplumDAOImp();
			  int a=gpDaoImp.execute("select count(1) from gf_ods.check");
			  System.out.println(a);
		  }catch(Exception e){
			  
			  e.printStackTrace();
			  
		  }
		
		  
		  
		  
	  }
	  */
	  
}


