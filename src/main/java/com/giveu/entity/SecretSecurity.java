/**
 * 
 */
package com.giveu.entity;

/**
 * @author 529017
 * 安全码类
 */
public class SecretSecurity {
	private String safeCode;
	private Integer version;
	
	public String getSafeCode() {
		return safeCode;
	}
	public void setSafeCode(String safeCode) {
		this.safeCode = safeCode;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
