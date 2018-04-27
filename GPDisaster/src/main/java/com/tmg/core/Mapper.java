package com.tmg.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;



@Component
public class Mapper {
	
	
	private static Logger log=Logger.getLogger(Mapper.class);
	
	private static Map<String,Integer> typeMap;

	
	
	
	static{
		readConfiguration();
	}
	
	
	private  static void readConfiguration(){
		
		BufferedReader reader=null;
		log.debug("currentpath:Mapper function"+System.getProperty("user.dir"));
		final String file="/mapping.properties";
		typeMap= new HashMap<String,Integer>();
		try{
			
			//read for current folder
			//reader= new BufferedReader(new FileReader(new File(file)));
			InputStream stream =Mapper.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			
			String line=reader.readLine();
			String[] lines;
			String dataType;
			int type=0;
			while(line!=null){
				lines=line.split(",");
				dataType=lines[0];
				
				try{
					type=Integer.valueOf(lines[1]);
				}catch(Exception e){
					log.info("convert string to interger error",e);
				}
				
				typeMap.put(dataType, type);	
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
	
	
	/*
	 * get the parameter of a datatype
	 * 
	 * return 
	 * 
	 * 0 no parameter
	 * 1 one parameter
	 * 2 two parameter
	 */
	
	public static int getParameter(String dataType){
		
		int para=0;
		
		
		para=typeMap.get(dataType);
		
		return para;
		
		
		
	}
	
	
	
	public static void main(String[] args){
		
		String path=System.getProperty("user.dir");
		
		System.out.println(path);
		
		
		for(String key:typeMap.keySet())
			System.out.println(key+":"+typeMap.get(key));
		
		

		
		
	}
	

}
