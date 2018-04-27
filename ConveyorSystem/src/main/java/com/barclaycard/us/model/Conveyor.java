/**
 * 
 */
package com.barclaycard.us.model;

/**
 * @author Haojie Ma
 * Mar 23, 2018
 */

//you can treat this class as Edge in Graph theory
public class Conveyor {
	
	private short from;
	private short to;
	private float time;
	
	public Conveyor(){

	}
	
	
	public Conveyor(short from,short to,float time){
		this.from=from;
		this.to=to;
		this.time=time;
	}
	
	public short getFrom() {
		return from;
	}
	public void setFrom(short from) {
		this.from = from;
	}
	public short getTo() {
		return to;
	}
	public void setTo(short to) {
		this.to = to;
	}
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
	
	public int hashCode(){
		
		return from+to;
		
	}
	
	
	public boolean equals(Object obj){
		if(!obj.getClass().isInstance(this))
			return false;
		
		Conveyor o=(Conveyor)obj;
		return (o.from==this.from&&o.to==this.to);
		
	}
	

	public String toString(){
		return from+","+to+","+time;
		
	}
}
