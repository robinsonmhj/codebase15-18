/**
 * 
 */
package com.tmg.gf.DAOImp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.tmg.gf.DAO.MonitorDAO;
import com.tmg.gf.Model.RequestHistoryModel;


/**
 * @author Haojie Ma
 * @date Jun 11, 2015
 */
public class RequestHistoryDAOImp implements MonitorDAO{
	
private Logger log=Logger.getLogger(RequestHistoryDAOImp.class);
	
	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<RequestHistoryModel> getAll() {
		Session session =null;
		List<RequestHistoryModel> list=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RequestHistoryModel.class);
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}

	
	
	@SuppressWarnings("unchecked")
	public String getPreviousSchemaByUserNameGroupName(String userName,int groupId){
		List<String> list=null;
		String schema=null;
		Session session =null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RequestHistoryModel.class);
			criteria.add(Restrictions.eq("user_name", userName));
			criteria.add(Restrictions.eq("group_id", groupId));
			criteria.addOrder(Order.desc("insert_time"));
			criteria.setProjection(Property.forName("schema_name"));//only extract the schema_name column
			list=criteria.list();
			if(list!=null&&!list.isEmpty())
				schema=list.get(0);
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return schema;
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getLatestUserNameByGroupId(List<Integer> maxList,int groupId){
		List<String> list=null;
		Session session =null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RequestHistoryModel.class);
			criteria.add(Restrictions.in("request_id", maxList));
			criteria.add(Restrictions.eq("group_id", groupId));
			criteria.setProjection(Property.forName("user_name"));
			list=criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Integer> getMaxRequestIdBySchema(String schemaName){
		List<Integer> list=new ArrayList<Integer>(0);
		Session session =null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RequestHistoryModel.class);
			criteria.add(Restrictions.eq("schema_name", schemaName));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("user_name"));
			projectionList.add(Projections.max("request_id"));
			criteria.setProjection(projectionList);
			List<Object[]> tmp=criteria.list();
			for(Object[] value:tmp){
				list.add((Integer)value[1]);
			}
			
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}
	
	public void insert(RequestHistoryModel detail) {
		Session session = null;

		
		try{
			session = this.sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(detail);
			session.getTransaction().commit();

			
		}catch(Exception e){
			
			log.info("user:"+detail);
			log.error("insert error",e);
			
		}finally{
			session.close();
		}
		

	}
	
	
	

}


