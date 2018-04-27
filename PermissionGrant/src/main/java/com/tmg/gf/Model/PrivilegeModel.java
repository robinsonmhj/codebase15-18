/**
 * 
 */
package com.tmg.gf.Model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author Haojie Ma
 * @date Jun 29, 2015
 */


@Entity
@IdClass(PrivilegeModel.PK.class)
@Table(name="PRIVILEGE",schema="monitor")
public class PrivilegeModel {
	
	@Id
	int role_id;
	int group_id;
	String schema_name;
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
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
	
	public static class PK implements Serializable{
		
		protected Integer role_id;
		protected Integer group_id;
		protected String schema_name;
		
		public PK(){}
		
		public PK(Integer role_id,Integer group_id,String schema_name){
			this.role_id=role_id;
			this.group_id=group_id;
			this.schema_name=schema_name;
			
		}
}


	
	
}

