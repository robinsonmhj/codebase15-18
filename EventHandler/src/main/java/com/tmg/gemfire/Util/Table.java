package com.tmg.gemfire.Util;


import java.util.List;

import com.tmg.gemfire.Util.Column;



public class Table {
	
	private String name;
	private List<Column> cloumns;
	//private String primaryKey;
	
	
	public Table(){
		
	}
	public Table(String name){
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getCloumns() {
		return cloumns;
	}

	public void setCloumns(List<Column> cloumns) {
		this.cloumns = cloumns;
	}
	
	/*
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	*/

}
