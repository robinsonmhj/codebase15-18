package com.tmxxx.Bean;

public enum  ServerType {
	LOCATOR("locator(normal)"),
	DATASTORE("datastore(normal)");

	private String type;
	
	private ServerType(String type){
		this.type=type;
	}
	
	public String type(){
		return type;
	}
}
