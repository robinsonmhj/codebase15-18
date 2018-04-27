/**
 * 
 */
package com.barclaycard.us.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.barclaycard.us.util.PathUtil;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class PathServiceImpl implements PathService {
	
	
	private static Logger log=Logger.getLogger(PathServiceImpl.class);
	private BagService bagService;
	private DepartureService departureService;
	private PathUtil pathUtil;
	
	
	public PathServiceImpl(){
		bagService= new BagServiceImpl();
		//flightService= new FlightServiceImpl();
		departureService= new DepartureServiceImpl();
		pathUtil= PathUtil.getInstance();
		
	}
	
	
	public String getPath(long bagId){
		String path=null;
		StringBuilder res= new StringBuilder();
		res.append(bagId);
		res.append(",");
		short fromGateId=bagService.getGateIdByBagId(bagId);
		int flightId=bagService.getFlightIdByBagId(bagId);
		log.info("flightId="+flightId);
		int toGateId=departureService.getGateIdByFlightId(flightId);
		log.info("toGateId="+toGateId);
		if(fromGateId<0){
			path="The bag doesn't exist";
			log.error(bagId+" doesn't exist");
		}else if(toGateId<0){
			path="departure gate "+toGateId+" doesn't exist";
			log.error("departure gate "+toGateId+" doesn't exist");
		}else
			path=pathUtil.getShortTime(fromGateId, toGateId);
		
		
		res.append(path);
		return  res.toString();
		
		
	}
	
	
	public List<String> getPath(List<Long> bagList){
		
		List<String> res= new ArrayList<String>();
		for(long bagId:bagList){
			String p=getPath(bagId);
			res.add(p);
			
		}
		
		return res;
		
	}
	

}
