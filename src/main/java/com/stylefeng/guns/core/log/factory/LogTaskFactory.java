package com.stylefeng.guns.core.log.factory;

import com.stylefeng.guns.common.constant.state.LogSucceed;
import com.stylefeng.guns.common.constant.state.LogType;
import com.stylefeng.guns.common.persistence.dao.LoginLogMapper;
import com.stylefeng.guns.common.persistence.model.SecretLoginLog;
import com.stylefeng.guns.common.persistence.model.SecretOperationLog;
import com.stylefeng.guns.core.db.Db;
import com.stylefeng.guns.core.log.LogManager;
import com.stylefeng.guns.core.util.ToolUtil;
import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * 日志操作任务创建工厂
 *
 * @author fengshuonan
 * @date 2016年12月6日 下午9:18:27
 */
public class LogTaskFactory {

    private static Logger logger = Logger.getLogger(LogManager.class);
    private static LoginLogMapper loginLogMapper = Db.getMapper(LoginLogMapper.class);
    //private static OperationLogMapper operationLogMapper = Db.getMapper(OperationLogMapper.class);

    public static TimerTask loginLog(final Integer userId, final String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SecretLoginLog secretLoginLog = LogFactory.createLoginLog(LogType.LOGIN, userId, null, ip);
                    loginLogMapper.insert(secretLoginLog);
                } catch (Exception e) {
                    logger.error("创建登录日志异常!", e);
                }
            }
        };
    }

    public static TimerTask loginLog(final String username, final String msg, final String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                SecretLoginLog secretLoginLog = LogFactory.createLoginLog(
                        LogType.LOGIN_FAIL, null, "账号:" + username + "," + msg, ip);
                try {
                    loginLogMapper.insert(secretLoginLog);
                } catch (Exception e) {
                    logger.error("创建登录失败异常!", e);
                }
            }
        };
    }

    public static TimerTask exitLog(final Integer userId, final String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                SecretLoginLog secretLoginLog = LogFactory.createLoginLog(LogType.EXIT, userId, null,ip);
                try {
                    loginLogMapper.insert(secretLoginLog);
                } catch (Exception e) {
                    logger.error("创建退出日志异常!", e);
                }
            }
        };
    }

    public static TimerTask bussinessLog(final Integer userId, final String bussinessName, final String clazzName, final String methodName, final String msg) {
        return new TimerTask() {
            @Override
            public void run() {
                SecretOperationLog secretOperationLog = LogFactory.createOperationLog(
                        LogType.BUSSINESS, userId, bussinessName, clazzName, methodName, msg, LogSucceed.SUCCESS);
                try {
                    //operationLogMapper.insert(secretOperationLog);
                } catch (Exception e) {
                    logger.error("创建业务日志异常!", e);
                }
            }
        };
    }

    public static TimerTask exceptionLog(final Integer userId, final Exception exception) {
        return new TimerTask() {
            @Override
            public void run() {
                String msg = ToolUtil.getExceptionMsg(exception);
                SecretOperationLog secretOperationLog = LogFactory.createOperationLog(
                        LogType.EXCEPTION, userId, "", null, null, msg, LogSucceed.FAIL);
                try {
                    //operationLogMapper.insert(secretOperationLog);
                } catch (Exception e) {
                    logger.error("创建异常日志异常!", e);
                }
            }
        };
    }
}
