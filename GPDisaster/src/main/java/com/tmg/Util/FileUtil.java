/**
 * 
 */
package com.tmg.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;














import java.util.Map;

import org.apache.log4j.Logger;

import com.tmg.Bean.Column;
import com.tmg.Bean.Row;
import com.tmg.Bean.Table;
import com.tmg.core.Properties;


/**
 * @author Haojie Ma
 * @date Sep 9, 2015
 */
public class FileUtil {
	
	
	
	private static Logger log=Logger.getLogger(FileUtil.class);
	

	

	public static String convertMill2Date(long millecond){
		long start=System.currentTimeMillis();
		StringBuilder date=new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millecond);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		String delimiter="-";
		date.append(year).append(delimiter);
		if(month<10)
			date.append(0).append(month);
		else
			date.append(month);
		
		date.append(delimiter);
		
		
		if(day<10)
			date.append(0).append(day);
		else
			date.append(day);
		long end=System.currentTimeMillis();
		long using=end-start;
		if(using>10)
			log.info("convert date using time"+using);
		return date.toString();
		
		
	}

	
	public static String getTSColumn(Table table){
		
		List<Column> columnList=table.getCloumns();
		Iterator<Column> ite=columnList.iterator();
		String columnName;
		Column column;
		String tsString=Properties.getProperty("tmg.timestamp.columns");
		
		List<String> tsList=string2List(tsString);
		if(tsList==null)
			return null;
		
		while(ite.hasNext()){
			column=ite.next();
			columnName=column.getName();
			if(tsList.contains(columnName)){
				return columnName;
			}
		}
		return null;
	}
	

	
	private static String formatDate(String date){
		
		try{
			DateFormat df = new SimpleDateFormat("ddMMMyyyy");
			StringBuilder dateString= new StringBuilder();
			Date d=df.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;// Jan is 0,Feb 1, plus 1 to the month
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			String delimiter="-";
			dateString.append(year).append(delimiter);
			if(month<10)
				dateString.append(0).append(month);
			else
				dateString.append(month);
			
			dateString.append(delimiter);
			
			
			if(day<10)
				dateString.append(0).append(day);
			else
				dateString.append(day);
			
			return dateString.toString();
			
		}catch(Exception e){
			log.error("",e);
		}
		
		return null;
		
	}
	
	
	public static String  getDate(int i){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
		Calendar calendar = Calendar.getInstance();	
		calendar.add(Calendar.DATE,i);
		Date date=calendar.getTime();
		String d=sf.format(date);
		return d;
		
	}
	
