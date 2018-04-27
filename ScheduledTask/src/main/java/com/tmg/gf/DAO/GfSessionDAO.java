/**
 * 
 */
package com.tmg.gf.DAO;

import java.util.List;


/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */
public interface GfSessionDAO {
	
    
    public <T> List<T> getSlowQuery();
    public <T> List<T> getByUUID(String UUID);
    public <T> List<T> getQueryByUserName(String username);
    
    
}


