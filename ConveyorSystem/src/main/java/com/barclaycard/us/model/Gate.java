/**
 * 
 */
package com.barclaycard.us.model;

/**
 * @author Haojie Ma
 * Mar 23, 2018
 */

//you can think this class as vertex in graph theory
public class Gate {
	
	private short id;
	private String name;
	
	public Gate(short id,String name){
		this.id=id;
		this.name=name;
	}
	
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int hashCode(){
		
		return id;
		
	}
	
	public boolean equals(Object obj){
		if(!obj.getClass().isInstance(this))
			return false;
		Gate o=(Gate)obj;
		return this.id==o.id;
		
	}
	
	

}
