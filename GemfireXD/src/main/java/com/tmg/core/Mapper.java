package com.tmg.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;



@Component
public class Mapper {
	
	
	private static Logger log=Logger.getLogger(Mapper.class);
	
	private static Map<String,String> typeMap;
	private static Map<String,Integer> paraMap;
	private static Map<String,String> defaultValueMap;
	
	
	
	static{
		readConfiguration();
	}
	
	
	private  static void readConfiguration(){
		
		BufferedReader reader=null;
		log.info("currentpath:Mapper function"+System.getProperty("user.dir"));
		final String file="/mapping_mssql.properties";
		typeMap= new HashMap<String,String>();
		paraMap= new HashMap<String,Integer>();
		defaultValueMap= new HashMap<String,String>();
		try{
			
			//read for current folder
			//reader= new BufferedReader(new FileReader(new File(file)));
			InputStream stream =Mapper.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			
			String line=reader.readLine();
			String[] lines;
			String gp,gf,defaultValue;
			int para=0;
			while(line!=null){
				lines=line.split(",");
				gp=lines[0];
				gf=lines[1];
				defaultValue=lines[3];
				//System.out.println("gp="+gp+",gf="+gf+",default="+defaultValue);
				try{
					para=Integer.valueOf(lines[2]);
				}catch(Exception e){
					System.out.println("line="+line);
					log.info("convert string to interger error",e);
				}
				
				typeMap.put(gp, gf);	
				paraMap.put(gp, para);
				defaultValueMap.put(gp, defaultValue);
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
	
	
	public static String Gemfire2GP(String type){
		
		String value;
		for(String key:typeMap.keySet()){
			
			value=typeMap.get(key);
			if(value.equalsIgnoreCase(type))
				return key;
		}
				
		return null;
		
		
	}
	
	
	public static String GP2Gemfire(String type){
		

		
		 String value=typeMap.get(type);
		 return value;
		
		
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
	
	public static int getParameter(String gpType){
		
		int para=0;
		
		
		para=paraMap.get(gpType);
		
		return para;
		
		
		
	}
	
	public static String getDefaultValue(String gpType){
		
		
		return defaultValueMap.get(gpType);
		
		
	}
	
	public static void main(String[] args){
		
		String path=System.getProperty("user.dir");
		
		System.out.println(path);
		
		
		
		System.out.println(Mapper.GP2Gemfire("int"));
		System.out.println(Mapper.getParameter("int"));
		
		for(String key:typeMap.keySet())
			System.out.println(key+":"+typeMap.get(key));
		
		
		for(String key:paraMap.keySet())
			System.out.println(key+":"+paraMap.get(key));
		
		
	}
	

}
