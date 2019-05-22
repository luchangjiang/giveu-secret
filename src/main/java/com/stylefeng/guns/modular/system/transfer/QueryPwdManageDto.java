package com.stylefeng.guns.modular.system.transfer;

import com.stylefeng.guns.common.util.PatternReg;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class QueryPwdManageDto {

    private Integer id;
    private String queryCommand;

    private String queryDesc;
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "有效截止日期不能为空")
    private Date endDate;
    @PatternReg(regexp = "^[1-9]+[0-9]*$",message = "设定使用次数必须为正整数")
    private Integer totalUseTimes;
    private Integer useTimes;
    @PatternReg(regexp = "^[1-9]+[0-9]*$",message = "设定加密次数必须为正整数")
    private Integer totalEncryptTimes;
    private Integer encryptTimes;
    @PatternReg(regexp = "^[1-9]+[0-9]*$",message = "设定解密次数必须为正整数")
    private Integer totalDecryptTimes;
    private Integer decryptTimes;
    private Integer status;
    private Date updateTime;

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
}
