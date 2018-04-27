package com.tmg.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;



public class Properties {
	
	
	
	private static Logger log=Logger.getLogger(Properties.class);
	
	private static Map<String,String> map;
	
	static {
		readConfiguration();
	}
	
	private  static void readConfiguration(){
		
		BufferedReader reader=null;
		log.debug("Class:com.tmg.core.Properties");
		log.debug("currentpath:"+System.getProperty("user.dir"));
		//final String file="configuration.properties";
		//read the configuration from the jar
		final String file="/configuration.properties";
		map= new HashMap<String,String>();
		try{
			//read from current folder which is the same path as the jar
			//reader= new BufferedReader(new FileReader(new File(file)));
			//read the configuration from the jar
			InputStream stream =Properties.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			String line=reader.readLine();
			String[] lines;
			String key,value;
			//System.out.println("haha:"+line);
			while(line!=null){
				if(line.startsWith("#")){
					line=reader.readLine();
					continue;
				}
					
				lines=line.split("=");
				if(lines.length!=2){
					log.debug("ignore line "+line);
				}else{
					key=lines[0];
					value=lines[1];
					map.put(key, value);
				}
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
	
	
	public  static String getProperty(String key){
		String value=map.get(key);
		log.debug(key+":"+value);
		return value;
		
	}
	
	
	public static void main(String[] args){
		
		
	
		
		
	}
	

}
