/**
 * 
 */
package com.barclaycard.us.model;

/**
 * @author Haojie Ma
 * Mar 23, 2018
 */
public class Bag {
	
	private long id;
	private long travelerId;
	private short entryGateId;
	private int flightId;
	
	public Bag(){
		
		
	}
	
	public Bag(long id,short entryGateId,int flightId){
		this.id=id;
		this.entryGateId=entryGateId;
		this.flightId=flightId;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTravelerId() {
		return travelerId;
	}
	public void setTravelerId(long travelerId) {
		this.travelerId = travelerId;
	}
	public short getEntryGateId() {
		return entryGateId;
	}
	public void setEntryGateId(short entryGateId) {
		this.entryGateId = entryGateId;
	}
	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	
	public int hashCode(){
		return (int)id;
		
	}
	
	public boolean equals(Object obj){
		if(!obj.getClass().isInstance(this))
			return false;
		Bag o=(Bag)obj;
		return o.id==this.id;
		
	}
	

}
