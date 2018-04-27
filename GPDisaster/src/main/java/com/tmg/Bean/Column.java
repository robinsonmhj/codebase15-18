package com.tmg.Bean;


import org.springframework.stereotype.Component;

@Component
public class Column {
	
	private String name;
	private String type;
	private int len;
	private boolean isPrimary=false;//is 
	private String flag;//NO not null YES null EMPTYSTRING unknown// flag indicating column can be null nor not
	private int decimalLen;
	
	public Column(){
		
		
	}

	
	public Column(String name, String type,int len,String flag,int decimalLen){
		
		this.name=name;
		this.type=type;
		this.len=len;
		this.flag=flag;
		this.decimalLen=decimalLen;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public boolean isPrimary() {
		return isPrimary;
	}
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	
	public int getDecimalLen() {
		return decimalLen;
	}


	public void setDecimalLen(int decimalLen) {
		this.decimalLen = decimalLen;
	}

	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + decimalLen;
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime * result + (isPrimary ? 1231 : 1237);
		result = prime * result + len;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Column other = (Column) obj;
		if (decimalLen != other.decimalLen)
			return false;
		if (flag == null) {
			if (other.flag != null)
				return false;
		} else if (!flag.equals(other.flag))
			return false;
		if (isPrimary != other.isPrimary)
			return false;
		if (len != other.len)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Column [name=" + name + ", type=" + type + ", len=" + len
				+ ", isPrimary=" + isPrimary + ", flag=" + flag
				+ ", decimalLen=" + decimalLen + "]";
	}
	
	
	
	
	

}
