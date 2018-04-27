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
	
	private static Map<String,Integer> tableType;
	
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
		final String typeFile="/table_type.properties";
		map= new HashMap<String,String>();
		tableType= new HashMap<String,Integer>();
		try{
			//read from current folder which is the same path as the jar
			//reader= new BufferedReader(new FileReader(new File(file)));
			//read the configuration from the jar
			InputStream stream =Properties.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			String line=reader.readLine();
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
			stream =Properties.class.getResourceAsStream(typeFile);
			reader= new BufferedReader(new InputStreamReader(stream));
			line=reader.readLine();
			while(line!=null){
				lines=line.split(",");
				key=lines[0];
				int type=-1;
				try{
					type=Integer.valueOf(lines[1]);
				}catch(Exception e){
					log.info("type not correct,",e);
				}
				tableType.put(key, type);
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
	
	
	public static Map<String,Integer> getTableType(){
		
		return tableType;
	}
	
	public static void main(String[] args){
		
		
	
		
		
	}
	

}
