/**
 * 
 */
package com.giveu.entity;

import java.util.ArrayList;

/**
 * @author 529017
 * 返回码类
 */
public class ResponseModel<E> {
	private String message;
	private String code;
	private ArrayList<E> data;
	private Boolean result;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<E> getData() {
		return data;
	}

	public void setData(ArrayList<E> data) {
		this.data = data;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
}
