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

import com.tmg.gf.DAO.MonitorDAO;
import com.tmg.gf.Model.QueryKilledLog;


/**
 * @author Haojie Ma
 * @date Jun 11, 2015
 */
public class QueryKilledLogDAOImp implements MonitorDAO{
	
private Logger log=Logger.getLogger(QueryDetailDAOImp.class);
	
	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<QueryKilledLog> getAll() {
		Session session =null;
		List<QueryKilledLog> list=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(QueryKilledLog.class);
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}

	
	public void insert(QueryKilledLog detail) {
		Session session = null;

		
		try{
			session = this.sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(detail);
			session.getTransaction().commit();

			
		}catch(Exception e){
			
			log.info("KillLog:"+detail);
			log.error("insert error",e);
			
		}finally{
			session.close();
			
		}
		
		

	}
	
	
	

}


