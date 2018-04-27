/**
 * 
 */
package com.tmg.gf.Model;

/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;


@Entity
@Table(name="sessions",schema="sys")
public class GfSession {

	@Id 
	private String CURRENT_STATEMENT_UUID;
	private String ID;
	private int SESSION_ID;
	private String HOSTNAME;
	private int SERVER_LISTENING_PORT;
	private String USER_ID;
	private String CLIENT_BIND_ADDRESS;
	private int CLIENT_BIND_PORT;
	private String SOCKET_CONNECTION_STATUS;
	private String SESSION_STATUS;
	private Timestamp SESSION_BEGIN_TIME;
	private String SESSION_INFO;
	private String CURRENT_STATEMENT;
	private String CURRENT_STATEMENT_STATUS;
	private long CURRENT_STATEMENT_ELAPSED_TIME;
	private int CURRENT_STATEMENT_ACCESS_FREQUENCY;
	private int CURRENT_STATEMENT_MEMORY_USAGE;
	private String NETWORK_INTERFACE_INFO;
	
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
	
	public int getCLIENT_BIND_PORT() {
		return CLIENT_BIND_PORT;
	}
	public void setCLIENT_BIND_PORT(int cLIENT_BIND_PORT) {
		CLIENT_BIND_PORT = cLIENT_BIND_PORT;
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
	
	public String getCURRENT_STATEMENT_UUID() {
		return CURRENT_STATEMENT_UUID;
	}
	public void setCURRENT_STATEMENT_UUID(String cURRENT_STATEMENT_UUID) {
		CURRENT_STATEMENT_UUID = cURRENT_STATEMENT_UUID;
	}
	public String getCURRENT_STATEMENT() {
		return CURRENT_STATEMENT;
	}
	public void setCURRENT_STATEMENT(String cURRENT_STATEMENT) {
		CURRENT_STATEMENT = cURRENT_STATEMENT;
	}
	public String getCURRENT_STATEMENT_STATUS() {
		return CURRENT_STATEMENT_STATUS;
	}
	public void setCURRENT_STATEMENT_STATUS(String cURRENT_STATEMENT_STATUS) {
		CURRENT_STATEMENT_STATUS = cURRENT_STATEMENT_STATUS;
	}
	public long getCURRENT_STATEMENT_ELAPSED_TIME() {
		return CURRENT_STATEMENT_ELAPSED_TIME;
	}
	public void setCURRENT_STATEMENT_ELAPSED_TIME(
			long cURRENT_STATEMENT_ELAPSED_TIME) {
		CURRENT_STATEMENT_ELAPSED_TIME = cURRENT_STATEMENT_ELAPSED_TIME;
	}
	public int getCURRENT_STATEMENT_ACCESS_FREQUENCY() {
		return CURRENT_STATEMENT_ACCESS_FREQUENCY;
	}
	public void setCURRENT_STATEMENT_ACCESS_FREQUENCY(
			int cURRENT_STATEMENT_ACCESS_FREQUENCY) {
		CURRENT_STATEMENT_ACCESS_FREQUENCY = cURRENT_STATEMENT_ACCESS_FREQUENCY;
	}
	public int getCURRENT_STATEMENT_MEMORY_USAGE() {
		return CURRENT_STATEMENT_MEMORY_USAGE;
	}
	public void setCURRENT_STATEMENT_MEMORY_USAGE(int cURRENT_STATEMENT_MEMORY_USAGE) {
		CURRENT_STATEMENT_MEMORY_USAGE = cURRENT_STATEMENT_MEMORY_USAGE;
	}
	public int getSERVER_LISTENING_PORT() {
		return SERVER_LISTENING_PORT;
	}
	public void setSERVER_LISTENING_PORT(int sERVER_LISTENING_PORT) {
		SERVER_LISTENING_PORT = sERVER_LISTENING_PORT;
	}
	public String getSOCKET_CONNECTION_STATUS() {
		return SOCKET_CONNECTION_STATUS;
	}
	public void setSOCKET_CONNECTION_STATUS(String sOCKET_CONNECTION_STATUS) {
		SOCKET_CONNECTION_STATUS = sOCKET_CONNECTION_STATUS;
	}
	public String getSESSION_INFO() {
		return SESSION_INFO;
	}
	public void setSESSION_INFO(String sESSION_INFO) {
		SESSION_INFO = sESSION_INFO;
	}
	public String getNETWORK_INTERFACE_INFO() {
		return NETWORK_INTERFACE_INFO;
	}
	public void setNETWORK_INTERFACE_INFO(String nETWORK_INTERFACE_INFO) {
		NETWORK_INTERFACE_INFO = nETWORK_INTERFACE_INFO;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((CLIENT_BIND_ADDRESS == null) ? 0 : CLIENT_BIND_ADDRESS
						.hashCode());
		result = prime * result + CLIENT_BIND_PORT;
		result = prime
				* result
				+ ((CURRENT_STATEMENT == null) ? 0 : CURRENT_STATEMENT
						.hashCode());
		result = prime * result + CURRENT_STATEMENT_ACCESS_FREQUENCY;
		result = prime
				* result
				+ (int) (CURRENT_STATEMENT_ELAPSED_TIME ^ (CURRENT_STATEMENT_ELAPSED_TIME >>> 32));
		result = prime * result + CURRENT_STATEMENT_MEMORY_USAGE;
		result = prime
				* result
				+ ((CURRENT_STATEMENT_STATUS == null) ? 0
						: CURRENT_STATEMENT_STATUS.hashCode());
		result = prime
				* result
				+ ((CURRENT_STATEMENT_UUID == null) ? 0
						: CURRENT_STATEMENT_UUID.hashCode());
		result = prime * result
				+ ((HOSTNAME == null) ? 0 : HOSTNAME.hashCode());
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime
				* result
				+ ((NETWORK_INTERFACE_INFO == null) ? 0
						: NETWORK_INTERFACE_INFO.hashCode());
		result = prime * result + SERVER_LISTENING_PORT;
		result = prime
				* result
				+ ((SESSION_BEGIN_TIME == null) ? 0 : SESSION_BEGIN_TIME
						.hashCode());
		result = prime * result + SESSION_ID;
		result = prime * result
				+ ((SESSION_INFO == null) ? 0 : SESSION_INFO.hashCode());
		result = prime * result
				+ ((SESSION_STATUS == null) ? 0 : SESSION_STATUS.hashCode());
		result = prime
				* result
				+ ((SOCKET_CONNECTION_STATUS == null) ? 0
						: SOCKET_CONNECTION_STATUS.hashCode());
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
		GfSession other = (GfSession) obj;
		if (CLIENT_BIND_ADDRESS == null) {
			if (other.CLIENT_BIND_ADDRESS != null)
				return false;
		} else if (!CLIENT_BIND_ADDRESS.equals(other.CLIENT_BIND_ADDRESS))
			return false;
		if (CLIENT_BIND_PORT != other.CLIENT_BIND_PORT)
			return false;
		if (CURRENT_STATEMENT == null) {
			if (other.CURRENT_STATEMENT != null)
				return false;
		} else if (!CURRENT_STATEMENT.equals(other.CURRENT_STATEMENT))
			return false;
		if (CURRENT_STATEMENT_ACCESS_FREQUENCY != other.CURRENT_STATEMENT_ACCESS_FREQUENCY)
			return false;
		if (CURRENT_STATEMENT_ELAPSED_TIME != other.CURRENT_STATEMENT_ELAPSED_TIME)
			return false;
		if (CURRENT_STATEMENT_MEMORY_USAGE != other.CURRENT_STATEMENT_MEMORY_USAGE)
			return false;
		if (CURRENT_STATEMENT_STATUS == null) {
			if (other.CURRENT_STATEMENT_STATUS != null)
				return false;
		} else if (!CURRENT_STATEMENT_STATUS
				.equals(other.CURRENT_STATEMENT_STATUS))
			return false;
		if (CURRENT_STATEMENT_UUID == null) {
			if (other.CURRENT_STATEMENT_UUID != null)
				return false;
		} else if (!CURRENT_STATEMENT_UUID.equals(other.CURRENT_STATEMENT_UUID))
			return false;
		if (HOSTNAME == null) {
			if (other.HOSTNAME != null)
				return false;
		} else if (!HOSTNAME.equals(other.HOSTNAME))
			return false;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (NETWORK_INTERFACE_INFO == null) {
			if (other.NETWORK_INTERFACE_INFO != null)
				return false;
		} else if (!NETWORK_INTERFACE_INFO.equals(other.NETWORK_INTERFACE_INFO))
			return false;
		if (SERVER_LISTENING_PORT != other.SERVER_LISTENING_PORT)
			return false;
		if (SESSION_BEGIN_TIME == null) {
			if (other.SESSION_BEGIN_TIME != null)
				return false;
		} else if (!SESSION_BEGIN_TIME.equals(other.SESSION_BEGIN_TIME))
			return false;
		if (SESSION_ID != other.SESSION_ID)
			return false;
		if (SESSION_INFO == null) {
			if (other.SESSION_INFO != null)
				return false;
		} else if (!SESSION_INFO.equals(other.SESSION_INFO))
			return false;
		if (SESSION_STATUS == null) {
			if (other.SESSION_STATUS != null)
				return false;
		} else if (!SESSION_STATUS.equals(other.SESSION_STATUS))
			return false;
		if (SOCKET_CONNECTION_STATUS == null) {
			if (other.SOCKET_CONNECTION_STATUS != null)
				return false;
		} else if (!SOCKET_CONNECTION_STATUS
				.equals(other.SOCKET_CONNECTION_STATUS))
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
		return "GfSession [CURRENT_STATEMENT_UUID=" + CURRENT_STATEMENT_UUID
				+ ", ID=" + ID + ", SESSION_ID=" + SESSION_ID + ", HOSTNAME="
				+ HOSTNAME + ", SERVER_LISTENING_PORT=" + SERVER_LISTENING_PORT
				+ ", USER_ID=" + USER_ID + ", CLIENT_BIND_ADDRESS="
				+ CLIENT_BIND_ADDRESS + ", CLIENT_BIND_PORT="
				+ CLIENT_BIND_PORT + ", SOCKET_CONNECTION_STATUS="
				+ SOCKET_CONNECTION_STATUS + ", SESSION_STATUS="
				+ SESSION_STATUS + ", SESSION_BEGIN_TIME=" + SESSION_BEGIN_TIME
				+ ", SESSION_INFO=" + SESSION_INFO + ", CURRENT_STATEMENT="
				+ CURRENT_STATEMENT + ", CURRENT_STATEMENT_STATUS="
				+ CURRENT_STATEMENT_STATUS
				+ ", CURRENT_STATEMENT_ELAPSED_TIME="
				+ CURRENT_STATEMENT_ELAPSED_TIME
				+ ", CURRENT_STATEMENT_ACCESS_FREQUENCY="
				+ CURRENT_STATEMENT_ACCESS_FREQUENCY
				+ ", CURRENT_STATEMENT_MEMORY_USAGE="
				+ CURRENT_STATEMENT_MEMORY_USAGE + ", NETWORK_INTERFACE_INFO="
				+ NETWORK_INTERFACE_INFO + "]";
	}
	

	
	

	
	
	
	
	
	
	
}


