/**
 * 
 */
package com.barclaycard.us.model;

/**
 * @author Haojie Ma
 * Mar 23, 2018
 */
public class Flight {
	
	private int id;
	private String name;
	private int airlineId;
	
	public Flight(){
		
		
	}
	
	public Flight(int id,String name){
		this.id=id;
		this.name=name;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAirlineId() {
		return airlineId;
	}
	public void setAirlineId(int airlineId) {
		this.airlineId = airlineId;
	}
	
	
	public int hashCode(){
		return id;
		
	}

	public boolean equals(Object obj){
		
		if(!obj.getClass().isInstance(this))
			return false;
		Flight o=(Flight)obj;
		return o.id==this.id;
	}
}
