package com.tmg.gemfire.Util;


import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLParser {
	public static Logger log=Logger.getLogger(XMLParser.class);
	

	
	public static String xml2String(String doc){
		
		StringBuilder sb= new StringBuilder();
		
		try{
			
			SAXReader reader = new SAXReader();
	        Document document = reader.read(doc);
	        
	        
	        document.getRootElement();
	        
	        List listName = document.selectNodes("//row/@c1");
	        List listDesc = document.selectNodes("//row/@c3");
	        
	        Iterator iterName=listName.iterator();
	        Iterator iterDesc=listDesc.iterator();
	        
	        String roleName,roleDesc;
	   
	        while(iterName.hasNext()||iterDesc.hasNext()){
	        	Attribute attributeName=(Attribute) iterName.next();
	        	Attribute attributeDesc=(Attribute) iterDesc.next();
	        	roleName=attributeName.getValue();
	        	roleDesc=attributeDesc.getValue();
	        	sb.append(roleName).append("\t").append(roleDesc).append("\n");
	        }
	        
			
			
		}catch(Exception e){
			
			log.error("",e);
			
		}
		
		
		return sb.toString();
		
		
	}
	
	
	
	public static void main(String[] args){
		
		String doc="C:/users/hma/Desktop/Devrole.xml";
		
		System.out.println(xml2String(doc));
		
	}
	
	

}
