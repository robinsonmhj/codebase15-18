/**
 * 
 */
package com.tmg.Bean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Component;

/**
 * @author Haojie Ma
 * @date Dec 18, 2015
 */

public class MapContainer<K,V> {
	
	private Queue<K> queue; 
	private Map<K,V> map;
	
	
	public MapContainer(){
		queue=new LinkedList<K>();
		map= new HashMap<K,V>();
		
	}
	
	public synchronized  void add2Queue(K key,V value){
		
		queue.add(key);
		map.put(key, value);
		
	
	}
	
	
	public synchronized  V getFromQueue(){	
		if(queue.isEmpty())
			return null;
		else{
			K key= queue.remove();
			return map.get(key);
		}
			

	}
	
	
	
	

}


