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

import com.tmg.gf.DAO.MonitorDAO;
import com.tmg.gf.Model.GroupModel;


/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */
public class GroupDAOImp implements MonitorDAO {

	
	private Logger log=Logger.getLogger(GroupDAOImp.class);
	
	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<GroupModel> getAll() {
		Session session =null;
		List<GroupModel> list=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(GroupModel.class);
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}
	
	@SuppressWarnings("unchecked")
	public int getIdByName(String group) {
		Session session =null;
		int group_id=-1;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(GroupModel.class);
			criteria.add(Restrictions.eq("group_name", group));
			criteria.setProjection(Property.forName("group_id"));//only extract the user_id column
			List<Object> list = criteria.list();
			if(list!=null)
				group_id=(int)list.get(0);
			
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return group_id;

	}
	
	
	@SuppressWarnings("unchecked")
	public String getNameById(int groupId) {
		Session session =null;
		String groupName=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(GroupModel.class);
			criteria.add(Restrictions.eq("group_id", groupId));
			criteria.setProjection(Property.forName("group_name"));//only extract the group_name column
			List<String> list = criteria.list();
			if(list!=null)
				groupName=list.get(0);
			
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return groupName;

	}
	
	
}
