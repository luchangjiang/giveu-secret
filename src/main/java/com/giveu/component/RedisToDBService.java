package com.giveu.component;

import com.giveu.dao.mysql.QueryManageDao;
import com.giveu.dao.mysql.SecretDao;
import com.giveu.entity.QueryManage;
import com.giveu.entity.SecretManage;
import com.giveu.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RedisToDBService {

    @Autowired
    SecretDao secretDao;

    @Autowired
    QueryManageDao queryManageDao;

    @Autowired
    RedisUtil redisUtil;

    public Boolean initSecretToRedis() {
        Boolean result = true;
        List<SecretManage> secretList = secretDao.getSecretList();
        for (int i = 0; i < secretList.size(); i++) {
            result = result && initSecretToRedis(secretList.get(i));
        }
        result = result && redisUtil.set("allSecretManage", secretList, 0);
        return result;
    }

    public Boolean forceInitSecretToRedis(){
        List<SecretManage> secretList = (List<SecretManage>)redisUtil.get("allSecretManage");
        for (SecretManage item :secretList) {
             redisUtil.del(item.getSecretCommand());
        }
        redisUtil.del("allSecretManage");
        return initSecretToRedis();
    }

    /**
     * 单个初始化秘钥(用于更新加解密次数)
     *
     * @param secretManage
     * @return
     */
    public Boolean initSecretToRedis(SecretManage secretManage) {
        Boolean result;
        result = redisUtil.set(secretManage.getSecretCommand(), secretManage, 0);
        return result;
    }

    /**
     *根据条件获取秘钥
     * @return
     */
    public List<SecretManage> getPwdByCommand(String command, Integer status, Integer version, Integer index) {
        List<SecretManage> secretList;
        List<SecretManage> resultList = new ArrayList<>();
        if (!redisUtil.hasKey("allSecretManage")) {
            secretList = secretDao.getSecretList();
            redisUtil.set("allSecretManage", secretList, 0);
        } else {
            secretList = (List<SecretManage>) redisUtil.get("allSecretManage");
        }
        for (int i = 0; i < secretList.size(); i++) {
            //SecretManage item =secretList.get(i);
            boolean result = true;
            if (command != null && !command.isEmpty() && !command.equals("")) {
                if (!secretList.get(i).getSecretCommand().equals(command)) {
                    result = false;
                }
            }
            if (result && status != null) {
                if (secretList.get(i).getStatus() != status) {
                    result = false;
                }
            }
            if (result && version != null) {
                if (secretList.get(i).getVersion() != version) {
                    result = false;
                }
            }
            if (result && index != null) {
                if (secretList.get(i).getSecretIndex() != index) {
                    result = false;
                }
            }
            if (result) {
                resultList.add(secretList.get(i));
            }
        }
        return resultList;
    }

    /**
     * 批量初始化查询口令Redis
     *
     * @return
     */
    public Boolean initCommandToRedis() {
        Boolean result = true;
        List<QueryManage> list = queryManageDao.getQueryManageList();
        for (int i = 0; i < list.size(); i++) {
            result = result && initCommandToRedis(list.get(i).getQueryCommand(), list.get(i));
        }
        return result;
    }

    /**
     * 单个初始化 查询口令redis
     *
     * @param queryCommand
     * @param queryManage
     * @return
     */
    public Boolean initCommandToRedis(String queryCommand, QueryManage queryManage) {
        Boolean result=false;
        if (queryManage == null) {
            if (queryCommand == null || queryCommand.equals("")) {
                return false;
            }
            queryManage = queryManageDao.getQueryManage(queryCommand);
        }
        if(queryManage!=null) {
            result = redisUtil.set(queryManage.getQueryCommand(), queryManage, 0);
        }
        return result;
    }

    /**
     * 单个更新 查询口令redis
     *
     * @param queryCommand
     * @param queryManage
     * @return
     */
    public Boolean updateCommandToRedis(String queryCommand, QueryManage queryManage) {
        Boolean result=false;
        if (queryManage == null) {
            if (queryCommand == null || queryCommand.equals("")) {
                return false;
            }
            queryManage = queryManageDao.getQueryManage(queryCommand);
        }
        if(queryManage!=null) {
            result = redisUtil.set(queryManage.getQueryCommand(), queryManage, 0);
        }
        return result;
    }

    /**
     * 从Redis更新秘钥到数据库
     * @return
     */
    public Integer updateSecretFromRedis() {
        int count = 0;
        List<SecretManage> secretList = secretDao.getSecretList();
        for (int i = 0; i < secretList.size(); i++) {
            SecretManage secretManage = secretList.get(i);
            if (!redisUtil.hasKey(secretManage.getSecretCommand())) {  //判断是否存在
                redisUtil.set(secretManage.getSecretCommand(), secretManage, 0);
            } else {
                SecretManage queryRedis = (SecretManage) redisUtil.get(secretManage.getSecretCommand());
                count += secretDao.updateTime(queryRedis);
            }
        }
        return count;
    }

    /**
     * 从Redis更新查询口令到数据库
     * @return
     */
    public Integer updateCommandFromRedis() {
        int count = 0;
        List<QueryManage> list = queryManageDao.getQueryManageList();
        for (int i = 0; i < list.size(); i++) {
            QueryManage queryManage = list.get(i);
            //redisUtil.del(queryManage.getQueryCommand());
            int useTimes = 0;
            int decryptTimes = 0;
            int encryptTimes = 0;
            if (!redisUtil.hasKey(queryManage.getQueryCommand())) {
                redisUtil.set(queryManage.getQueryCommand(), queryManage, 0);
            } else {
                QueryManage queryRedis = (QueryManage) redisUtil.get(queryManage.getQueryCommand());
                useTimes = queryRedis.getUseTimes();
                decryptTimes = queryRedis.getDecryptTimes();
                encryptTimes = queryRedis.getEncryptTimes();
                count += queryManageDao.updateQueryUseTimes(useTimes, encryptTimes, decryptTimes, queryManage.getQueryCommand());
            }
        }
        return count;
    }

    /**
     * 更新秘钥加解密次数
     * @return
     */
    public Boolean updateSecretTimes(String command, int decryptTimes, int encryptTimes) {
        Boolean result = true;
        if (!redisUtil.hasKey(command)) {
            SecretManage secretManage = new SecretManage();
            secretManage.setSecretCommand(command);
            secretManage = secretDao.getSecretManage(secretManage);
            result = result && redisUtil.set(command, secretManage, 0);
        } else {
            SecretManage secretManage = (SecretManage) redisUtil.get(command);
            if (decryptTimes > 0) {
                secretManage.setDecryptTimes(secretManage.getDecryptTimes() + decryptTimes);
                secretManage.setUpdateTime(new Date());
            }
            if (encryptTimes > 0) {
                secretManage.setEncryptTimes(secretManage.getEncryptTimes() + encryptTimes);
                secretManage.setUpdateTime(new Date());
            }
            result = result && redisUtil.set(command, secretManage, 0);
        }
        return result;
    }

    /**
     * 更新查询口令解密次数
     * @return
     */
    public Boolean updateCommandDecryptTimes(String queryCommand, int decryptTimes, int useTimes) {
        return updateCommandTimes(queryCommand,"decryptTimes",decryptTimes, useTimes);
    }

    /**
     * 更新查询口令加密次数
     * @return
     */
    public Boolean updateCommandEncryptTimes(String queryCommand, int encryptTimes, int useTimes) {
        return updateCommandTimes(queryCommand,"encryptTimes",encryptTimes, useTimes);
    }

    private Boolean updateCommandTimes(String queryCommand, String type, int times, int useTimes) {
        Boolean result = true;
        if (!redisUtil.hasKey(queryCommand)) {
            QueryManage queryManage = queryManageDao.getQueryManage(queryCommand);
            result = result && initCommandToRedis(queryCommand, queryManage);
        } else {
            QueryManage queryManage = (QueryManage) redisUtil.get(queryCommand);
            if (times > 0) {
                if (type.equals("encryptTimes")) {
                    queryManage.setEncryptTimes(queryManage.getEncryptTimes() + times);
                    queryManage.setUseTimes(queryManage.getUseTimes() + useTimes);
                } else if (type.equals("decryptTimes")) {
                    queryManage.setDecryptTimes(queryManage.getDecryptTimes() + times);
                    queryManage.setUseTimes(queryManage.getUseTimes() + useTimes);
                }
                queryManage.setUpdateTime(new Date());
                result = result && redisUtil.set(queryCommand, queryManage, 0);
            }
        }
        return result;
    }

    /**
     * 在redis里获取查询口令
     * @param queryCommand
     * @return
     */
    public QueryManage getQueryManageRedis(String queryCommand){
        if(queryCommand==null ||queryCommand.equals("")){
            return null;
        }
        if (!redisUtil.hasKey(queryCommand)) {
            initCommandToRedis(queryCommand,null);
        }
        return (QueryManage)redisUtil.get(queryCommand);
    }
}
