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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.tmg.gf.DAO.MonitorDAO;
import com.tmg.gf.Model.QueryStatus;

/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */
public class QueryStatusDAOImp implements MonitorDAO {

	private Logger log = Logger.getLogger(QueryStatusDAOImp.class);

	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<QueryStatus> getAll() {
		Session session = null;
		List<QueryStatus> list = null;
		try {
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(QueryStatus.class);
			list = criteria.list();
		} catch (Exception e) {

			log.error("", e);

		} finally {
			session.close();

		}

		return list;
	}

	public void insert(QueryStatus status) {

		Session session = null;
		try {
			session = this.sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(status);
			session.getTransaction().commit();

		} catch (Exception e) {

			log.error("", e);
		} finally {

			session.close();

		}

	}

	@SuppressWarnings("unchecked")
	public List<QueryStatus> getByUUID(String uuid) {

		Session session = null;
		List<QueryStatus> list = null;
		try {

			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(QueryStatus.class);
			criteria.add(Restrictions.eq("STATEMENT_UUID", uuid));
			list = criteria.list();

		} catch (Exception e) {

			log.error("", e);

		} finally {
			session.close();

		}

		return list;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<String> getStatusByUUID(String uuid) {

		Session session = null;
		List<String> list = null;
		try {

			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(QueryStatus.class);
			criteria.add(Restrictions.eq("STATEMENT_UUID", uuid));
			criteria.setProjection(Property.forName("STATEMENT_STATUS"));//only extract the CURRENT_STATEMENT_UUID column
			list = criteria.list();

		} catch (Exception e) {

			log.error("", e);

		} finally {
			session.close();

		}

		return list;
	}

}
