/**
 * 
 */
package com.barclaycard.us.service;

import java.util.List;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public interface PathService {
	
	public List<String> getPath(List<Long> bagIds);
	public String getPath(long bagId);
	
	

}
