/**
 * 
 */
package com.barclaycard.us.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barclaycard.us.model.Flight;
import com.barclaycard.us.util.AuxiliaryUtil;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class FlightServiceImpl implements CommonService {
	private Map<Integer, Flight> map = new HashMap<Integer, Flight>();
	private List<Flight> flights;

	public FlightServiceImpl() {
		flights = AuxiliaryUtil.getFlights();
		for (Flight f : flights)
			map.put(f.getId(), f);
	}

	public String getNameById(int flightId) {
		Flight f = map.get(flightId);
		if (f == null) {

			return null;
		}

		return f.getName();

	}

	
	public int getIdByName(String flightName){
		
		for(Flight f:flights){
			if(f.getName().equals(flightName))
				return f.getId();
			
		}
		
		
		return -1;
		
	}
	
	
	
	

}
