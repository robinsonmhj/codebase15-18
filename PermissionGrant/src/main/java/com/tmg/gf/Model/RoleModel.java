/**
 * 
 */
package com.tmg.gf.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Haojie Ma
 * @date Mar 11, 2016
 */

@Entity
@Table(name="ROLES",schema="monitor")
public class RoleModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int role_id;
	private String role_name;
	private String privilege;
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	
	

}


