package com.stylefeng.guns.modular.system.service.impl;

import com.giveu.entity.SecretManage;
import com.giveu.component.RedisToDBService;
import com.giveu.util.PassportUtil;
import com.stylefeng.guns.common.constant.tips.ErrorTip;
import com.stylefeng.guns.common.constant.tips.SuccessTip;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.modular.system.dao.SecretManageDao;
import com.stylefeng.guns.modular.system.service.ISecretManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 密口令Dao
 *
 * @author fengshuonan
 * @Date 2018-09-05 19:32:39
 */
@Service
public class SecretManageServiceImpl implements ISecretManageService {

    @Autowired
    SecretManageDao secretManageDao;

    @Autowired
    RedisToDBService redisToDBServiceImpl;

    /**
     * 获取列表
     */
    @Override
    public List<SecretManage> getList(Integer secretType) {
        return secretManageDao.getSecretGroup(secretType);
    }

    @Override
    public List<Map<String, Object>> selectSecretManage(Integer status) {
        redisToDBServiceImpl.updateSecretFromRedis();
        //redisToDBServiceImpl.forceInitSecretToRedis();
        return secretManageDao.selectSecretManage(status);
    }

    // TODO:+事务

    /**
     * 禁用密钥服务
     */
    @Override
    @Transactional
    public Tip instead(String uuid) {
        Integer version = PassportUtil.getMaxVersion();
        SecretManage param = new SecretManage();
        //UUID uuid = UUID.randomUUID();
        param.setUuid(uuid);
        SecretManage oldSecret = secretManageDao.getSecretManage(param);
        if (oldSecret.getStatus() == 0) {
            ErrorTip tip = new ErrorTip(0, "该秘钥已禁用");
            return tip;
        }
        SecretManage newSecret = createSecretManage();
        if (secretManageDao.updateSecretStatus(uuid) == 1) {
            UUID newUuid = UUID.randomUUID();
            newSecret.setUuid(newUuid.toString());
            newSecret.setSecretIndex(oldSecret.getSecretIndex());
            newSecret.setVersion(version);
            Integer result = secretManageDao.insertSecretManage(newSecret);
            if (result != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ErrorTip tip = new ErrorTip(0, "更新失败");
                return tip;
            }
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ErrorTip tip = new ErrorTip(0, "更新失败");
            return tip;
        }
        redisToDBServiceImpl.initSecretToRedis();
        SuccessTip tip = new SuccessTip();
        tip.setMessage("更新成功");
        return tip;
    }

    // TODO:+事务

    /**
     * 全部36组密钥更新
     */
    @Override
    @Transactional
    public Tip insteadAll() {
        //redisToDBServiceImpl.forceInitSecretToRedis();
        Integer version = PassportUtil.getMaxVersion();
        List<SecretManage> oldSecretList = secretManageDao.getSecretGroup(2);
        for (int i = 0; i < 36; i++) {
            SecretManage newSecret = createSecretManage();
            if (oldSecretList != null && oldSecretList.size() > 0) {
                SecretManage oldSecret = oldSecretList.get(i);
                secretManageDao.updateSecretStatus(oldSecret.getUuid());
            }
            newSecret.setSecretIndex(i);
            UUID uuid = UUID.randomUUID();
            newSecret.setUuid(uuid.toString());
            newSecret.setVersion(version);
            if (secretManageDao.insertSecretManage(newSecret) != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ErrorTip tip = new ErrorTip(0, "替换失败");
                return tip;
            }

        }
        redisToDBServiceImpl.initSecretToRedis();
        SuccessTip tip = new SuccessTip();
        tip.setMessage("替换成功");
        return tip;
    }

    // TODO:+事务

    /**
     * 生成36组新密钥
     */
    @Override
    public Boolean createSecretGroup(String version) {
        boolean flag = true;

        for (int i = 0; i < 36; i++) {

            String command = "";

//			int j = 0;
//			for (j = 0; j < maxTimes; j++) {
//				command = PassportUtil.getRandomValue();
//				param=new SecretManage();
//				param.setSecretCommand(command);
//				result=secretManageDao.getSecretManage(param);
//				if(result==null){
//					break;
//				}
//			}
//
//			if (j == maxTimes) {
//				throw new Exception("密口令生成次数超限！");
//			}
//
//			String password = "";
//
//			int k = 0;
//			for (k = 0; k < maxTimes; k++) {
//				password = PassportUtil.getRandomValue();
//			}
//
//			if (i == maxTimes) {
//				throw new Exception("密钥生成次数超限！");
//			}
//
//			SecretManage secretManage = new SecretManage();
//			secretManage.setSecretCommand(command);
//			secretManage.setSecretPassword(password);
        }

        return flag;
    }

    /**
     * 创建一组密口令和和秘钥
     */
    private SecretManage createSecretManage() {
        int maxTimes = 200;
        SecretManage param;
        SecretManage result;

        String command = "";
        String password = "";

        int j = 0;
        for (j = 0; j < maxTimes; j++) {
            command = PassportUtil.getRandomValue(false);
            param = new SecretManage();
            param.setSecretCommand(command);
            result = secretManageDao.getSecretManage(param);
            if (result == null) {
                break;
            }
        }
        for (j = 0; j < maxTimes; j++) {
            password = PassportUtil.getRandomValue(true);
            param = new SecretManage();
            param.setSecretPassword(password);
            result = secretManageDao.getSecretManage(param);
            if (result == null) {
                break;
            }
        }
        param = new SecretManage();
        param.setSecretCommand(command);
        param.setSecretPassword(password);
        return param;
    }

}
