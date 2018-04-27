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
 * @date Jun 9, 2015
 */

@Entity
@Table(name="GROUPS",schema="monitor")
public class GroupModel {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int group_id;
	private String group_name;
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	
	
	
	
	
}


