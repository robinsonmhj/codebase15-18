/**
 * 
 */
package com.tmg.gf.Model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */

@Entity
@Table(name = "query_details", schema = "monitor")
public class QueryDetail {

	@Id
	private String STATEMENT_UUID;
	private int SESSION_ID;
	private String HOSTNAME;
	private String USER_ID;
	private String CLIENT_BIND_ADDRESS;
	private String SESSION_STATUS;
	private Timestamp SESSION_BEGIN_TIME;
	private String STATEMENT;
	private long STATEMENT_ELAPSED_TIME;

	public String getSTATEMENT_UUID() {
		return STATEMENT_UUID;
	}

	public void setSTATEMENT_UUID(String STATEMENT_UUID) {
		this.STATEMENT_UUID = STATEMENT_UUID;
	}

	public int getSESSION_ID() {
		return SESSION_ID;
	}

	public void setSESSION_ID(int sESSION_ID) {
		SESSION_ID = sESSION_ID;
	}

	public String getHOSTNAME() {
		return HOSTNAME;
	}

	public void setHOSTNAME(String hOSTNAME) {
		HOSTNAME = hOSTNAME;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getCLIENT_BIND_ADDRESS() {
		return CLIENT_BIND_ADDRESS;
	}

	public void setCLIENT_BIND_ADDRESS(String cLIENT_BIND_ADDRESS) {
		CLIENT_BIND_ADDRESS = cLIENT_BIND_ADDRESS;
	}


	public String getSESSION_STATUS() {
		return SESSION_STATUS;
	}

	public void setSESSION_STATUS(String sESSION_STATUS) {
		SESSION_STATUS = sESSION_STATUS;
	}

	public Timestamp getSESSION_BEGIN_TIME() {
		return SESSION_BEGIN_TIME;
	}

	public void setSESSION_BEGIN_TIME(Timestamp sESSION_BEGIN_TIME) {
		SESSION_BEGIN_TIME = sESSION_BEGIN_TIME;
	}

	public String getSTATEMENT() {
		return STATEMENT;
	}

	public void setSTATEMENT(String STATEMENT) {
		this.STATEMENT = STATEMENT;
	}

	public long getSTATEMENT_ELAPSED_TIME() {
		return STATEMENT_ELAPSED_TIME;
	}

	public void setSTATEMENT_ELAPSED_TIME(
			long STATEMENT_ELAPSED_TIME) {
		this.STATEMENT_ELAPSED_TIME = STATEMENT_ELAPSED_TIME;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((CLIENT_BIND_ADDRESS == null) ? 0 : CLIENT_BIND_ADDRESS
						.hashCode());
		result = prime * result
				+ ((HOSTNAME == null) ? 0 : HOSTNAME.hashCode());
		result = prime
				* result
				+ ((SESSION_BEGIN_TIME == null) ? 0 : SESSION_BEGIN_TIME
						.hashCode());
		result = prime * result + SESSION_ID;
		result = prime * result
				+ ((SESSION_STATUS == null) ? 0 : SESSION_STATUS.hashCode());
		result = prime * result
				+ ((STATEMENT == null) ? 0 : STATEMENT.hashCode());
		result = prime
				* result
				+ (int) (STATEMENT_ELAPSED_TIME ^ (STATEMENT_ELAPSED_TIME >>> 32));
		result = prime * result
				+ ((STATEMENT_UUID == null) ? 0 : STATEMENT_UUID.hashCode());
		result = prime * result + ((USER_ID == null) ? 0 : USER_ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryDetail other = (QueryDetail) obj;
		if (CLIENT_BIND_ADDRESS == null) {
			if (other.CLIENT_BIND_ADDRESS != null)
				return false;
		} else if (!CLIENT_BIND_ADDRESS.equals(other.CLIENT_BIND_ADDRESS))
			return false;
		if (HOSTNAME == null) {
			if (other.HOSTNAME != null)
				return false;
		} else if (!HOSTNAME.equals(other.HOSTNAME))
			return false;
		if (SESSION_BEGIN_TIME == null) {
			if (other.SESSION_BEGIN_TIME != null)
				return false;
		} else if (!SESSION_BEGIN_TIME.equals(other.SESSION_BEGIN_TIME))
			return false;
		if (SESSION_ID != other.SESSION_ID)
			return false;
		if (SESSION_STATUS == null) {
			if (other.SESSION_STATUS != null)
				return false;
		} else if (!SESSION_STATUS.equals(other.SESSION_STATUS))
			return false;
		if (STATEMENT == null) {
			if (other.STATEMENT != null)
				return false;
		} else if (!STATEMENT.equals(other.STATEMENT))
			return false;
		if (STATEMENT_ELAPSED_TIME != other.STATEMENT_ELAPSED_TIME)
			return false;
		if (STATEMENT_UUID == null) {
			if (other.STATEMENT_UUID != null)
				return false;
		} else if (!STATEMENT_UUID.equals(other.STATEMENT_UUID))
			return false;
		if (USER_ID == null) {
			if (other.USER_ID != null)
				return false;
		} else if (!USER_ID.equals(other.USER_ID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QueryDetail [STATEMENT_UUID=" + STATEMENT_UUID
				+ ", SESSION_ID=" + SESSION_ID + ", HOSTNAME=" + HOSTNAME
				+ ", USER_ID=" + USER_ID + ", CLIENT_BIND_ADDRESS="
				+ CLIENT_BIND_ADDRESS + ", SESSION_STATUS=" + SESSION_STATUS
				+ ", SESSION_BEGIN_TIME=" + SESSION_BEGIN_TIME + ", STATEMENT="
				+ STATEMENT + ", STATEMENT_ELAPSED_TIME="
				+ STATEMENT_ELAPSED_TIME + "]";
	}
	
	
	
	
	
}

	