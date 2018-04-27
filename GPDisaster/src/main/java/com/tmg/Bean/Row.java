/**
 * 
 */
package com.tmg.Bean;

import java.util.Map;

/**
 * @author Haojie Ma
 * @date Dec 17, 2015
 */
public class Row {
	
	private String operation;
	private Map<String,String> map;
	
	
	public Row(){
		
		
	}
	
	
	public Row(String operation,Map<String,String> map){
		this.operation=operation;
		this.map=map;
		
	}
	
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
	
	
	

}


