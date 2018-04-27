/**
 * 
 */
package com.multiplemedia.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.multiplemedia.dao.UserDao;
import com.multiplemedia.model.User;

/**
 * @author Haojie Ma
 * @date Dec 11, 2015
 */

@Repository("UserDaoImpl")
public class UserDaoImpl extends GenericDaoImpl<User,String> implements UserDao{
	
	
	@Override
	public boolean removeUser(String userName){
		 Query query = currentSession().createQuery("from user u where userName=:username");
		 query.setParameter("username", userName);
	     return query.executeUpdate() > 0;
		
	}

	//support username and wechat to login
	@Override
    public boolean exist(String userName, String password){
		Query query = currentSession().createQuery("select 'A' from User u where (userName=:username or weichat=:username) and password=:password");
		query.setParameter("username", userName);
		query.setParameter("password", password);
        return query.list().size() > 0;
		
	}
	@Override
    public User getUser(String userName){
		Query query = currentSession().createQuery("from User where userName=:username");
        query.setParameter("username", userName);
        return (User) query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPrivilegeList(String userName){
		Query query = currentSession().createQuery("select l.linkUrl from Link l, Privilege p where l.linkId=p.linkId and p.userName=:username");
		query.setParameter("username", userName);
		return query.list();
		
	}
	
	
}


