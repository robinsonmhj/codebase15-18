/**
 * 
 */
package com.barclaycard.us.service;



/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public interface BagService {
	
	public short getGateIdByBagId(long bagId);
	public int getFlightIdByBagId(long bagId);
	

}
