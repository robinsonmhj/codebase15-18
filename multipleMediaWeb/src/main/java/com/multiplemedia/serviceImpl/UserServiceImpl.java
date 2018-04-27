/**
 * 
 */
package com.multiplemedia.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.multiplemedia.dao.GenericDao;
import com.multiplemedia.dao.UserDao;
import com.multiplemedia.model.User;
import com.multiplemedia.service.UserService;





/**
 * @author Haojie Ma
 * @date Dec 14, 2015
 */
@Service
public class UserServiceImpl extends GenericServiceImpl<User,String> implements UserService{

	private UserDao userDao;
	public UserServiceImpl(){
		
		
	}
	
	
	@Autowired
    public UserServiceImpl(@Qualifier("UserDaoImpl")GenericDao<User, String> genericDao) {
        super(genericDao);
        this.userDao = (UserDao) genericDao;
    }
	
    

	/*
    public UserServiceImpl(GenericDao<User, String> genericDao) {
        super(genericDao);
        this.userDao = (UserDao) genericDao;
    }
	*/
	 
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
	public boolean removeUser(String username){
		return userDao.removeUser(username);
	}
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly=true)
    public boolean exist(String userName, String password){
    	return userDao.exist(userName, password);
    	
    }
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public User getUser(String username){
    	
    	return userDao.getUser(username);
    	
    }
    
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<String> getPrivilegeList(String userName){
    	
    	return userDao.getPrivilegeList(userName);
    	
    }
	
}


