/**
 * 
 */
package com.tmg.gf.DAOImp;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tmg.gf.DAO.MonitorDAO;
import com.tmg.gf.Model.QueryDetail;

/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */
public class QueryDetailDAOImp implements MonitorDAO {

	
	private Logger log=Logger.getLogger(QueryDetailDAOImp.class);
	
	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<QueryDetail> getAll() {
		Session session =null;
		List<QueryDetail> list=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(QueryDetail.class);
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}

	
	public void insert(QueryDetail detail) {
		Session session = null;

		
		try{
			session = this.sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(detail);
			session.getTransaction().commit();

			
		}catch(Exception e){
			
			log.info("queryDetail:"+detail);
			log.error("insert error",e);
			
		}finally{
			session.close();
			
		}
		
		

	}
	
	
	public void update(QueryDetail detail){
		
		Session session=null;
		try{
			String uuid=detail.getSTATEMENT_UUID();
			long time=detail.getSTATEMENT_ELAPSED_TIME();
			
			session=this.sessionFactory.openSession();
			
			Query query=session.createQuery("update QueryDetail set STATEMENT_ELAPSED_TIME=:elapsed_time where STATEMENT_UUID=:uuid");
			
			query.setBigInteger("elapsed_time", BigInteger.valueOf(time));
			query.setString("uuid", uuid);
		  
			
			query.executeUpdate();
		}catch(Exception e){
			
			log.error("",e);
		}finally{
			if(session!=null)
				session.close();
		}
		
		
		
		
	}

}
