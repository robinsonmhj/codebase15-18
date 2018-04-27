/**
 * 
 */
package com.tmg.eventHandler.Synchronize;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pivotal.gemfirexd.callbacks.Event;
import com.pivotal.gemfirexd.callbacks.EventCallback;
import com.tmg.eventHandler.Asynchronize.AsyncListener;
import com.tmg.gemfire.DAO.CommonDAO;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;
import com.tmg.gemfire.Util.DataType;
import com.tmg.gemfire.Util.TmgLogger;

/**
 * @author Haojie Ma
 * @date Apr 16, 2015
 */
public class synchronizedWriter implements EventCallback {

	
	private static Logger log=Logger.getLogger(TmgLogger.LOGGER_NAME);
	
	private CommonDAO gpDaoImp;



	public void onEvent(Event event) throws SQLException {

		gpDaoImp=new GreenplumDAOImp();
		
		String columnName, dataType, updatedColumnName, value;
		
		try {

			Event.Type evType = event.getType();
			String tableName = event.getTableName();
			

			StringBuilder sql = new StringBuilder("insert into ");
			sql.append(tableName).append(" ( ");

			ResultSetMetaData oldMeta = event.getResultSetMetaData();

			Map<String, String> columnNameMap = new LinkedHashMap<String, String>();

			int columnTotal = oldMeta.getColumnCount();

			for (int i = 1; i <= columnTotal; i++) {
				columnName = oldMeta.getColumnName(i);
				dataType = oldMeta.getColumnTypeName(i);
				columnNameMap.put(columnName, dataType);
				sql.append(columnName).append(",");
			}

			// remove the last comma
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") values (");
			if (evType == Event.Type.BEFORE_UPDATE) {
				ResultSet updatedRs = event.getNewRowsAsResultSet();
				ResultSetMetaData updatedMeta = updatedRs.getMetaData();

				ResultSet oldRs = event.getOldRowAsResultSet();

				List<String> updatedColumnNameList = new ArrayList<String>();

				int columnCount = updatedMeta.getColumnCount();

				for (int i = 1; i <= columnCount; i++) {
					updatedColumnName = updatedMeta.getColumnName(i);
					updatedColumnNameList.add(updatedColumnName);
				}

				for (String key : columnNameMap.keySet()) {
					dataType = columnNameMap.get(key);

					if (updatedColumnNameList.contains(key)) {
						value = updatedRs.getString(key);
					} else {
						value = oldRs.getString(key);
					}

					if (DataType.isSpecial(dataType)&value!=null)
					{
						value=value.replaceAll("'", "''");//replace the column value which contains single quote
						if(value.contains("\\")){
							value=value.replace("\\", "\\\\");
							sql.append("E'").append(value).append("'");
						}else
							sql.append("'").append(value).append("'");
						
					}
					else
						sql.append(value);

					sql.append(",");

				}

				sql.deleteCharAt(sql.length() - 1);

				sql.append(");");

			} else if (evType == Event.Type.BEFORE_INSERT) {
				ResultSet newRow = event.getNewRowsAsResultSet();

				for (String key : columnNameMap.keySet()) {
					dataType = columnNameMap.get(key);
					value = newRow.getString(key);

					if (DataType.isSpecial(dataType)&value!=null)
					{
						value=value.replaceAll("'", "''");//replace the column value which contains single quote
						if(value.contains("\\")){
							value=value.replace("\\", "\\\\");
							sql.append("E'").append(value).append("'");
						}else
							sql.append("'").append(value).append("'");
						
					}
					else
						sql.append(value);

					sql.append(",");

				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(");");
			} else if (evType == Event.Type.BEFORE_DELETE) {
				ResultSet oldRs = event.getOldRowAsResultSet();
				for (String key : columnNameMap.keySet()) {
					dataType = columnNameMap.get(key);
					value = oldRs.getString(key);
					if (key.equals("del")||key.equals("DEL"))
						value = "1";
					if (DataType.isSpecial(dataType)&value!=null)
					{
						value=value.replaceAll("'", "''");//replace the column value which contains single quote
						if(value.contains("\\")){
							value=value.replace("\\", "\\\\");
							sql.append("E'").append(value).append("'");
						}else
							sql.append("'").append(value).append("'");
						
					}
					else
						sql.append(value);
					sql.append(",");

				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(");");

			}
			gpDaoImp.executeUpdate(sql.toString());
			

		} catch (Exception e) {
			//System.out.println("system.error in onEvent");
			//e.printStackTrace();
			log.log(Level.SEVERE,"log"+e);

		}

	}

	//will be called in the end
	public void close() throws SQLException {

		log.info("I am in close");
	}
	
	
	

	//first method will be called
	public void init(String targetSchema) throws SQLException {

		//this.targetSchema = targetSchema;
		log.info("log:init==" + targetSchema);
		
		AsyncListener asyncListener= new AsyncListener();
		log.info("sys i just create a asyncListener instance");
		
	}

	public static void main(String[] args){
		
		String classPath=System.getProperty("java.class.path");
		
		System.out.println("classPath++"+classPath);
		
		String[] classPathArray=classPath.split(";");
		BufferedReader reader=null;
		try{
			
			URI uri=synchronizedWriter.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			
			File path= new File(uri.getPath());
			
			InputStream stream =synchronizedWriter.class.getResourceAsStream("/jdbc.properties");
			
			
			
			 reader= new BufferedReader(new InputStreamReader(stream));
			
			
			String line=reader.readLine();
			
			while(line!=null){
				System.out.println(line);
				line=reader.readLine();
			}
				
			
			
			File base=new File(uri).getParentFile();
			
			System.out.println("path:"+path+",base:"+base.getPath());
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			try{
				
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		
		Class.class.getResourceAsStream("");
		
		for(int i=0;i<classPathArray.length;i++){
			String name=classPathArray[i];
			File file= new File(name);
			if(file.isDirectory()){
				
				String[] list= file.list();
				
				for(int j=0;j<list.length;j++)
					System.out.println(list[j]);
				
				
				
			}
			
			
			
		}
		
		
		
	}
	
	

}
