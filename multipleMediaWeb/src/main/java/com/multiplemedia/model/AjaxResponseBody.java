/**
 * 
 */
package com.multiplemedia.model;

/**
 * @author Haojie Ma
 * @date Oct 21, 2015
 */
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.multiplemedia.views.User;
import com.multiplemedia.views.Views;


public class AjaxResponseBody<T> {

	@JsonView(Views.Public.class)
	String msg;
	
	@JsonView(Views.Public.class)
	String code;
	
	@JsonView(Views.Public.class)
	List<T> result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	
}



