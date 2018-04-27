/**
 * 
 */
package com.barclaycard.us.model;

/**
 * @author Haojie Ma
 * Mar 23, 2018
 */
public class Traveler {
	
	private long id;
	private short identityTypeId;
	private String fName;
	private String lName;
	private String identityNo;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public short getIdentityTypeId() {
		return identityTypeId;
	}
	public void setIdentityTypeId(short identityTypeId) {
		this.identityTypeId = identityTypeId;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getIdentityNo() {
		return identityNo;
	}
	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}
	
	
	

}
