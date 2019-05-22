package com.giveu.service.impl;

import com.giveu.common.ResponseCode;
import com.giveu.component.RedisToDBService;
import com.giveu.entity.EncryptData;
import com.giveu.entity.QueryManage;
import com.giveu.entity.ResponseModel;
import com.giveu.service.QueryPasswordService;
import com.giveu.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QueryPasswordServiceImpl implements QueryPasswordService {

//    @Autowired
//    QueryManageDao queryManageDao;

    @Autowired
    RedisToDBService redisToDBService;

    @Autowired
    SecretService secretService;

    /**
     * 获取密口令可用次数
     *
     * @param pwd  秘钥
     * @param type 类型 0：加密 1：解密
     * @return
     */
    @Override
    public ResponseModel<String> getAvailableTimes(String pwd, Integer type) {
        //QueryManage mod = queryManageDao.getQueryManage(pwd);
        QueryManage mod = redisToDBService.getQueryManageRedis(pwd);
        ResponseModel<String> res = new ResponseModel<>();
        if (mod == null) {
            res.setMessage("查询口令不存在");
            res.setResult(false);
            res.setCode("0");
            return res; //查询口令不存在
        }
        if (mod.getStatus() == 0) { //状态为0，禁用
            res.setMessage("查询口令已禁用");
            res.setResult(false);
            res.setCode("0");
            return res;
        }
        if (mod.getTotalUseTimes() <= mod.getUseTimes()) { //可用次数已用完
            res.setMessage("查询口令可用次数已用完");
            res.setResult(false);
            res.setCode("0");
            return res;
        }
        try {
            Date dt = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
            String now = f.format(dt);
            String endDate = f.format(mod.getEndDate());
            Date dateNow = f.parse(now);
            Date dateEnd = f.parse(endDate);
            if (dateNow.getTime() > dateEnd.getTime()) {
                res.setMessage("查询口令已过期");
                res.setResult(false);
                res.setCode("0");
                return res;
            }
        } catch (ParseException ex) {
            res.setMessage("查询口令已过期");
            res.setResult(false);
            res.setCode("0");
            return res;
        }
        if (type == 1) {
            if (mod.getTotalDecryptTimes() <= mod.getDecryptTimes()) { //
                res.setMessage("可用解密次数已用完");
                res.setResult(false);
                res.setCode("0");
            } else {
                res.setMessage("");
                res.setResult(true);
                Integer times = mod.getTotalDecryptTimes() - mod.getDecryptTimes();
                res.setCode(times.toString());
            }
            return res;
        } else if (type == 0) {
            if (mod.getTotalEncryptTimes() <= mod.getEncryptTimes()) { //可用加密次数已用完
                res.setMessage("可用加密次数已用完");
                res.setResult(false);
                res.setCode("0");
            } else {
                res.setMessage("");
                res.setResult(true);
                Integer times = mod.getTotalEncryptTimes() - mod.getEncryptTimes();
                res.setCode(times.toString());
            }
            return res;
        } else {
            res.setMessage("操作异常");
            res.setResult(false);
            res.setCode("0");
            return res;
        }
    }

    @Override
    public ResponseModel<EncryptData> encrypt(List<String> sources, String pwd) {
        ResponseModel<String> verify = getAvailableTimes(pwd, 0);
        ResponseModel res = new ResponseModel();
        ArrayList<EncryptData> result = new ArrayList();
        int countEmpty = 0;
        res.setResult(false);
        if(sources==null){
            res.setCode(ResponseCode.ERROR.getIndex());
            res.setData(result);
            res.setMessage("加密数据不能为空");
            return res;
        }
        for (String item : sources) {
            if (item == null || item.trim().isEmpty()) {
                countEmpty++;
            }
            result.add(new EncryptData(item, item));
        }
        if (verify.getResult() == false) {
            res.setCode(ResponseCode.ERROR.getIndex());
            res.setMessage(verify.getMessage());
            res.setData(result);
            res.setResult(false);
            return res;
        }
        Integer lastTimes = Integer.parseInt(verify.getCode());
        int needTimes = sources.size() - countEmpty;
        if (lastTimes < needTimes) {  //次数只够查询一部分数据
            redisToDBService.updateCommandEncryptTimes(pwd, lastTimes, 1);
            //queryManageDao.updateUseTimes(1, lastTimes, 0, pwd);
            result = secretService.encrypt(sources.subList(0, lastTimes));
            for (int i = lastTimes; i < sources.size(); i++) {
                result.add(new EncryptData(sources.get(i), sources.get(i)));
            }
            res.setMessage("查询口令剩余次数只能加密前" + lastTimes.toString() + "条数据");
        } else {
            redisToDBService.updateCommandEncryptTimes(pwd, needTimes, 1);
            //queryManageDao.updateUseTimes(1, sources.size(), 0, pwd);
            result = secretService.encrypt(sources);
            res.setMessage("加密成功");
        }
        res.setCode(ResponseCode.OK.getIndex());
        res.setData(result);
        res.setResult(true);
        return res;
    }

    @Override
    public ResponseModel<EncryptData> decrypt(List<String> sources, String pwd) {
        ResponseModel<String> verify = getAvailableTimes(pwd, 1);
        ResponseModel res = new ResponseModel();
        res.setResult(false);
        ArrayList<EncryptData> result = new ArrayList();
        if(sources==null) {
            res.setCode(ResponseCode.ERROR.getIndex());
            res.setData(result);
            res.setMessage("解密数据不能为空");
            return res;
        }
        int countEmpty = 0;
        for (String item : sources) {
            if (item == null || item.trim().isEmpty()) {
                countEmpty++;
            }
            result.add(new EncryptData(item, item));
        }
        if (verify.getResult() == false) {
            res.setResult(false);
            res.setCode(ResponseCode.ERROR.getIndex());
            res.setMessage(verify.getMessage());
            res.setData(result);
            return res;
        }
        Integer lastTimes = Integer.parseInt(verify.getCode());
        int needTimes = sources.size() - countEmpty;
        if (lastTimes < needTimes) {  //次数只够查询一部分数据
            redisToDBService.updateCommandDecryptTimes(pwd, lastTimes, 1);
            //queryManageDao.updateUseTimes(1, 0, lastTimes, pwd);
            result = secretService.decrypt(sources.subList(0, lastTimes));
            for (int i = lastTimes; i < sources.size(); i++) {
                result.add(new EncryptData(sources.get(i), sources.get(i)));
            }
            res.setMessage("查询口令剩余次数只能解密前" + lastTimes.toString() + "条数据");
        } else {
            redisToDBService.updateCommandDecryptTimes(pwd, needTimes, 1);
            //queryManageDao.updateUseTimes(1, 0, sources.size(), pwd);
            result = secretService.decrypt(sources);
            res.setMessage("解密成功");
        }
        res.setCode(ResponseCode.OK.getIndex());
        res.setData(result);
        res.setResult(true);
        return res;
    }
}
