/**
 * 
 */
package com.tmghealth.log.gp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Haojie Ma
 * @date Feb 1, 2016
 */
public class ParseLog {
	
	
	
	
	public static void main(String[] args){
		
		//String fileName="test1.txt";
		//parseLog(fileName);
		
		
		/*
		String path="C:/project/Greenplum/DisasterRecovery/DevTestLog/";
		File dir= new File(path);
		String[] files=dir.list();
		for(int i=0;i<files.length;i++){
			String file=files[i];
			if(file.endsWith(".csv"))
				parseLog(file);
		}
		*/
		
		
		readFile("C:/project/Greenplum/DisasterRecovery/DevTestLog//target/gpdb-2016-02-01_000000.csv");
		
		
		
	}
	
	
	
	
	public static void parseLog(String fileName){
		
		
		String path="C:/project/Greenplum/DisasterRecovery/DevTestLog/";
		File file=new File(path+fileName);
		File f1= new File(path+"/target/"+fileName);
		File f2= new File(path+"/target/"+fileName+".error");
		BufferedReader reader=null;
		BufferedWriter writer1=null;
		BufferedWriter writer2=null;
		try{
			reader= new BufferedReader(new FileReader(file));
			writer1= new BufferedWriter(new FileWriter(f1));
			writer2= new BufferedWriter(new FileWriter(f2));
			String line=reader.readLine();
			StringBuilder sb=null;
			int count=0;
			
			
			while(line!=null){
				
				if(line.matches("^([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{1,6} CST,).*")){
					//System.out.println("+++"+line);
					count++;
					
					if(count==2){
						if(filterString(sb.toString())){
							writer1.write(sb.toString());
							writer1.write("\n");
						}else{
							writer2.write(sb.toString());
							writer2.write("\n");
						}
						count=1;
					}
					sb= new StringBuilder(line.replaceAll("\n", ""));
				}else{
					sb.append(line);
				}
				line=reader.readLine();
			}
			
			if(filterString(sb.toString())){
				writer1.write(sb.toString());
				writer1.write("\n");
			}else{
				writer2.write(sb.toString());
				writer2.write("\n");						
			}
		}catch(Exception e){
			
			
		}finally{
			try{
				reader.close();
				writer1.flush();
				writer1.close();
				writer2.flush();
				writer2.close();
				
			}catch(Exception e){
				
				
			}
			
			
		}
		
		
		
		
	}
	
	
	public static boolean filterString(String s){
		
		//System.out.println("+++"+s);
		String[] result=s.split(",");
		if(result.length>=3&&(result[2].equalsIgnoreCase("\"testtmgdw\"")||result[2].equalsIgnoreCase("\"devtmgdw_2\"")))
			return true;
		else
			return false;
		
		
		
		
		
		
		
	}
	
	
	
	
	
	public static void readFile(String filename){
		
		BufferedReader reader=null;
		
		try{
			
			reader = new BufferedReader(new FileReader(new File(filename)));
			
			String line=reader.readLine();
			
			int count=0;
			while(line!=null){
				count++;
				System.out.println(line);
				if(count==20){
					
					break;
				}
					
				
				 line=reader.readLine();
				
				
				
			}
			
			
			
			
		}catch(Exception e){
			
			
			
		}
		
		
		
	}
	
	

}


