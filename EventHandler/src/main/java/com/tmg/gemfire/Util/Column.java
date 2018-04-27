package com.tmg.gemfire.Util;


public class Column {

	private String name;
	private String type;
	private int len;
	private boolean isPrimary = false;// is
	private String flag;// NO not null YES null EMPTYSTRING unknown// flag
						// indicating column can be null nor not
	private int decimalLen;

	public Column() {

	}

	public Column(String name, String type) {

		this.name = name;
		this.type = type;

	}

	public Column(String name, String type, int len, String flag, int decimalLen) {

		this.name = name;
		this.type = type;
		this.len = len;
		this.flag = flag;
		this.decimalLen = decimalLen;
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

}
