/**
 * 
 */
package com.barclaycard.us.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;



/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class FileUtil {
	
	private static Logger log=Logger.getLogger(FileUtil.class);
	
	public static List<String> readFile(String fileName){
		List<String> l= new ArrayList<String>();
		File file=new File(fileName);
		if(!file.exists()){
			log.error(fileName+" doesn't exist");
			return l;
		}
		BufferedReader reader=null;
		try{
			reader= new BufferedReader(new FileReader(file));
			String line=reader.readLine();
			while(line!=null){
				if(line.length()==0){
					log.info("thre is one invalid line in "+fileName);
				}else
					l.add(line);
				line=reader.readLine();
			}
		}catch(Exception e){
			log.info("something unexpected while reading file:",e);
			
		}finally{
			try{
				reader.close();
			}catch(Exception e){
				log.error("cannot close file",e);
			}
		}
		
		return l;
	}

}
