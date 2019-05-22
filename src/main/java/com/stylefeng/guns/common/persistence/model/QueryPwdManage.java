package com.stylefeng.guns.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

public class QueryPwdManage  extends Model<QueryPwdManage> {
    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    private String queryCommand;
    private String queryDesc;
    private Date createTime;
    private Date endDate;
    private Integer totalUseTimes;
    private Integer useTimes;
    private Integer totalEncryptTimes;
    private Integer encryptTimes;
    private Integer totalDecryptTimes;
    private Integer decryptTimes;
    private Integer status;
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getQueryDesc() {
        return queryDesc;
    }

    public void setQueryDesc(String queryDesc) {
        this.queryDesc = queryDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
