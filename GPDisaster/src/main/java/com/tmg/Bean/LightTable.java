/**
 * 
 */
package com.tmg.Bean;

import java.util.List;

/**
 * @author Haojie Ma
 * @date Dec 18, 2015
 */
public class LightTable {
	
	private String tableName;
	private List<String> columnList;
	
	public LightTable(){
		
		
	}
	
	
	public LightTable(String tableName){
		
		this.tableName=tableName;
	}
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<String> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}


	
	
	
	
	

}


