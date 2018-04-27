package com.tmg.Bean;


import java.util.List;

import com.tmg.Bean.Column;

import org.springframework.stereotype.Component;

@Component
public class Table {
	
	private String name;
	private List<Column> cloumns;
	private List<String> dsList;// distributed key list;
	
	
	public Table(){
		
	}
	
	public Table(String name){
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getCloumns() {
		return cloumns;
	}

	public void setCloumns(List<Column> cloumns) {
		this.cloumns = cloumns;
	}
	
	
	public List<String> getDsList() {
		return dsList;
	}

	public void setDsList(List<String> dsList) {
		this.dsList = dsList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cloumns == null) ? 0 : cloumns.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Table other = (Table) obj;
		if (cloumns == null) {
			if (other.cloumns != null)
				return false;
		} else if (!cloumns.equals(other.cloumns))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", cloumns=" + cloumns + "]";
	}
	
	
	
	
	
	

}
