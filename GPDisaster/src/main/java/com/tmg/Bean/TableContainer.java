/**
 * 
 */
package com.tmg.Bean;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Haojie Ma
 * @date Sep 23, 2015
 */
public class TableContainer {
	

	//tableName=schema.table
	private Queue<String> processingQueue;
	
	
	public TableContainer(){
		processingQueue= new LinkedList<String>();
	}
	
	
	public TableContainer(Queue<String> processingQueue){
		this.processingQueue= processingQueue;
	}
	

	
	public synchronized  void add2Queue(String tableName){
		
		processingQueue.add(tableName);
		
	
	}
	
	
	public synchronized  String getTableFromQueue(){	
		if(processingQueue.isEmpty())
			return null;
		else
			return processingQueue.remove();

	}


	

}


