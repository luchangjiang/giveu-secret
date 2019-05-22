/**
 * 
 */
package com.giveu.entity;

import java.util.ArrayList;

/**
 * @author 529017
 * 请求加密数据源类
 */
public class ReqRefData {
	
	private static final long serialVersionUID = 1L;
	
	private Integer mosaic;
	private ArrayList<String> refData;

	public Integer getMosaic() {
		return mosaic;
	}

	public void setMosaic(Integer mosaic) {
		this.mosaic = mosaic;
	}

	public ArrayList<String> getRefData() {
		return refData;
	}

	public void setRefData(ArrayList<String> refData) {
		this.refData = refData;
	}
}
