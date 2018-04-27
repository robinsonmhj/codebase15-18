/**
 * 
 */
package com.tmg.gf.Model;



import java.sql.Timestamp;

import javax.persistence.Column;
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
@Table(name="REQUEST_HISTORY",schema="monitor")
public class RequestHistoryModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int request_id;
	private String user_name;
	private int group_id;
	private String schema_name;
	@Column(insertable=false)
	private Timestamp insert_time;
	public int getRequest_id() {
		return request_id;
	}
	public void setRequest_id(int request_id) {
		this.request_id = request_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getSchema_name() {
		return schema_name;
	}
	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}
	public Timestamp getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}
	
	
	


}


