package com.tmg.gemfire.Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileUtil {
	
	private static Logger log= Logger.getLogger(FileUtil.class);
	
	public static List<String> string2List(String s){
		
		
		if(s==null||s.trim().equals(""))
			return null;
		
		List<String> l= new ArrayList<String>();
		
		String[] array=s.split(",");
		int len=array.length;
		
		for(int i=0;i<len;i++){
			String value=array[i];
			if(!value.startsWith("#")){
				l.add(value);
				//log.debug("FileUtil add "+value+" to List");
			}	
		}
		return l;
	}
	
	
	
	
	
	
	
	

}
