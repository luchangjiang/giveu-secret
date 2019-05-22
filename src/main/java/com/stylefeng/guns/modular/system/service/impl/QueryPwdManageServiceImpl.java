package com.stylefeng.guns.modular.system.service.impl;

import com.giveu.entity.QueryManage;
import com.giveu.component.RedisToDBService;
import com.giveu.util.PassportUtil;
import com.giveu.util.RedisUtil;
import com.stylefeng.guns.common.constant.tips.ErrorTip;
import com.stylefeng.guns.common.constant.tips.SuccessTip;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.persistence.model.QueryPwdManage;
import com.stylefeng.guns.modular.system.dao.QueryPwdManageDao;
import com.stylefeng.guns.modular.system.service.IQueryPwdManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询口令管理Dao
 *
 * @author fengshuonan
 * @Date 2018-09-04 20:20:09
 */
@Service
public class QueryPwdManageServiceImpl implements IQueryPwdManageService {

    @Autowired
    QueryPwdManageDao queryPwdManageDao;

//    @Autowired
//    QueryPwdManageMapper queryPwdManageMapper;

    @Autowired
    RedisToDBService redisToDBServiceImpl;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public QueryManage getQueryPwdManageByid(Integer queryPwdManageId) {
        return queryPwdManageDao.getQuerPwdById(queryPwdManageId);
    }

    @Override
    public List<Map<String, Object>> getQueryPwdList(String queryPwd) {
        redisToDBServiceImpl.updateCommandFromRedis();
        return queryPwdManageDao.selectQueryPwd(queryPwd);
    }

    @Override
    public Tip updateQueryPwdById(QueryManage queryPwdManage) {
        Integer result = queryPwdManageDao.updateById(queryPwdManage);
        if (result != 1) {
            ErrorTip tip = new ErrorTip(0, "更新失败");
            return tip;
        }
        redisToDBServiceImpl.updateCommandToRedis(queryPwdManage.getQueryCommand(), null);
        SuccessTip tip = new SuccessTip();
        tip.setMessage("更新成功");
        return tip;
    }

    @Override
    public QueryManage getQuerPwdByPassword(String command) {
        return queryPwdManageDao.getQuerPwdByPassword(command);
    }

    @Override
    public Tip insertQueryPwdManage(QueryManage queryPwdManage) {
        String strCommand;
        // 判断查询口令是否重复
        boolean isRepeat=true;
        do{
            strCommand=getStrCommand();
            QueryManage thePwd = getQuerPwdByPassword(strCommand);
            if (thePwd == null) {
                isRepeat=false;
            }
        }while (isRepeat);
        queryPwdManage.setQueryCommand(strCommand);
        Integer result = queryPwdManageDao.insert(queryPwdManage);
        if (result != 1) {
            ErrorTip tip = new ErrorTip(0, "插入失败");
            return tip;
        }
        redisToDBServiceImpl.initCommandToRedis(queryPwdManage.getQueryCommand(), queryPwdManage);
        SuccessTip tip = new SuccessTip();
        tip.setMessage("插入成功");
        return tip;
    }

    @Override
    public Tip deleteQueryPwdManage(Integer queryPwdManageId) {
        QueryManage queryPwdManage = getQueryPwdManageByid(queryPwdManageId);
        if(queryPwdManage.getStatus()==0){
            ErrorTip tip = new ErrorTip(0, "该秘钥已禁用");
            return tip;
        }
        Integer result = queryPwdManageDao.deleteById(queryPwdManageId);
        if (result != 1) {
            ErrorTip tip = new ErrorTip(0, "禁用失败");
            return tip;
        }
        queryPwdManage.setStatus(0);
        queryPwdManage.setUpdateTime(new Date());
        redisToDBServiceImpl.initCommandToRedis(null, queryPwdManage);
        SuccessTip tip = new SuccessTip();
        tip.setMessage("禁用成功");
        return tip;
    }

    public QueryManage autoMapper(QueryPwdManage queryPwdManage) {
        QueryManage queryManage = new QueryManage();
        if (queryManage != null) {
            queryManage.setId(queryManage.getId());
            queryManage.setUseTimes(queryManage.getUseTimes());
            queryManage.setDecryptTimes(queryManage.getDecryptTimes());
            queryManage.setEncryptTimes(queryManage.getEncryptTimes());
            queryManage.setTotalDecryptTimes(queryManage.getTotalDecryptTimes());
            queryManage.setTotalEncryptTimes(queryManage.getTotalEncryptTimes());
            queryManage.setTotalUseTimes(queryManage.getTotalUseTimes());
            queryManage.setUpdateTime(queryManage.getUpdateTime());
            queryManage.setQueryCommand(queryManage.getQueryCommand());
            queryManage.setQueryDesc(queryManage.getQueryDesc());
            queryManage.setEndDate(queryManage.getEndDate());
            queryManage.setUpdateUser(queryManage.getUpdateUser());
        }
        return queryManage;
    }

    public String getStrCommand() {
        Date dt = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        return f.format(dt) + PassportUtil.getRandomValue(false);
    }
}

