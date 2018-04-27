/**
 * 
 */
package com.appannie.stock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;




/**
 * @author Haojie Ma
 * @date Jun 2, 2016
 */
public class Properties {
	
	
private static Logger log=Logger.getLogger(Properties.class);
	
	private static Map<String,String> map;
	
	static {
		readConfiguration();
	}
	
	private  static void readConfiguration(){
		
		BufferedReader reader=null;
		//final String file="configuration.properties";
		//read the configuration from the jar
		final String file="/configuration.properties";
		map= new HashMap<String,String>();
		try{
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
	

}


