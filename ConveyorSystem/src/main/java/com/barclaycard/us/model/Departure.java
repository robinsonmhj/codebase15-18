/**
 * 
 */
package com.barclaycard.us.model;

import java.sql.Time;

/**
 * @author Haojie Ma
 * Mar 23, 2018
 */
public class Departure {
	
	private int flightId;
	private short gateId;
	private int destinationId;
	private Time departureTime;
	
	public Departure(){
		
		
	}
	
	public Departure(int fligtId,short gateId,int destinationId,Time departureTime){
		
		this.flightId=fligtId;
		this.gateId=gateId;
		this.destinationId=destinationId;
		this.departureTime=departureTime;
		
	}
	
	public Departure(int fligtId,short gateId,int destinationId){
		
		this.flightId=fligtId;
		this.gateId=gateId;
		this.destinationId=destinationId;
		
	}
	
	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	public short getGateId() {
		return gateId;
	}
	public void setGateId(short gateId) {
		this.gateId = gateId;
	}
	public int getDesinationId() {
		return destinationId;
	}
	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}
	public Time getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}
	
	public int hashCode(){
		return flightId;
	}
	
	public boolean equals(Object obj){
		if(!obj.getClass().isInstance(this))
			return false;
		Departure o=(Departure)obj;
		return o.flightId==this.flightId;
		
		
	}
	
	

}
