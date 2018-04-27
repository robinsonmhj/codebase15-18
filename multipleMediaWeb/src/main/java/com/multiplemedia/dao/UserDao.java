/**
 * 
 */
package com.multiplemedia.dao;

import java.util.List;

import com.multiplemedia.model.User;

/**
 * @author Haojie Ma
 * @date Dec 11, 2015
 */
public interface UserDao extends GenericDao<User,String>{
	
	public boolean removeUser(String userName);
    public boolean exist(String userName, String password);
    public User getUser(String userName);
    public List<String> getPrivilegeList(String userName);
   

}


