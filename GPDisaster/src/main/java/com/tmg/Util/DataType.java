/**
 * 
 */
package com.tmg.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haojie Ma
 * @date Apr 17, 2015
 */
public class DataType {
	
	private static  List<String> specialList;
	
	
	static{
		specialList= new ArrayList<String>();

		specialList.add("char");
		specialList.add("varchar");
		specialList.add("text");
		specialList.add("xml");
		specialList.add("time");
		specialList.add("timetz");
		specialList.add("timestamp");
		specialList.add("timestamptz");
		specialList.add("date");
		specialList.add("bool");
		
		//System.out.println("Hello I am in DataType static block");
		
	}
	
	
	
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


