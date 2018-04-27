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
import com.tmg.gf.Model.PrivilegeModel;

/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */
public class PrivilegeDAOImp implements MonitorDAO {

	private Logger log = Logger.getLogger(PrivilegeDAOImp.class);

	@Resource(name = "hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<PrivilegeModel> getAll() {
		Session session = null;
		List<PrivilegeModel> list = null;
		try {
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(PrivilegeModel.class);
			list = criteria.list();
		} catch (Exception e) {

			log.error("", e);

		} finally {
			session.close();

		}

		return list;
	}



	
	@SuppressWarnings("unchecked")
	public int getByGroupIdandSchemaName(int groupId,String schemaName) {

		Session session = null;
		int roleId=0;
		try {
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(PrivilegeModel.class);
			criteria.add(Restrictions.eq("group_id", groupId));
			criteria.add(Restrictions.eq("schema_name", schemaName));
			criteria.setProjection(Property.forName("role_id"));//only extract the user_id column
			List<Integer> list = criteria.list();
			if(list!=null&&!list.isEmpty())
				roleId=list.get(0);

		} catch (Exception e) {

			log.error("", e);

		} finally {
			session.close();

		}

		return roleId;
	}	
	
	
	
	public void insert(PrivilegeModel privilege) {

		Session session = null;
		try {
			session = this.sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(privilege);
			session.getTransaction().commit();

		} catch (Exception e) {

			log.error("", e);
		} finally {

			session.close();

		}

	}
}