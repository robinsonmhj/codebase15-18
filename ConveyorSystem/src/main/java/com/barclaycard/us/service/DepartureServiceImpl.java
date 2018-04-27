/**
 * 
 */
package com.barclaycard.us.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.barclaycard.us.model.Departure;
import com.barclaycard.us.util.AuxiliaryUtil;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class DepartureServiceImpl implements DepartureService{
	private Logger log= Logger.getLogger(DepartureServiceImpl.class);
	private Map<Integer,Departure> map= new HashMap<Integer,Departure>();
	private List<Departure> departures;
	
	public DepartureServiceImpl(){
		
		departures= AuxiliaryUtil.getDepartures();
		for(Departure d:departures)
			map.put(d.getFlightId(), d);
	}
	
	
	public int getGateIdByFlightId(int flightId){
		
		//special case, flightId=0 which means it will go to the bagclaim in the airport
		if(flightId==0){
			final String gateName="BaggageClaim";
			CommonService gateService=new GateServiceImpl();
			return gateService.getIdByName(gateName);
		}

		Departure d=map.get(flightId);
		if(d==null){
			log.error("Invalid request, using flightId="+flightId);
			return -1;
		}
		log.info(d.getFlightId()+","+d.getGateId()+","+d.getDesinationId()+","+d.getDepartureTime());
		return d.getGateId();
		
	}
	
	//the method is heavy, you are recommended to call getGateIdByFlightId();
	public int getGateIdByFlightName(String flightName){
		
		//special case to handle when the flightName is arrival
		if("ARRIVAL".equals(flightName.toUpperCase())){
			CommonService gateService=new GateServiceImpl();
			return gateService.getIdByName("BaggageClaim");
		}
		
		CommonService flightService=new FlightServiceImpl();
		int flightId=flightService.getIdByName(flightName);
		return getGateIdByFlightId(flightId);
		
		
			
	}
	

}
