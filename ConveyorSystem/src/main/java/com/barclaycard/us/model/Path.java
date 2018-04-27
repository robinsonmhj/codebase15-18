/**
 * 
 */
package com.barclaycard.us.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Haojie Ma
 * Mar 24, 2018
 */
public class Path {
	
	// think about change from, to to Gate
	private int to; //it is the identity 
	private int from;
	private float time=Float.MAX_VALUE;//default value
	private Queue path= new LinkedList<Integer>();
	
	
	public Path(){
		
	}
	
	public Path(int from,int to,float time){
		this.from=from;
		this.to=to;
		this.time=time;
	}
	
	public Path(int from,int to){
		this.from=from;
		this.to=to;
	}
	
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
	public Queue getPath() {
		return path;
	}
	public void setPath(Queue path) {
		this.path = path;
	}
	
	public boolean Equals(Object obj){
		if(!obj.getClass().isInstance(this))
			return false;
		
		Path o=(Path)obj;
		return o.to==this.to;
		
	}
	
	public int hashCode(){
		return to;
	}
	
	

}
