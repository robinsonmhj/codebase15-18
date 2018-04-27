/**
 * 
 */
package com.tmg.gf.DAOImp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.tmg.gf.Model.RoleModel;

/**
 * @author Haojie Ma
 * @date Mar 11, 2016
 */
public class RoleDAOImp {
	
	
private Logger log=Logger.getLogger(RoleDAOImp.class);
	
	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<RoleModel> getAll() {
		Session session =null;
		List<RoleModel> list=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RoleModel.class);
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}

	@SuppressWarnings("unchecked")
	public String getPrivilegeById(int roleId) {
		Session session =null;
		List<String> list=null;
		String privilege=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RoleModel.class);
			criteria.add(Restrictions.eq("role_id",roleId));
			criteria.setProjection(Property.forName("privilege"));
			list = criteria.list();
			if(list!=null&&list.size()!=0)
				privilege=list.get(0);
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return privilege;

	}
	
	
	@SuppressWarnings("unchecked")
	public String getRoleNameById(int roleId) {
		Session session =null;
		List<String> list=null;
		String privilege=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RoleModel.class);
			criteria.add(Restrictions.eq("role_id",roleId));
			criteria.setProjection(Property.forName("role_name"));
			list = criteria.list();
			if(list!=null&&list.size()!=0)
				privilege=list.get(0);
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return privilege;

	}

}


