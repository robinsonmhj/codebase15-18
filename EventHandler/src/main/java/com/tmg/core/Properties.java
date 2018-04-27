package com.tmg.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;



import java.util.logging.Logger;

import com.tmg.eventHandler.Synchronize.synchronizedWriter;
import com.tmg.gemfire.Util.TmgLogger;

public class Properties {
	
	
	
	private static  Logger log=Logger.getLogger(TmgLogger.LOGGER_NAME);
	
	private static Map<String,String> map;
	
	static {
		readConfiguration();
	}
	
	private  static void readConfiguration(){
		
		BufferedReader reader=null;
		//log.info(Level.INFO,"Class:com.tmg.core.Properties");
		//log.info(Level.INFO,"currentpath:"+System.getProperty("user.dir"));
		final String file="/jdbc.properties";
		map= new HashMap<String,String>();
		try{
			InputStream stream =synchronizedWriter.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			String line=reader.readLine();
			
			while(line!=null&&!line.startsWith("#")){
				String[] lines=line.split("=");
				map.put(lines[0], lines[1]);
				line=reader.readLine();
			}
		}catch(Exception e){
			log.log(Level.SEVERE,"excpetion",e);
		}finally{
			try{
				reader.close();
			}catch(Exception ex){
				log.log(Level.SEVERE,"close file "+file+" error ",ex);
			}
		}
	}
	
	
	public  static String getProperty(String key){
		String value=map.get(key);
		//log.info(key+":"+value);
		return value;
		
	}
	
	
	public static void main(String[] args){
		
		
	
		
		
	}
	

}
