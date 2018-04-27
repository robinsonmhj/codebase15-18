package com.tmg.Util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;





import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;







import org.springframework.util.StringUtils;

import com.tmg.Bean.Column;
import com.tmg.Bean.Table;
import com.tmg.core.Mapper;
import com.tmg.core.Properties;





@Component
public class TableUtil {
	
	
	private static Logger log=Logger.getLogger(TableUtil.class);
	
	
	
	public static List<String> getPrimary(Table table){
		
		if(table==null){
			log.error("Table is null");
			return null;
		}
		
		
		List<String> keyList= new ArrayList<String>();
		
		List<Column> columnList=table.getCloumns();
		
		
		Iterator<Column> iter= columnList.iterator();
		
		while(iter.hasNext()){
			Column column=iter.next();
			if(column.isPrimary()){
				String columnName=column.getName();
				keyList.add(columnName);
				log.debug("PrimaryKey:"+columnName+"added");
			}
		}
		
		return keyList;
		
		
	}
	
	
	public static String getPartitionColumn(Table table){
		
		String columnName=null,name;
		Column column;
		
		String partitionString=Properties.getProperty("tmg.partitioned.columns");
		List<String> partitionList=FileUtil.string2List(partitionString);
		
		if(StringUtils.isEmpty(table)){
			
			log.info("table is null");
			return columnName;
			
		}
		
		List<Column> columnList=table.getCloumns();
		
		Iterator<Column> iterator= columnList.iterator();
		while(iterator.hasNext()){
			column=iterator.next();
			name=column.getName();
			if(partitionList.contains(name)){
				columnName=name;
				break;
			}
				
		}
		
		return columnName;
		
		
	}
	
	

	public static StringBuilder createTable(Table table,String newName){
		
		if(StringUtils.isEmpty(table)){
			log.error("table is "+table);
			return null;
		}
		
		Column column;
		String columnName,columnType,flag;
		int len;
		StringBuilder create= new StringBuilder("create table ").append(newName).append("\n(");
		List<Column> columnList=table.getCloumns();
		int index=newName.indexOf(".");
		len=newName.length();
		String tablewithoutschema=newName.substring(index+1, len);

		Iterator<Column> iterator= columnList.iterator();
		
		while(iterator.hasNext()){
			column=iterator.next();
			columnName=column.getName();
			columnType=column.getType();
			flag=column.getFlag();
			int columnLen=column.getLen();
			create.append("\t").append(columnName).append(" ");
			//datatype bpchar wasn't regonized in the create table definition
			if(columnType.equals("bpchar"))
				create.append("character");
			else
				create.append(columnType);
			
			//numeric default length is 131089. we don't need to specify that
			if(!columnType.equals("numeric")&&columnLen!=131089){
				int type=Mapper.getParameter(columnType);
				if(type==1){
					create.append("(").append(columnLen).append(") ");	
				}else if(type==2){
					int decimal=column.getDecimalLen();
					create.append("(").append(columnLen).append(",").append(decimal);
					create.append(") ");
				}
			}
			
			
			if(flag.equals("NO"))
				create.append(" NOT NULL");
			
			create.append(",\n");
		}
		
		List<String> pkList=getPrimary(table);
		List<String> dsList=table.getDsList();
		
		if(!StringUtils.isEmpty(pkList)){
			Iterator<String> pkItera=pkList.iterator();
			String pk;
			create.append("\tCONSTRAINT ").append(tablewithoutschema).append("_pkey PRIMARY KEY (");
			
			//distributed key should be exactly the same with primary key or left sub set of primary key
			for(int i=0;i<dsList.size();i++){
				pk=dsList.get(i);
				create.append(pk).append(",");
			}
			while(pkItera.hasNext()){
				pk=pkItera.next();
				if(!dsList.contains(pk))
					create.append(pk).append(",");
			}
			//remove the last comma
			len=create.length();
			create.delete(len-1, len);
			
			create.append(")\n");
		}
		
		create.append(")\n");//bucket to close the column definition
		
		
		if(!StringUtils.isEmpty(dsList)){
			Iterator<String> dsKeyItera=dsList.iterator();
			create.append("DISTRIBUTED BY (");
			while(dsKeyItera.hasNext()){
				String dsKey=dsKeyItera.next();
				create.append(dsKey).append(",");
				
			}
			
			
			//remove the last comma
			len=create.length();
			create.delete(len-1, len);
		
			create.append(");\n");
		}
		
		
		return create;
		
		
	}
 

}
