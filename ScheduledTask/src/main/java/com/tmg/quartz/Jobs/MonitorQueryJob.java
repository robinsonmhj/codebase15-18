/**
 * 
 */
package com.tmg.quartz.Jobs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmg.gf.DAOImp.GfSessionDAOImp;
import com.tmg.gf.DAOImp.QueryDetailDAOImp;
import com.tmg.gf.DAOImp.QueryStatusDAOImp;
import com.tmg.gf.Model.GfSession;
import com.tmg.gf.Model.QueryDetail;
import com.tmg.gf.Model.QueryStatus;

/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */
@DisallowConcurrentExecution
public class MonitorQueryJob extends QuartzJobBean {
	// public class monitorQueryJob implements Job {
	private Logger log = Logger.getLogger(MonitorQueryJob.class);
	
	private final String done="DONE";

	@Resource(name = "GfSessionDAO")
	private GfSessionDAOImp sessionDaoImp;

	@Resource(name = "QueryDetailDAO")
	private QueryDetailDAOImp queryDetailImp;

	@Resource(name = "QueryStatusDAO")
	private QueryStatusDAOImp queryStatusImp;

	public void setsessionDaoImp(GfSessionDAOImp sessionDaoImp) {
		this.sessionDaoImp = sessionDaoImp;
	}

	public void setqueryDetailImp(QueryDetailDAOImp queryDetailImp) {
		this.queryDetailImp = queryDetailImp;
	}

	public void setqueryStatusImp(QueryStatusDAOImp queryStatusImp) {
		this.queryStatusImp = queryStatusImp;
	}

	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {

		log.info("Query Monitor Job begins");
		try {
			List<GfSession> sessionList = sessionDaoImp.getByUUID(null);
			List<QueryDetail> detailList = queryDetailImp.getAll();
			List<String> uuidList = null;// uuid in the monitor.query_details
											// table
			List<String> uuidSessionList = new ArrayList<String>();// the uuid
																	// in the
																	// sys.sessions
																	// table
			Iterator<GfSession> sessionIter = sessionList.iterator();
			if (detailList != null && !detailList.isEmpty()) {
				Iterator<QueryDetail> detailIter = detailList.iterator();
				uuidList = new ArrayList<String>();
				while (detailIter.hasNext()) {
					QueryDetail detail = detailIter.next();
					String uuid = detail.getSTATEMENT_UUID();
					uuidList.add(uuid);
				}

			}

			while (sessionIter.hasNext()) {
				GfSession session = sessionIter.next();
				if (session == null) {
					log.info("skip session due to without uuid");
					continue;
				}
				int session_id = session.getSESSION_ID();
				String uuid = session.getCURRENT_STATEMENT_UUID();
				String client_address = session.getCLIENT_BIND_ADDRESS();
				String statement = session.getCURRENT_STATEMENT();
				long elpsed_time = session.getCURRENT_STATEMENT_ELAPSED_TIME();
				String hostname = session.getHOSTNAME();
				Timestamp beginTime = session.getSESSION_BEGIN_TIME();
				String session_status = session.getSESSION_STATUS();
				String statement_status = session.getCURRENT_STATEMENT_STATUS();
				String user_id = session.getUSER_ID();
				QueryDetail detail = new QueryDetail();
				detail.setCLIENT_BIND_ADDRESS(client_address);
				detail.setSTATEMENT(statement);
				detail.setSTATEMENT_ELAPSED_TIME(elpsed_time);
				detail.setSTATEMENT_UUID(uuid);
				detail.setHOSTNAME(hostname);
				detail.setSESSION_BEGIN_TIME(beginTime);
				detail.setSESSION_ID(session_id);
				detail.setSESSION_STATUS(session_status);
				detail.setUSER_ID(user_id);

				Date date = new java.util.Date();
				Timestamp insertTime = new Timestamp(date.getTime());
				QueryStatus querystatus = new QueryStatus();
				querystatus.setSTATEMENT_UUID(uuid);
				querystatus.setSTATEMENT_STATUS(statement_status);
				querystatus.setInsert_time(insertTime);

				uuidSessionList.add(uuid);

				// initial run, no data in monitor.query_details table
				if (uuidList == null) {
					queryDetailImp.insert(detail);
					queryStatusImp.insert(querystatus);
				} else {
					// update the elapsed_time of the statement if the statement
					// is in monitor.query_details
					if (uuidList.contains(uuid)) {
						queryDetailImp.update(detail);
						List<String> statusList = queryStatusImp.getStatusByUUID(uuid);
						Iterator<String> statusIter = statusList.iterator();
						boolean flag = true;
						while (statusIter.hasNext()) {
							String tmpStatus = statusIter.next();
							if (tmpStatus.equalsIgnoreCase(statement_status)) {
								flag = false;
								break;
							}
						}
						// if there are new status in the current statement,
						// insert
						if (flag)
							queryStatusImp.insert(querystatus);
					} else {// if the statement is not in monitor.query_details
						queryDetailImp.insert(detail);
						queryStatusImp.insert(querystatus);
					}
				}

				log.debug("Session:" + session.toString() + " done");
			}

			// if the query in monitor.query_details disappers
			if (uuidList != null) {

				for (int i = 0; i < uuidList.size(); i++) {
					String uuid = uuidList.get(i);
					if (!uuidSessionList.contains(uuid)) {
						List<String> statusList = queryStatusImp.getStatusByUUID(uuid);
						Iterator<String> statusIter = statusList.iterator();
						boolean flag = true;
						while (statusIter.hasNext()) {
							String tmpStatus = statusIter.next();
							if (tmpStatus.equalsIgnoreCase(done)) {
								flag = false;
								break;
							}
						}
						if (flag){
							Date date = new java.util.Date();
							Timestamp insertTime = new Timestamp(date.getTime());
							QueryStatus querystatus = new QueryStatus();
							querystatus.setSTATEMENT_UUID(uuid);
							querystatus.setSTATEMENT_STATUS(done);
							querystatus.setInsert_time(insertTime);
							queryStatusImp.insert(querystatus);
							
						}
					}

				}
			}
		} catch (Exception e) {
			log.error("", e);
		}

		log.info("Query Monitor Job ends");

	}

}
