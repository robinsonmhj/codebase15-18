/**
 * 
 */
package com.multiplemedia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Haojie Ma
 * @date Dec 15, 2015
 */


@Entity
@Table(name="link")
public class Link {
	
	@Id
	@Column(name="link_id",columnDefinition="smallint")
	@GeneratedValue
	private int linkId;
	
	@Column(name="link_name")
	private String linkName;
	
	@Column(name="link_url")
	private String linkUrl;
	
	@Column(name="parent_id",columnDefinition="smallint")
	private int parentId;
	
	public int getLinkId() {
		return linkId;
	}
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
		
	

}


