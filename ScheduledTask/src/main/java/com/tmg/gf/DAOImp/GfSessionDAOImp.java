/**
 * 
 */
package com.tmg.gf.DAOImp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.tmg.gf.DAO.GfSessionDAO;
import com.tmg.gf.Model.GfSession;

/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */

public class GfSessionDAOImp implements GfSessionDAO {
	
	private Logger log= Logger.getLogger(GfSessionDAOImp.class);

	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSlowQuery() {
		
		List<String> sessionList =null;
		Session session=null;
		try{
			
			ProjectionList list=Projections.projectionList();
			
			
			list.add(Projections.property("CURRENT_STATEMENT_UUID"));
			session= this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(GfSession.class);
			criteria.add(Restrictions
					.sqlRestriction("CURRENT_STATEMENT_STATUS like '%EXECUTING%' "));
			criteria.add(Restrictions
					.sqlRestriction("CURRENT_STATEMENT_ELAPSED_TIME >=30 "));
			criteria.setProjection(list);//only extract the CURRENT_STATEMENT_UUID column
			sessionList = criteria.list();
		}catch(Exception e){
			log.error("error in getSlowQuery()",e);
			
			
		}finally{
			if(session!=null)
				session.close();
		}
		
		
		return sessionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GfSession> getByUUID(String uuid) {
		Session session =null;
		List<GfSession> sessionList=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(GfSession.class);
			
			criteria.add(Restrictions.sqlRestriction("CURRENT_STATEMENT not like '%monitor.query%' and CURRENT_STATEMENT not like '%sys.sessions%'"));
			criteria.add(Restrictions.ne("USER_ID", "serveradmin"));
			if (uuid != null) {
				criteria.add(Restrictions.eq("CURRENT_STATEMENT_UUID", uuid));
			}

			sessionList= criteria.list();
		}catch(Exception e){
			
			log.error("Exception in getByUUID",e);
			
		}finally{
			if(session!=null)
				session.close();
			
		}

		
		
		return sessionList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GfSession> getQueryByUserName(String username) {
		Session session =null;
		List<GfSession> sessionList=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(GfSession.class);
			criteria.add(Restrictions.sqlRestriction("LOWER(user_id) like '"+username+"' and client_bind_address!='/10.67.35.12'"));
			//criteria.add(Restrictions.eq("USER_ID", username));
			sessionList= criteria.list();
		}catch(Exception e){
			
			log.error("Exception in getQueryByUserName",e);
			
		}finally{
			if(session!=null)
				session.close();
			
		}

		
		
		return sessionList;
	}
	
	
}
