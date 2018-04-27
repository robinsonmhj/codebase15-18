/**
 * 
 */
package com.tmg.core;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Haojie Ma
 * @date Apr 22, 2016
 */
public class Container<T> {
	
	Queue<T> queue;
	
	public Container(Queue<T> queue){
		this.queue=queue;
	}
	
	public Container(){
		queue= new LinkedList<T>();
	}
	
	
	synchronized public T get(){
		
		T value=queue.poll();
		
		return value;
		
	}
	
	
	synchronized public void put(T value){
		
		queue.add(value);
		
	}
	

}


