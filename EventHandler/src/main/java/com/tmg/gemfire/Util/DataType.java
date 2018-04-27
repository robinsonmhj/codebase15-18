/**
 * 
 */
package com.tmg.gemfire.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haojie Ma
 * @date Apr 17, 2015
 */
public class DataType {
	
	private static  List<String> specialList;
	//private  List<String> fullList;
	
	
	static{
		specialList= new ArrayList<String>();

		specialList.add("char");
		specialList.add("varchar");
		specialList.add("long varchar");
		specialList.add("time");
		specialList.add("timestamp");
		specialList.add("date");
		specialList.add("smallint");
		
		System.out.println("Hello I am in DataType static block");
		
	}
	
	
	/*
	public DataType(){
		
		
		
		specialList.add("char");
		specialList.add("varchar");
		specialList.add("long varchar");
		specialList.add("time");
		specialList.add("timestamp");
		specialList.add("date");
		specialList.add("smallint");
		
		
	}
	
	*/
	
	public static boolean  isSpecial(String type){
		
		boolean f=false;
		
		if(type==null||type.trim().equals(""))
			return f;
	
		if(specialList.contains(type.toLowerCase())||specialList.contains(type.toUpperCase())){
		
			f=true;
		
		 }
	
		return f;
	}
	

}


