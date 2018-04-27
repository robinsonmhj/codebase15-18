/**
 * 
 */
package com.multiplemedia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Haojie Ma
 * @date Dec 15, 2015
 */

@Entity
@Table(name="privilege")
public class Privilege {
	
	@Id
	@GeneratedValue
	private int id;
	

	@Column(name="link_id",columnDefinition="smallint")
	private int linkId;
	

	@Column(name="user_name")
	private String userName;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getLinkId() {
		return linkId;
	}


	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	

	
	
	

}


