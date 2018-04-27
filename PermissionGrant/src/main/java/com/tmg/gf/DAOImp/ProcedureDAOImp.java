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
import org.hibernate.criterion.Restrictions;

import com.tmg.gf.DAO.MonitorDAO;
import com.tmg.gf.Model.ProcedureModel;

/**
 * @author Haojie Ma
 * @date Mar 4, 2016
 */
public class ProcedureDAOImp implements MonitorDAO {
	
	
private Logger log=Logger.getLogger(ProcedureDAOImp.class);
	
	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<ProcedureModel> getAll() {
		Session session =null;
		List<ProcedureModel> list=null;
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(ProcedureModel.class);
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}

	@SuppressWarnings("unchecked")
	public List<ProcedureModel> getProcedureListByLevel(String level) {
		Session session =null;
		List<ProcedureModel> list=null;
		List<String> levelList= new ArrayList<String>();
		
		if(level.equals("DBA")){
			levelList.add("EXECUTE");
			levelList.add("DEPLOY");
			levelList.add("DBA");
		}else if(level.equals("DEPLOY")){
			levelList.add("EXECUTE");
			levelList.add("DEPLOY");
		}else if(level.equals("EXECUTE"))
			levelList.add("EXECUTE");
			
		try{
			session = this.sessionFactory.openSession();
			Criteria criteria=session.createCriteria(ProcedureModel.class);
			criteria.add(Restrictions.in("level",levelList));
			list = criteria.list();
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			session.close();
		}
		
		return list;

	}


	

}


