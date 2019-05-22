package com.giveu.entity;

import java.util.Date;

/**
 * @author Silver 2018/8/30
 */
public class QueryManage {

    private Integer id;
    /**
     *查询口令
     */
    private String queryCommand;
    /**
     * 激活状态，0：禁用，1：激活
     */
    private Integer status;
    /**
     * 截止日期
     */
    private Date endDate;
    /**
     * 设定使用次数（按查询页面请求数）
     */
    private Integer totalUseTimes;
    /**
     * 已使用次数
     */
    private Integer useTimes;
    /**
     *  设定加密次数（按条数）
     */
    private Integer totalEncryptTimes;
    /**
     *  已加密次数
     */
    private Integer encryptTimes;
    /**
     *  设定解密次数
     */
    private Integer totalDecryptTimes;
    /**
     *  已解密次数
     */
    private Integer decryptTimes;
    /**
     * 描述
     */
    private String queryDesc;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private Integer updateUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQueryCommand() {
        return queryCommand;
    }

    public void setQueryCommand(String queryCommand) {
        this.queryCommand = queryCommand;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalUseTimes() {
        return totalUseTimes;
    }

    public void setTotalUseTimes(Integer totalUseTimes) {
        this.totalUseTimes = totalUseTimes;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Integer getTotalEncryptTimes() {
        return totalEncryptTimes;
    }

    public void setTotalEncryptTimes(Integer totalEncryptTimes) {
        this.totalEncryptTimes = totalEncryptTimes;
    }

    public Integer getEncryptTimes() {
        return encryptTimes;
    }

    public void setEncryptTimes(Integer encryptTimes) {
        this.encryptTimes = encryptTimes;
    }

    public Integer getTotalDecryptTimes() {
        return totalDecryptTimes;
    }

    public void setTotalDecryptTimes(Integer totalDecryptTimes) {
        this.totalDecryptTimes = totalDecryptTimes;
    }

    public Integer getDecryptTimes() {
        return decryptTimes;
    }

    public void setDecryptTimes(Integer decryptTimes) {
        this.decryptTimes = decryptTimes;
    }

    public String getQueryDesc() {
        return queryDesc;
    }

    public void setQueryDesc(String queryDesc) {
        this.queryDesc = queryDesc;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }
}
