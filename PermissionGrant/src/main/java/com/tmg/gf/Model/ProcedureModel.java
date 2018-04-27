/**
 * 
 */
package com.tmg.gf.Model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * @author Haojie Ma
 * @date Mar 04, 2016
 */


@Entity
@Table(name="PROCEDURES",schema="monitor")
public class ProcedureModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int procedure_id;
	private String schema_name;
	private String procedure_name;
	private String level;
	public int getProcedure_id() {
		return procedure_id;
	}
	public void setProcedure_id(int procedure_id) {
		this.procedure_id = procedure_id;
	}
	public String getSchema_name() {
		return schema_name;
	}
	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}
	public String getProcedure_name() {
		return procedure_name;
	}
	public void setProcedure_name(String procedure_name) {
		this.procedure_name = procedure_name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + procedure_id;
		result = prime * result
				+ ((procedure_name == null) ? 0 : procedure_name.hashCode());
		result = prime * result
				+ ((schema_name == null) ? 0 : schema_name.hashCode());
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
		ProcedureModel other = (ProcedureModel) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (procedure_id != other.procedure_id)
			return false;
		if (procedure_name == null) {
			if (other.procedure_name != null)
				return false;
		} else if (!procedure_name.equals(other.procedure_name))
			return false;
		if (schema_name == null) {
			if (other.schema_name != null)
				return false;
		} else if (!schema_name.equals(other.schema_name))
			return false;
		return true;
	}

	
	
	
	
	
	
	

}


