/**
 * 
 */
package com.barclaycard.us.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barclaycard.us.model.Gate;
import com.barclaycard.us.util.AuxiliaryUtil;

/**
 * @author Haojie Ma Mar 25, 2018
 */
public class GateServiceImpl implements CommonService {

	private Map<Short, Gate> map = new HashMap<Short, Gate>();
	private List<Gate> gates;

	public GateServiceImpl() {
		gates = AuxiliaryUtil.getGates();
		for (Gate g : gates)
			map.put(g.getId(), g);
	}

	public String getNameById(int gateId) {
		Gate g = map.get(gateId);
		if (g == null) {
			return null;
		}
		return g.getName();
	}
	
	public int getIdByName(String gateName){
		
		for(Gate g:gates){
			if(g.getName().equals(gateName))
				return g.getId();
		}
		
		return -1;
		
	}
	

}
