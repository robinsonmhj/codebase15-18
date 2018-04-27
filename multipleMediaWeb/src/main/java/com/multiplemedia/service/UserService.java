/**
 * 
 */
package com.multiplemedia.service;

import java.util.List;


import com.multiplemedia.model.User;

/**
 * @author Haojie Ma
 * @date Dec 14, 2015
 */

public interface UserService extends GenericService<User,String>{
	public boolean removeUser(String userName);
    public boolean exist(String userName, String password);
    public User getUser(String userName);
    public List<String> getPrivilegeList(String userName);
}


