/**
 * 
 */
package com.multiplemedia.model;

/**
 * @author Haojie Ma
 * @date Oct 21, 2015
 */





import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.multiplemedia.views.Views;

@Entity
@Table(name="user")
public class User {

	
	@Id
	@JsonView(Views.Public.class)
	@Column(name="user_name")
	private String userName;
	
	@JsonView(Views.Public.class)
	@Column(name="first_name")
	private String firstName;
	
	@JsonView(Views.Public.class)
	@Column(name="last_name")
	private String lastName;
	
	@JsonView(Views.Public.class)
	private String avenue;
	
	@JsonView(Views.Public.class)
	private String province;
	
	@JsonView(Views.Public.class)
	private String city;
	
	@JsonView(Views.Public.class)
	private String county;
	
	@JsonView(Views.Public.class)
	private String weichat;
	
	@JsonView(Views.Public.class)
	private long qq;
	
	@JsonView(Views.Public.class)
	private String email;
	
	@Column(columnDefinition="char(32)")
	private String password;
	
	@JsonView(Views.Public.class)
	private long cellphone;
	
	@JsonView(Views.Public.class)
	@Column(name="company_id")
	private long companyId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvenue() {
		return avenue;
	}

	public void setAvenue(String avenue) {
		this.avenue = avenue;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getWeichat() {
		return weichat;
	}

	public void setWeichat(String weichat) {
		this.weichat = weichat;
	}

	public long getQq() {
		return qq;
	}

	public void setQq(long qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getCellphone() {
		return cellphone;
	}

	public void setCellphone(long cellphone) {
		this.cellphone = cellphone;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}



	
	

	
	
	
	
	
}


