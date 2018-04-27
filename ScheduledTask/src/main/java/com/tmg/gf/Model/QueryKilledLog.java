/**
 * 
 */
package com.tmg.gf.Model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * @author Haojie Ma
 * @date Jun 11, 2015
 */


@Entity
@Table(name="query_killed_log",schema="monitor")
public class QueryKilledLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long Id;
	private String statement_uuid;
	private Timestamp insert_time;
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getStatement_uuid() {
		return statement_uuid;
	}
	public void setStatement_uuid(String statement_uuid) {
		this.statement_uuid = statement_uuid;
	}
	public Timestamp getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (Id ^ (Id >>> 32));
		result = prime * result
				+ ((insert_time == null) ? 0 : insert_time.hashCode());
		result = prime * result
				+ ((statement_uuid == null) ? 0 : statement_uuid.hashCode());
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
		QueryKilledLog other = (QueryKilledLog) obj;
		if (Id != other.Id)
			return false;
		if (insert_time == null) {
			if (other.insert_time != null)
				return false;
		} else if (!insert_time.equals(other.insert_time))
			return false;
		if (statement_uuid == null) {
			if (other.statement_uuid != null)
				return false;
		} else if (!statement_uuid.equals(other.statement_uuid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "KillLog [Id=" + Id + ", statement_uuid=" + statement_uuid
				+ ", insert_time=" + insert_time + "]";
	} 
	
	
	
	
	
	

}


