/**
 * 
 */
package com.tmg.thread;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tmg.Bean.MapContainer;
import com.tmg.Bean.TableContainer;
import com.tmg.core.Properties;

/**
 * @author Haojie Ma
 * @date Dec 18, 2015
 */
@Component
public class FileList implements Runnable {

	private Logger log=Logger.getLogger(FileList.class);
	private TableContainer container;
	private MapContainer<String,String[]> mapContainer;
	
	
	public FileList(){
		
		
	}
	
	
	public FileList(TableContainer container,MapContainer<String,String[]> mapContainer){
		this.container=container;
		this.mapContainer=mapContainer;
		
	}
	
	
	
	public void run(){
		
		String path=Properties.getProperty("tmg.flatfile.path");
		File directory;
		
		while(true){
			
			String tableName=container.getTableFromQueue();
			if(tableName==null)
				return;
			
			directory=new File(path+tableName);
			String[] fileList=directory.list();
			
			if(fileList==null||fileList.length==0){
				log.info(tableName+" doesn't have any files");
				continue;
			}
				
			
			mapContainer.add2Queue(tableName, fileList);
			
			log.info(tableName+" has file "+fileList);
			
		}
	}



	public TableContainer getContainer() {
		return container;
	}



	public void setContainer(TableContainer container) {
		this.container = container;
	}


	public MapContainer<String, String[]> getMapContainer() {
		return mapContainer;
	}


	public void setMapContainer(MapContainer<String, String[]> mapContainer) {
		this.mapContainer = mapContainer;
	}



	
	
	
	
	
	
	
}


