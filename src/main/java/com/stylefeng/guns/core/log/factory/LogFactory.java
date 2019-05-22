package com.stylefeng.guns.core.log.factory;

import com.stylefeng.guns.common.constant.state.LogSucceed;
import com.stylefeng.guns.common.constant.state.LogType;
import com.stylefeng.guns.common.persistence.model.SecretLoginLog;
import com.stylefeng.guns.common.persistence.model.SecretOperationLog;

import java.util.Date;

/**
 * 日志对象创建工厂
 *
 * @author fengshuonan
 * @date 2016年12月6日 下午9:18:27
 */
public class LogFactory {

    /**
     * 创建操作日志
     *
     * @author fengshuonan
     * @Date 2017/3/30 18:45
     */
    public static SecretOperationLog createOperationLog(LogType logType, Integer userId, String bussinessName, String clazzName, String methodName, String msg, LogSucceed succeed) {
        SecretOperationLog secretOperationLog = new SecretOperationLog();
        secretOperationLog.setLogType(logType.getMessage());
        secretOperationLog.setLogName(bussinessName);
        secretOperationLog.setUserId(userId);
        secretOperationLog.setClassName(clazzName);
        secretOperationLog.setMethod(methodName);
        secretOperationLog.setCreateTime(new Date());
        secretOperationLog.setSucceed(succeed.getMessage());
        secretOperationLog.setMessage(msg);
        return secretOperationLog;
    }

    /**
     * 创建登录日志
     *
     * @author fengshuonan
     * @Date 2017/3/30 18:46
     */
    public static SecretLoginLog createLoginLog(LogType logType, Integer userId, String msg, String ip) {
        SecretLoginLog secretLoginLog = new SecretLoginLog();
        secretLoginLog.setLogName(logType.getMessage());
        secretLoginLog.setUserId(userId);
        secretLoginLog.setCreateTime(new Date());
        secretLoginLog.setSucceed(LogSucceed.SUCCESS.getMessage());
        secretLoginLog.setIp(ip);
        secretLoginLog.setMessage(msg);
        return secretLoginLog;
    }
}
