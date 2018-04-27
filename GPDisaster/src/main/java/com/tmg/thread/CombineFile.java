/**
 * 
 */
package com.tmg.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tmg.Bean.TableContainer;
import com.tmg.core.Properties;

/**
 * @author Haojie Ma
 * @date Nov 6, 2015
 */
public class CombineFile implements Runnable{
	
	private static Logger log=Logger.getLogger(CombineFile.class);
	private Map<String,List<String>> map;// key:table name,value:partition table name
	private TableContainer tableProducer;// contains the tables names without partition
	private TableContainer tableConsumer;// contains the tables names without partition
	private Map<String,List<String>> processedMap;
	
	
	public CombineFile(){	
		
	}
	
	
	
	
	
	public Map<String, List<String>> getMap() {
		return map;
	}





	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}





	public TableContainer getTableProducer() {
		return tableProducer;
	}





	public void setTableProducer(TableContainer tableProducer) {
		this.tableProducer = tableProducer;
	}





	public TableContainer getTableConsumer() {
		return tableConsumer;
	}





	public void setTableConsumer(TableContainer tableConsumer) {
		this.tableConsumer = tableConsumer;
	}


	


	public Map<String, List<String>> getProcessedMap() {
		return processedMap;
	}





	public void setProcessedMap(Map<String, List<String>> processedMap) {
		this.processedMap = processedMap;
	}





	public void run(){
		
		String tableName,patitionTableName;
		String path=Properties.getProperty("tmg.gpfdist.dir");
		String ext=Properties.getProperty("tmg.gpfdist.file.extension");
		while(true){
			tableName=tableConsumer.getTableFromQueue();
			if(tableName==null){
				/*try{
					Thread.sleep(5000);
					log.info(Thread.currentThread().getName()+" is going to sleep for 5 seconds");
					
				}catch(Exception e){
					log.info("I am in trouble, I cannot sleep");
				}
				continue;
				*/
				break;
			}
				
			List<String> partitionList=processedMap.get(tableName);
			if(partitionList==null||partitionList.isEmpty())
				continue;
			
			/*
			List<String> processedList=processedMap.get(tableName);
			if(processedList==null){
				log.debug("No such table:"+tableName+" in the processedMap");
				continue;
			}
			int count=partitionList.size();
			while(true){
				int processedCount=0;
				processedCount=processedList.size();
				if(count==processedCount)
					break;
				else{
					try{
						log.info(tableName+","+"processedCount="+processedCount+",total="+count);
						log.info(Thread.currentThread().getName()+" is going to sleep for 5 seconds");
						Thread.sleep(5000);
					}catch(Exception e){
						log.info("",e);
					}
					
					processedList=processedMap.get(tableName);
				}
			}
			*/
			Iterator<String> iterator=partitionList.iterator();
			boolean flag=true;//flag indicating first row
			BufferedWriter writer=null;
			BufferedReader reader=null;
			//List<File> fileList= new ArrayList<File>();
			
				
				//System.out.println("Final name:"+path+tableName+ext);
				File finalFile=new File(path+tableName+ext);
				while(iterator.hasNext()){
					patitionTableName=iterator.next();
					//if the table is not partitioned, we don't need to combine the files
					String regex=".*_[0-9]{1,}_prt_.*";
					if(!patitionTableName.matches(regex))
						break;
					File file= new File(path+patitionTableName+ext);
					//System.out.println(file.getAbsolutePath());
					//sometimes the partition doesn't contain any data, so the file name is not generated
					if(!file.exists())
						continue;

					//fileList.add(file);
					try{
					if(flag){
						flag=false;
						writer= new BufferedWriter(new FileWriter(finalFile,true));
					} 
					reader= new BufferedReader(new FileReader(file));
					String line=reader.readLine();
					while(line!=null){
							writer.write(line);
							writer.write("\n");
							line=reader.readLine();
					}
					
				}catch(Exception e){
					log.info("",e);
				}finally{
					try{	
						reader.close();
						//delete the file right after it is processed may save some disk
						try{
							Files.delete(file.toPath());
						}catch(Exception e){
							log.error("delete file "+file.getAbsolutePath()+" error",e);
						}
					}catch(Exception e){
						log.error("Close file error",e);
					}
				}
			}
			
				
			try{
				if(writer!=null)
					writer.close();
			}catch(Exception e){
				
				log.info("Close writer error",e);
				
			}
			
			/*
			Iterator<File> removeIterator=fileList.iterator();
			while(removeIterator.hasNext()){
				File file=removeIterator.next();
				try{
					Files.delete(file.toPath());
				}catch(Exception e){
					log.error("delete file "+file.getAbsolutePath()+" error",e);
				}
			}
			*/
			log.debug("add table:"+tableName);
			tableProducer.add2Queue(tableName);
			
	}
	}
	

}


