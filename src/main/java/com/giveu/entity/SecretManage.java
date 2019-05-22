/**
 * 
 */
package com.giveu.entity;

import java.util.Date;

/**
 * @author 529017
 * 密钥类
 */
public class SecretManage {
	private Long id;
	private String uuid;
	private String secretCommand;
	private String secretPassword;
	private Integer secretIndex;
	private Integer version;
	private Integer status;
	private Long encryptTimes;
	private Long decryptTimes;
	private String updateIp;
	private Date createTime;
	private Date updateTime;
	private Integer createUser;
	private Integer updateUser;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSecretCommand() {
		return secretCommand;
	}
	public void setSecretCommand(String secretCommand) {
		this.secretCommand = secretCommand;
	}
	public String getSecretPassword() {
		return secretPassword;
	}
	public void setSecretPassword(String secretPassword) {
		this.secretPassword = secretPassword;
	}
	public Integer getSecretIndex() {
		return secretIndex;
	}
	public void setSecretIndex(Integer secretIndex) {
		this.secretIndex = secretIndex;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getEncryptTimes() {
		return encryptTimes;
	}
	public void setEncryptTimes(Long encryptTimes) {
		this.encryptTimes = encryptTimes;
	}
	public Long getDecryptTimes() {
		return decryptTimes;
	}
	public void setDecryptTimes(Long decryptTimes) {
		this.decryptTimes = decryptTimes;
	}
	public String getUpdateIp() {
		return updateIp;
	}
	public void setUpdateIp(String updateIp) {
		this.updateIp = updateIp;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	public Integer getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}
}
