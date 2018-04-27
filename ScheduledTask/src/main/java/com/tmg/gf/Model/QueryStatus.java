/**
 * 
 */
package com.tmg.gf.Model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Haojie Ma
 * @date Jun 9, 2015
 */

@Entity
@Table(name="query_status",schema="monitor")
public class QueryStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String STATEMENT_UUID;
	private String STATEMENT_STATUS;
	private Timestamp insert_time;
	public String getSTATEMENT_UUID() {
		return STATEMENT_UUID;
	}
	public void setSTATEMENT_UUID(String STATEMENT_UUID) {
		this.STATEMENT_UUID = STATEMENT_UUID;
	}
	public String getSTATEMENT_STATUS() {
		return STATEMENT_STATUS;
	}
	public void setSTATEMENT_STATUS(String STATEMENT_STATUS) {
		this.STATEMENT_STATUS = STATEMENT_STATUS;
	}
	public Timestamp getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((STATEMENT_STATUS == null) ? 0
						: STATEMENT_STATUS.hashCode());
		result = prime
				* result
				+ ((STATEMENT_UUID == null) ? 0
						: STATEMENT_UUID.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((insert_time == null) ? 0 : insert_time.hashCode());
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
		QueryStatus other = (QueryStatus) obj;
		if (STATEMENT_STATUS == null) {
			if (other.STATEMENT_STATUS != null)
				return false;
		} else if (!STATEMENT_STATUS
				.equals(other.STATEMENT_STATUS))
			return false;
		if (STATEMENT_UUID == null) {
			if (other.STATEMENT_UUID != null)
				return false;
		} else if (!STATEMENT_UUID.equals(other.STATEMENT_UUID))
			return false;
		if (id != other.id)
			return false;
		if (insert_time == null) {
			if (other.insert_time != null)
				return false;
		} else if (!insert_time.equals(other.insert_time))
			return false;
		return true;
	}
	
	
	
	
}


