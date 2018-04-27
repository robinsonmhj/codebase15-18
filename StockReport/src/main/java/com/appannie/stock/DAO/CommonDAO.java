/**
 * 
 */
package com.appannie.stock.DAO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Haojie Ma
 * @date Jun 4, 2016
 */
public interface CommonDAO {
	
	public Map<Integer,String> getSymbol();
	public Map<Integer,BigDecimal> getLastChange();
	
	

}


