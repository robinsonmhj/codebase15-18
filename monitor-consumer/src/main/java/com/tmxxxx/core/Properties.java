package com.tmxxxx.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Properties {

	private static Logger log=Logger.getLogger(Properties.class);
	
	private static Map<String,Object> map;
	
	static {
		readConfiguration();
	}
	
	private  static void readConfiguration(){
		BufferedReader reader=null;
		log.debug("Class:com.tmg.core.Properties");
		log.debug("currentpath:"+System.getProperty("user.dir"));
		final String file="/configuration.properties";
		map= new HashMap<String,Object>();
		try{
			InputStream stream =Properties.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			String line=reader.readLine();
			log.debug(line);
			String[] lines;
			String key,value;
			while(line!=null){
				lines=line.split("=");
				if(lines.length>2){
					log.debug("ignore line "+line);
				}else if(lines.length==2){
					key=lines[0];
					value=lines[1];
					map.put(key, value);
				}else
					map.put(lines[0], "");	
				line=reader.readLine();
			}
		}catch(Exception e){
			log.info("excpetion",e);
		}finally{
			try{
				reader.close();
			}catch(Exception ex){
				log.info("close file "+file+" error ",ex);
			}
		}
	}
	
	
	public static String getProperty(String key){
		String value=(String)map.get(key);
		log.debug(key+":"+value);
		return value;
		
	}
	
	
	public static Map<String,Object> getMap(){
		return map;
	}
	
	

}
