/**
 * 
 */
package com.tmxxx.Bean;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Haojie Ma
 * @date Sep 23, 2015
 */
public class Container<E> {
	

	private Queue<E> processingQueue;
	
	
	public Container(){
		processingQueue= new LinkedList<E>();
	}
	
	
	public Container(Queue<E> processingQueue){
		this.processingQueue= processingQueue;
	}
	

	
	public synchronized  void add2Queue(E e){	
		processingQueue.add(e);
	}

	
	public synchronized  E get(){	
		if(processingQueue.isEmpty())
			return null;
		else
			return processingQueue.remove();

	}


	

}


