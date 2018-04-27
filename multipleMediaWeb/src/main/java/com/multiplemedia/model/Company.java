/**
 * 
 */
package com.multiplemedia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.multiplemedia.views.Views;

/**
 * @author Haojie Ma
 * @date Dec 11, 2015
 */



@Entity
@Table(name="company")
public class Company {
	
	@Id
	@GeneratedValue 
	@Column(name="company_Id")
	private long companyId;
	
	@JsonView(Views.Public.class)
	@Column(name="company_name")
	private String companyName;
	
	@JsonView(Views.Public.class)
	@Column(name="company_ave")
	private String companyAve;
	
	@JsonView(Views.Public.class)
	@Column(name="company_city")
	private String companyCity;
	
	@JsonView(Views.Public.class)
	@Column(name="company_province")
	private String companyProvince;
	
	@JsonView(Views.Public.class)
	@Column(name="company_county")
	private String companyCounty;
	
	@JsonView(Views.Public.class)
	@Column(name="company_weichat")
	private String companyWeichat;
	
	@JsonView(Views.Public.class)
	@Column(name="company_email")
	private String companyEmail;
	
	@JsonView(Views.Public.class)
	@Column(name="company_phone")
	private long companyPhone;
	
	@Column(name="company_logo")
	private long companyLogo;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAve() {
		return companyAve;
	}

	public void setCompanyAve(String companyAve) {
		this.companyAve = companyAve;
	}

	public String getCompanyCity() {
		return companyCity;
	}

	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}

	public String getCompanyProvince() {
		return companyProvince;
	}

	public void setCompanyProvince(String companyProvince) {
		this.companyProvince = companyProvince;
	}

	public String getCompanyCounty() {
		return companyCounty;
	}

	public void setCompanyCounty(String companyCounty) {
		this.companyCounty = companyCounty;
	}

	public String getCompanyWeichat() {
		return companyWeichat;
	}

	public void setCompanyWeichat(String companyWeichat) {
		this.companyWeichat = companyWeichat;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public long getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(long companyPhone) {
		this.companyPhone = companyPhone;
	}

	public long getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(long companyLogo) {
		this.companyLogo = companyLogo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyAve == null) ? 0 : companyAve.hashCode());
		result = prime * result
				+ ((companyCity == null) ? 0 : companyCity.hashCode());
		result = prime * result
				+ ((companyCounty == null) ? 0 : companyCounty.hashCode());
		result = prime * result
				+ ((companyEmail == null) ? 0 : companyEmail.hashCode());
		result = prime * result + (int) (companyId ^ (companyId >>> 32));
		result = prime * result + (int) (companyLogo ^ (companyLogo >>> 32));
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + (int) (companyPhone ^ (companyPhone >>> 32));
		result = prime * result
				+ ((companyProvince == null) ? 0 : companyProvince.hashCode());
		result = prime * result
				+ ((companyWeichat == null) ? 0 : companyWeichat.hashCode());
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
		Company other = (Company) obj;
		if (companyAve == null) {
			if (other.companyAve != null)
				return false;
		} else if (!companyAve.equals(other.companyAve))
			return false;
		if (companyCity == null) {
			if (other.companyCity != null)
				return false;
		} else if (!companyCity.equals(other.companyCity))
			return false;
		if (companyCounty == null) {
			if (other.companyCounty != null)
				return false;
		} else if (!companyCounty.equals(other.companyCounty))
			return false;
		if (companyEmail == null) {
			if (other.companyEmail != null)
				return false;
		} else if (!companyEmail.equals(other.companyEmail))
			return false;
		if (companyId != other.companyId)
			return false;
		if (companyLogo != other.companyLogo)
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (companyPhone != other.companyPhone)
			return false;
		if (companyProvince == null) {
			if (other.companyProvince != null)
				return false;
		} else if (!companyProvince.equals(other.companyProvince))
			return false;
		if (companyWeichat == null) {
			if (other.companyWeichat != null)
				return false;
		} else if (!companyWeichat.equals(other.companyWeichat))
			return false;
		return true;
	}

	
	

}


