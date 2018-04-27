/**
 * 
 */
package com.barclaycard.us.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barclaycard.us.model.Bag;
import com.barclaycard.us.util.AuxiliaryUtil;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class BagServiceImpl implements BagService{
	
	private Map<Long,Bag> map= new HashMap<Long,Bag>();
	
	public BagServiceImpl(){
		List<Bag> bags=AuxiliaryUtil.getBags();
		for(Bag b:bags)
			map.put(b.getId(),b);
	}
	
	public int getFlightIdByBagId(long bagId){
		
		Bag b=map.get(bagId);
		if(b==null)
			return -1;
		return b.getFlightId();
		
	}
	
	
	public short getGateIdByBagId(long bagId){
		
		Bag b=map.get(bagId);
		if(b==null)
			return -1;
		return b.getEntryGateId();
	}
	
	
}