public static List<String> string2List(String s){
		
		
		if(s==null||s.trim().equals(""))
			return null;
		
		List<String> l= new ArrayList<String>();
		
		String[] array=s.split(",");
		int len=array.length;
		
		for(int i=0;i<len;i++){
			String value=array[i];
			if(!value.startsWith("#")){
				l.add(value);
				//log.debug("FileUtil add "+value+" to List");
			}	
		}
		return l;
	}
	
	
	public static String processfileName(String fileName){
		
		
		//System.err.println(fileName);
		if(fileName==null){
			System.err.println("fileName is null");
			return null;
		}
			
		String[] fileNameArray= new String[2];// first element is fileName, second one is the timestamp
		
		String[] fileNameExt = fileName.split("\\.");
		if (fileNameExt.length != 2) 
			return null;
		
		String tmpfileName = fileNameExt[0];
		int lastIndex=tmpfileName.lastIndexOf("_");
		int len=tmpfileName.length();
		//fileNameArray[0]=tmpfileName.substring(0, lastIndex);
		//fileNameArray[1]=tmpfileName.substring(lastIndex+1,len);
		//return fileNameArray;
		return tmpfileName.substring(0, lastIndex);
		
		
	}

	public static List<Row> processFile(String fileName){
		Long start=System.currentTimeMillis();
		String tableName=processfileName(fileName);
		String basePath=Properties.getProperty("tmg.flatfile.path");
		//basePath="C:/work/ODSTest/";
		
		String delimiter=Properties.getProperty("tmg.flatfile.delimiter");
		List<Row> list= new ArrayList<Row>();
		
		String path=basePath+tableName+"/"+fileName;
		
		BufferedReader reader=null;
		
		try{
			
			reader= new BufferedReader(new FileReader(new File(path)));
			String line=reader.readLine();
			
			while(line!=null){
				String[] lines=line.split("\t");
				String operation=lines[1];//delete,insert,update
				lines=lines[3].split(delimiter);
				Map<String,String> map= new HashMap<String,String>();
				map.put("oprn", operation);
				for(int i=0;i<lines.length;i+=2){
					String key=lines[i].toLowerCase();
					String value=lines[i+1];
					map.put(key,value);
				}
				line=reader.readLine();
				Row row= new Row(operation,map);
				list.add(row);
			}
		}catch(Exception e){
			log.info("",e);	
		}finally{
			
			try{
				reader.close();
			}catch(Exception e){
				log.info("close file error"+path,e);
			}
			
			
		}
		
		
		
		Long end=System.currentTimeMillis();
		Long used=end-start;
		log.info("process file "+fileName+" using "+used);
		return list;
		
		
	}
	
	
	public static List<String> getSql(String fileName){
		
		File file=new File(fileName);
		if(!file.exists()||file.isDirectory()){
			log.info("No such file"+fileName);
			return null;
		}
		
		List<String> sqlList= new ArrayList<String>();
		
		BufferedReader reader=null;
		String delimiter1=",\"statement: ";//the white space 
		String delimiter2="\",,,,,,\"";
		
		try{
			
			reader= new BufferedReader(new FileReader(file));
			
			String line=null,sql=null;
			
			
			do{
				line=reader.readLine();
				if(line==null)
					break;
				String[] tmp1=line.split(delimiter1);
				if(tmp1.length!=2){
					log.info("this line is invalid"+line);
					continue;
				}
				
				String[] tmp2=tmp1[1].split(delimiter2);
				sql=tmp2[0];
				sqlList.add(sql);
				
				
			}while(line!=null);
			
		
		}catch(Exception e){
			
			log.info("",e);
		}finally{
			
			try{
				reader.close();
			}catch(Exception e){
				
				log.error("close file error",e);
			}
			
			
		}
		
		
		
		return sqlList;
		
		
	}
	
	

	
	public static void main(String[] args){
		
		/*
		//String dir="C:\\work\\SVN\\ODS\\GF_ODS\\Standard_Version\\Dev_Sprint\\03_Source_Code\\01_Database\\01_DDL\\Gemfire";
		String dir=Constant.prd_path_windows;
		String ext=".log";
		String date="2015-09-10";
		List<String> fList=getFileList(dir,ext,date);
		
		
		Iterator<String> ite=fList.iterator();
		while(ite.hasNext())
			System.out.println(ite.next());
		*/
		
		/*
		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-1);
		Date yesterday=calendar.getTime();
		String date=df.format(yesterday);
		System.out.println(date);
		*/
		//System.out.println(formatDate("08Jan2015"));

		/*
		String[] fileNameArray=processfileName("phone_1441845849150");
		if(fileNameArray==null)
			System.err.println("invliad file");
		else{
			
		System.out.println(fileNameArray[0]);
		System.out.println(fileNameArray[1]);
		}
		*/
		
		String fileName="organizations_1450305159234.txt";
		List<Row> list=processFile(fileName);
		
		Iterator<Row> itera=list.iterator();
		System.out.println("hello");
		while(itera.hasNext()){
			
			Row row=itera.next();
			String operator=row.getOperation();
			Map<String,String> map=row.getMap();
			for(String key:map.keySet()){
				String value=map.get(key);
				if(key.equals("VER")&&operator.equals("DELETE"))
					value="-"+value;
				System.out.print(value+",");		
			}
			
			System.out.println();
			
			
		}
		


	}
	
	
	
	
	

}


